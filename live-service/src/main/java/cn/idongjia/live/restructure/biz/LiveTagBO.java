package cn.idongjia.live.restructure.biz;

import cn.idongjia.live.db.mybatis.po.LiveTagPO;
import cn.idongjia.live.db.mybatis.query.DBLiveShowQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveTagQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveTagRelQuery;
import cn.idongjia.live.db.mybatis.query.DBPlayBackQuery;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.purelive.tag.ColumnTag;
import cn.idongjia.live.pojo.purelive.tag.PureLiveTagDO;
import cn.idongjia.live.pojo.purelive.tag.PureLiveTagRelDO;
import cn.idongjia.live.pojo.purelive.tag.TemplateTagRelDO;
import cn.idongjia.live.query.purelive.tag.PureLiveTagRelSearch;
import cn.idongjia.live.query.purelive.tag.PureLiveTagSearch;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.domain.entity.live.LiveTagRel;
import cn.idongjia.live.restructure.dto.*;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.domain.manager.LiveEntityManager;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.query.LiveShowQueryHandler;
import cn.idongjia.live.restructure.query.LiveTagQueryHandler;
import cn.idongjia.live.restructure.query.PlayBackQueryHandler;
import cn.idongjia.live.restructure.query.QueryFactory;
import cn.idongjia.live.restructure.repo.LiveTagRepo;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static cn.idongjia.live.support.LiveConst.STATE_LIVE_OVER;

/**
 * @author lc
 * @create at 2018/6/14.
 */
@Component
public class LiveTagBO {

    private static final Log LOGGER = LogFactory.getLog(LiveTagBO.class);

    @Resource
    private LiveTagQueryHandler liveTagQueryHandler;

    @Resource
    private LiveShowQueryHandler liveShowQueryHandler;

    @Resource
    private PlayBackQueryHandler playBackQueryHandler;

    @Resource
    private LiveTagRepo livePureTagRepo;

    @Resource
    private LiveEntityManager liveEntityManager;


    public PureLiveTagDO get(Long tagId) {
        LiveTagDTO pureLiveTagDTO = liveTagQueryHandler.get(tagId);
        return pureLiveTagDTO.assemblePureLiveTagDO();
    }

    public List<PureLiveTagDO> list(PureLiveTagSearch pureLiveTagSearch) {
        DBLiveTagQuery dbLiveTagQuery = QueryFactory.getInstance(pureLiveTagSearch);
        try {
            List<LiveTagDTO> pureLiveTagDTOS = liveTagQueryHandler.list(dbLiveTagQuery).get();
            return pureLiveTagDTOS.stream().map(LiveTagDTO::assemblePureLiveTagDO).collect(Collectors.toList());
        } catch (Exception e) {
            LiveTagBO.LOGGER.error("查询标签失败{}", e);
            throw LiveException.failure("查询标签失败");
        }
    }

    public Integer count(PureLiveTagSearch pureLiveTagSearch) {
        DBLiveTagQuery dbLiveTagQuery = QueryFactory.getInstance(pureLiveTagSearch);
        return liveTagQueryHandler.count(dbLiveTagQuery);
    }

    public BaseList<PureLiveTagDO> page(PureLiveTagSearch pureLiveTagSearch) {
        DBLiveTagQuery dbLiveTagQuery = QueryFactory.getInstance(pureLiveTagSearch);
        Integer count = liveTagQueryHandler.count(dbLiveTagQuery);
        BaseList<PureLiveTagDO> baseList = new BaseList<>();
        baseList.setCount(count);
        if (count > 0) {
            try {

                List<LiveTagDTO> pureLiveTagDTOS = liveTagQueryHandler.list(dbLiveTagQuery).get();
                List<PureLiveTagDO> pureLiveTagDOS = pureLiveTagDTOS.stream().map(LiveTagDTO::assemblePureLiveTagDO).collect(Collectors.toList());
                baseList.setItems(pureLiveTagDOS);
            } catch (Exception e) {
                LiveTagBO.LOGGER.error("查询标签失败{}", e);
                throw LiveException.failure("查询标签失败");
            }
        }
        return baseList;
    }

    public List<PureLiveTagDO> listWithoutFinished(PureLiveTagSearch pureLiveTagSearch) {
        DBLiveTagQuery dbLiveTagQuery = QueryFactory.getInstance(pureLiveTagSearch);
        dbLiveTagQuery.setStatus(0);
        try {
            List<LiveTagDTO> pureLiveTagDTOS = liveTagQueryHandler.list(dbLiveTagQuery).get();

            List<Long> tagIds = pureLiveTagDTOS.stream().map(LiveTagDTO::getId).collect(Collectors.toList());
            DBLiveTagRelQuery dbLiveTagRelQuery = DBLiveTagRelQuery.builder().status(0).page(pureLiveTagSearch.getPage()).limit(pureLiveTagSearch.getLimit()).tagIds(tagIds).build();
            List<LiveTagRelDTO> liveTagRelDTOS = liveTagQueryHandler.list(dbLiveTagRelQuery).get();
            List<Long> liveIds = new ArrayList<>();
            Map<Long, Long> liveIdTagIdRelMap = new HashMap<>();
            liveTagRelDTOS.stream().forEach(liveTagRelDTO -> {
                liveIds.add(liveTagRelDTO.getId());
                liveIdTagIdRelMap.put(liveTagRelDTO.getLiveId(), liveTagRelDTO.getId());
            });

            DBLiveShowQuery dbLiveShowQuery = DBLiveShowQuery.builder().ids(liveIds).states(Arrays.asList(STATE_LIVE_OVER)).build();
            //查询已结束的直播
            List<LiveShowDTO> liveShowDTOS = liveShowQueryHandler.list(dbLiveShowQuery).get();
            List<Long> playbackLiveIds = liveShowDTOS.stream()
                    .filter(liveShowDTO -> liveShowDTO.getType().intValue() != LiveEnum.LiveType.LIVE_AUCTION.getCode())
                    .map(LiveShowDTO::getId).collect(Collectors.toList());
            //已结束并有回复的直播
            Map<Long, List<PlayBackDTO>> playBackDTOMaps = playBackQueryHandler.map(DBPlayBackQuery.builder().liveIds(playbackLiveIds).status(0).build()).get();
            List<Long> containsIds = new ArrayList<>();

            liveShowDTOS.stream().forEach(liveShowDTO -> {
                List<PlayBackDTO> playBackDTOS = playBackDTOMaps.get(liveShowDTO.getId());
                if (Utils.isEmpty(playBackDTOS)) {
                    liveIdTagIdRelMap.remove(liveShowDTO.getId());
                } else {
                    long sum = playBackDTOS.stream().mapToLong(playBackDTO -> playBackDTO.getDuration() == null ? 0 : playBackDTO.getDuration()).sum();
                    if (sum == 0) {
                        liveIdTagIdRelMap.remove(liveShowDTO.getId());
                    }
                }

            });
            Collection<Long> relTagIds = liveIdTagIdRelMap.values();
            return pureLiveTagDTOS.stream().filter(pureLiveTagDTO -> {
                return relTagIds.contains(pureLiveTagDTO.getId());
            }).map(LiveTagDTO::assemblePureLiveTagDO).collect(Collectors.toList());
        } catch (Exception e) {
            LiveTagBO.LOGGER.error("查询标签失败{}", e);
            throw LiveException.failure("查询标签失败");
        }
    }

    public List<PureLiveTagRelDO> list(PureLiveTagRelSearch pureLiveTagRelSearch) {
        DBLiveTagRelQuery dbLiveTagRelQuery = QueryFactory.getInstance(pureLiveTagRelSearch);
        try {
            List<LiveTagRelDTO> liveTagRelDTOS = liveTagQueryHandler.list(dbLiveTagRelQuery).get();
            List<Long> tagIds = liveTagRelDTOS.stream().map(LiveTagRelDTO::getId).collect(Collectors.toList());
            DBLiveTagQuery dbLiveTagQuery = DBLiveTagQuery.builder().tagIds(tagIds).build();
            Map<Long, LiveTagDTO> liveTagDTOMap = liveTagQueryHandler.map(dbLiveTagQuery).get();
            return liveTagRelDTOS.stream().map(liveTagRelDTO -> {
                return liveTagRelDTO.assemblePureLiveTagRelDO(liveTagDTOMap.get(liveTagRelDTO.getId()));
            }).collect(Collectors.toList());
        } catch (Exception e) {
            LiveTagBO.LOGGER.error("查询标签失败{}", e);
            throw LiveException.failure("查询标签失败");
        }
    }

    public Integer count(PureLiveTagRelSearch pureLiveTagRelSearch) {
        DBLiveTagRelQuery dbLiveTagRelQuery = QueryFactory.getInstance(pureLiveTagRelSearch);
        return liveTagQueryHandler.count(dbLiveTagRelQuery);

    }

    public BaseList<PureLiveTagRelDO> page(PureLiveTagRelSearch tagRelSearch) {
        DBLiveTagRelQuery dbLiveTagRelQuery = QueryFactory.getInstance(tagRelSearch);
        Integer count = liveTagQueryHandler.count(dbLiveTagRelQuery);
        BaseList<PureLiveTagRelDO> baseList = new BaseList<>();
        baseList.setCount(count);
        if (count > 0) {
            try {

                List<LiveTagRelDTO> liveTagRelDTOS = liveTagQueryHandler.list(dbLiveTagRelQuery).get();
                List<Long> tagIds = liveTagRelDTOS.stream().map(LiveTagRelDTO::getId).collect(Collectors.toList());
                DBLiveTagQuery dbLiveTagQuery = DBLiveTagQuery.builder().tagIds(tagIds).build();
                Map<Long, LiveTagDTO> liveTagDTOMap = liveTagQueryHandler.map(dbLiveTagQuery).get();

                List<PureLiveTagRelDO> liveTagRelDOS = liveTagRelDTOS.stream().map(liveTagRelDTO -> {
                    return liveTagRelDTO.assemblePureLiveTagRelDO(liveTagDTOMap.get(liveTagRelDTO.getId()));
                }).collect(Collectors.toList());
                baseList.setItems(liveTagRelDOS);
            } catch (Exception e) {
                LiveTagBO.LOGGER.error("查询标签失败{}", e);
                throw LiveException.failure("查询标签失败");
            }
        }
        return baseList;
    }

    public TemplateTagRelDO getTemplateTagRel(Long tagId) {
        LiveTagDTO pureLiveTagDTO = liveTagQueryHandler.get(tagId);
        TemplateTagRelDTO templateTagRelDTO = liveTagQueryHandler.getTemplateTagRel(pureLiveTagDTO.getId());
        if (templateTagRelDTO == null) {
            return null;
        }
        return pureLiveTagDTO.assemblePureLiveTagDTO(templateTagRelDTO);

    }

    public List<Long> list(Long tagId, PureLiveTagRelSearch pureLiveTagRelSearch) {
        pureLiveTagRelSearch.setTagId(tagId);
        DBLiveTagRelQuery dbLiveTagRelQuery = QueryFactory.getInstance(pureLiveTagRelSearch);

        List<LiveTagRelDTO> liveTagRelDTOS = null;
        try {
            liveTagRelDTOS = liveTagQueryHandler.list(dbLiveTagRelQuery).get();
            return liveTagRelDTOS.stream().map(LiveTagRelDTO::getLiveId).collect(Collectors.toList());
        } catch (Exception e) {
            LiveTagBO.LOGGER.error("查询标签失败{}", e);
            throw LiveException.failure("查询标签失败");
        }
    }

    public Map<String, String> mapTemplatesById(Long tagId) {
        Map<String, String> reMap = new HashMap<>();
        DBLiveTagQuery dbLiveTagQuery = DBLiveTagQuery.builder().tagIds(Arrays.asList(tagId)).build();

        try {
            List<LiveTagDTO> pureLiveTagDTOS = liveTagQueryHandler.list(dbLiveTagQuery).get();
            if (!Utils.isEmpty(pureLiveTagDTOS)) {
                LiveTagDTO liveTagDTO = pureLiveTagDTOS.get(0);
                TemplateTagRelDTO templateTagRelDTO = liveTagQueryHandler.getTemplateTagRel(liveTagDTO.getId());
                reMap.put("url", templateTagRelDTO.getUrl());
            }
            return reMap;
        } catch (Exception e) {
            LiveTagBO.LOGGER.error("查询标签失败{}", e);
            throw LiveException.failure("查询标签失败");
        }

    }

    public List<PureLiveTagRelDO> listByLiveId(Long liveId) {
        DBLiveTagRelQuery dbLiveTagRelQuery = DBLiveTagRelQuery.builder().liveIds(Arrays.asList(liveId)).build();
        try {
            List<LiveTagRelDTO> liveTagRelDTOS = liveTagQueryHandler.list(dbLiveTagRelQuery).get();
            List<Long> tagIds = liveTagRelDTOS.stream().map(LiveTagRelDTO::getId).collect(Collectors.toList());
            DBLiveTagQuery dbLiveTagQuery = DBLiveTagQuery.builder().tagIds(tagIds).build();
            Map<Long, LiveTagDTO> liveTagDTOMap = liveTagQueryHandler.map(dbLiveTagQuery).get();
            return liveTagRelDTOS.stream().map(liveTagRelDTO -> {
                return liveTagRelDTO.assemblePureLiveTagRelDO(liveTagDTOMap.get(liveTagRelDTO.getId()));
            }).collect(Collectors.toList());
        } catch (Exception e) {
            LiveTagBO.LOGGER.error("查询标签失败{}", e);
            throw LiveException.failure("查询标签失败");
        }
    }

    public ColumnTag getColumnTag(Long tid) {
        LiveTagDTO pureLiveTagDTO = liveTagQueryHandler.get(tid);
        TemplateTagRelDTO templateTagRel = liveTagQueryHandler.getTemplateTagRel(tid);
        return pureLiveTagDTO.assembleColumnTag(templateTagRel);
    }


    /**
     * 添加直播tag
     *
     * @param pureLiveTagDO tag 参数
     * @return id
     * @author zhangyigjie
     */
    public Long addTag(PureLiveTagDO pureLiveTagDO) {
        LiveTagDTO dto = assambleLiveTag(pureLiveTagDO);
        return livePureTagRepo.add(dto);

    }

    /**
     * 删除tag
     *
     * @param tagId tagid
     * @return true or false
     * @author zhangyingjie
     */
    public boolean deleteLiveTag(Long tagId) {
        return livePureTagRepo.delete(tagId);
    }

    /**
     * 更新tag
     *
     * @param tagId         tagId
     * @param pureLiveTagDO 需要更新的tag
     * @return true
     * @author zhangyingjie
     */
    public boolean updateLiveTag(Long tagId, PureLiveTagDO pureLiveTagDO) {
        pureLiveTagDO.setId(tagId);
        LiveTagDTO dto = assambleLiveTag(pureLiveTagDO);
        return livePureTagRepo.updateTag(dto);
    }

    /**
     * 关联直播和tag
     *
     * @param pureLiveTagRelDO 关联参数
     * @return rel 的主键id
     * @author zhangyingjie
     */
    public Long addPureLiveTagRel(PureLiveTagRelDO pureLiveTagRelDO) {
        LiveEntity entity = liveEntityManager.load(pureLiveTagRelDO.getLid());
        LiveTagRel rel = assambleLiveTagRel(pureLiveTagRelDO);
        entity.addLiveTagRel(rel);
        return rel.getId();

    }

    /**
     * 更新
     *
     * @param pureLiveTagRelDO do
     * @return true or false
     * @author zhangyingjie
     */
    public boolean updateLiveTagRel(PureLiveTagRelDO pureLiveTagRelDO) {
        LiveEntity entity = liveEntityManager.load(pureLiveTagRelDO.getLid());

        return entity.updateLiveTagRel(assambleLiveTagRel(pureLiveTagRelDO));
    }

    /**
     * 删除
     *
     * @param pureLiveTagRelDO do
     * @return true or false
     * @author zhangyingjie
     */
    public boolean deleteLiveTagRel(PureLiveTagRelDO pureLiveTagRelDO) {
        LiveEntity entity = liveEntityManager.load(pureLiveTagRelDO.getLid());

        return entity.deleteLiveTagRel(assambleLiveTagRel(pureLiveTagRelDO));
    }


    private LiveTagRel assambleLiveTagRel(PureLiveTagRelDO pureLiveTagRelDO) {
        LiveTagRel rel = LiveTagRel.builder()
                .createTm(pureLiveTagRelDO.getCreateTm())
                .id(pureLiveTagRelDO.getId())
                .lid(pureLiveTagRelDO.getLid())
                .modifiedTm(pureLiveTagRelDO.getModifiedTm())
                .status(pureLiveTagRelDO.getStatus())
                .tagId(pureLiveTagRelDO.getTagId())
                .build();
        return rel;
    }

    private LiveTagDTO assambleLiveTag(PureLiveTagDO pureLiveTagDO) {
        LiveTagPO po = LiveTagPO.builder()
                .createTime(pureLiveTagDO.getCreateTm())
                .desc(pureLiveTagDO.getDesc())
                .id(pureLiveTagDO.getId())
                .modifiedTime(pureLiveTagDO.getModifiedTm())
                .name(pureLiveTagDO.getName())
                .pic(pureLiveTagDO.getPic())
                .status(pureLiveTagDO.getStatus())
                .type(pureLiveTagDO.getType())
                .weight(pureLiveTagDO.getWeight())
                .build();
        LiveTagDTO dto = new LiveTagDTO(po);
        return dto;

    }
}
