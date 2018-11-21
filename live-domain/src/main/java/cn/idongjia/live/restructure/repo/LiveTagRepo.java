package cn.idongjia.live.restructure.repo;

import cn.idongjia.live.db.mybatis.mapper.LiveTagMapper;
import cn.idongjia.live.db.mybatis.mapper.LiveTagRelMapper;
import cn.idongjia.live.db.mybatis.mapper.TemplateTagRelMapper;
import cn.idongjia.live.db.mybatis.po.LiveTagPO;
import cn.idongjia.live.db.mybatis.po.LiveTagRelPO;
import cn.idongjia.live.db.mybatis.po.TemplateTagRelPO;
import cn.idongjia.live.db.mybatis.query.DBLiveTagQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveTagRelQuery;
import cn.idongjia.live.db.mybatis.query.DBTemplateRelQuery;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.restructure.dto.LiveTagDTO;
import cn.idongjia.live.restructure.dto.LiveTagRelDTO;
import cn.idongjia.live.restructure.dto.TemplateTagRelDTO;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.idongjia.live.support.DateTimeUtil.millis2Timestamp;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/12
 * Time: 下午10:23
 */
@Repository
public class LiveTagRepo {
    private static final Log LOGGER = LogFactory.getLog(LiveTagRepo.class);
    @Resource
    private LiveTagMapper pureLiveTagMapper;
    @Resource
    private LiveTagRelMapper pureLiveTagRelMapper;
    @Resource
    private TemplateTagRelMapper templateTagRelMapper;

    @Resource
    private ConfigManager configManager;

    /**
     * 新增标签数据
     *
     * @param liveTagDTO 标签数据
     * @return 标签ID
     */
    public Long add(LiveTagDTO liveTagDTO) {
        // 补充数据
        if (liveTagDTO.getWeight() == null) {
            liveTagDTO.setWeight(0);
        }
        liveTagDTO.setStatus(LiveConst.STATUS_TAG_NORMAL);
        liveTagDTO.setCreateTime(millis2Timestamp(Utils.getCurrentMillis()));
        LiveTagRepo.LOGGER.info("新增标签内容为: " + liveTagDTO);
        LiveTagPO liveTagPO = liveTagDTO.toDO();
        if (pureLiveTagMapper.insert(liveTagPO) > 0) {
            LiveTagRepo.LOGGER.info("新增标签成功，ID: " + liveTagPO.getId());
        } else {
            LiveTagRepo.LOGGER.info("新增失败");
        }
        return liveTagPO.getId();
    }

    /**
     * 删除标签数据
     *
     * @param tagId 标签ID
     * @return 返回是否成功
     */
    public boolean removeTag(Long tagId) {
        LiveTagRepo.LOGGER.info("要删除标签ID为: " + tagId);
        // 创建标签数据补充数据并设置状态为-1
        LiveTagPO po=LiveTagPO.builder()
                .id(tagId)
                .status(LiveConst.STATUS_TAG_DEL)
                .modifiedTime(millis2Timestamp(Utils.getCurrentMillis()))
                .build();

        boolean isSuccess = pureLiveTagMapper.update(po,Utils.getCurrentMillis()) > 0;
        if (isSuccess) {
            LiveTagRepo.LOGGER.info("标签删除成功ID: " + tagId);
        } else {
            LiveTagRepo.LOGGER.info("标签删除失败");
        }
        return isSuccess;
    }

    /**
     * 更新标签数据
     *
     * @param liveTagDTO 标签数据
     * @return 更新是否成功
     */
    public boolean updateTag(LiveTagDTO liveTagDTO) {
        LiveTagPO liveTagPO = liveTagDTO.toDO();
        boolean isSuccess = pureLiveTagMapper.update(liveTagPO,Utils.getCurrentMillis()) > 0;
        if (isSuccess) {
            LiveTagRepo.LOGGER.info("更新标签成功ID: {}", liveTagPO.getId());
        } else {
            LiveTagRepo.LOGGER.info("更新标签失败");
        }
        return isSuccess;
    }

    /**
     * 根据tag ID获取标签信息
     *
     * @param tagId 标签ID
     * @return 标签信息
     */
    public LiveTagDTO getTagById(Long tagId) {

        LiveTagPO pureLiveTagPO = pureLiveTagMapper.getById(tagId);
        return new LiveTagDTO(pureLiveTagPO);
    }

    /**
     * 根据检索条件获取标签列表
     *
     * @param dbPureLiveTagQuery 标签检索条件
     * @return 标签列表
     */
    public List<LiveTagDTO> listTags(DBLiveTagQuery dbPureLiveTagQuery) {
        List<LiveTagPO> pureLiveTagPOS = pureLiveTagMapper.list(dbPureLiveTagQuery);
        return pureLiveTagPOS.stream().map(LiveTagDTO::new).collect(Collectors.toList());
    }

    /**
     * 根据检索条件获取标签数量
     *
     * @param dbLiveTagQuery 标签检索条件
     * @return 标签数量
     */
    public Integer count(DBLiveTagQuery dbLiveTagQuery) {
        return pureLiveTagMapper.count(dbLiveTagQuery);
    }

    /**
     * 根据检索条件获取数量和内容
     *
     * @param dbLiveTagQuery 检索条件
     * @return 数量和标签
     */
    public BaseList<LiveTagDTO> page(DBLiveTagQuery dbLiveTagQuery) {
        int count = pureLiveTagMapper.count(dbLiveTagQuery);
        BaseList<LiveTagDTO> baseList = new BaseList<>();
        baseList.setCount(count);
        if (count > 0) {
            List<LiveTagPO> liveTagPOS = pureLiveTagMapper.list(dbLiveTagQuery);
            List<LiveTagDTO> liveTagDTOS = liveTagPOS.stream().map(LiveTagDTO::new).collect(Collectors.toList());
            baseList.setItems(liveTagDTOS);
        }
        return baseList;
    }


    /**
     * 增加标签关联直播关系
     *
     * @param liveTagRelDTO 标签关联直播关系
     * @return 关系ID
     */
    public Long addTagRel(LiveTagRelDTO liveTagRelDTO) {

        LiveTagRepo.LOGGER.info("新增标签关联关系内容为: {}", liveTagRelDTO);
        LiveTagRelPO liveTagRelPO = liveTagRelDTO.toDO();
        if (pureLiveTagRelMapper.insert(liveTagRelPO) > 0) {
            LiveTagRepo.LOGGER.info("新增标签关联关系成功ID: {}", liveTagRelPO);
        } else {
            LiveTagRepo.LOGGER.info("新增标签关联关系失败");
        }
        return liveTagRelPO.getId();
    }


    /**
     * 删除标签关联直播
     *
     * @param tagRelId 删除内容
     * @return 是否成功
     */
    public boolean deleteTagRel(Long tagRelId) {
        LiveTagRelDTO liveTagRelDTO = new LiveTagRelDTO(new LiveTagRelPO());
        liveTagRelDTO.setId(tagRelId);
        liveTagRelDTO.setStatus(LiveConst.STATUS_TAG_REL_DEL);
        liveTagRelDTO.setModifiedTime(millis2Timestamp(Utils.getCurrentMillis()));
        LiveTagRepo.LOGGER.info("要删除关联内容为:{} ", liveTagRelDTO);
        LiveTagRelPO liveTagRelPO = liveTagRelDTO.toDO();
        boolean isSuccess = pureLiveTagRelMapper.update(liveTagRelPO, Utils.getCurrentMillis()) > 0;
        if (isSuccess) {
            LiveTagRepo.LOGGER.info("删除关联内容成功");
        } else {
            LiveTagRepo.LOGGER.info("删除关联内容失败");
        }
        return isSuccess;
    }

    /**
     * 更新标签关联直播
     *
     * @param liveTagRelDTO 更新内容
     * @return 是否成功
     */
    public boolean updatePureLiveTagRel(LiveTagRelDTO liveTagRelDTO) {
        liveTagRelDTO.setModifiedTime(millis2Timestamp(Utils.getCurrentMillis()));
        return pureLiveTagRelMapper.update(liveTagRelDTO.toDO(), Utils.getCurrentMillis()) > 0;
    }

    /**
     * 替换原有分类tag
     *
     * @param liveTagRelDTO 分类tag
     * @return 是否成功
     */
    void replacePureLiveTagRel(LiveTagRelDTO liveTagRelDTO) {
        LiveTagRepo.LOGGER.info("替换标签关联关系内容为: {}", liveTagRelDTO);

        DBLiveTagRelQuery dbLiveTagRelQuery = DBLiveTagRelQuery.builder()
                .liveIds(Arrays.asList(liveTagRelDTO.getLiveId()))
                .status(LiveConst.STATUS_TAG_REL_NORMAL).build();
        List<LiveTagRelPO> liveTagRelPOS = pureLiveTagRelMapper.list(dbLiveTagRelQuery);
        if (!Utils.isEmpty(liveTagRelPOS)) {
            LiveTagRelPO liveTagRelPO = liveTagRelPOS.get(0);
            LiveTagRepo.LOGGER.info("已有标签关联关系内容为:{} ", liveTagRelPO);
            if (!liveTagRelPO.getTagId().equals(liveTagRelPO.getTagId())) {
                deleteTagRel(liveTagRelPO.getTagId());
                addTagRel(liveTagRelDTO);
            }
        } else {
            addTagRel(liveTagRelDTO);
        }
        LiveTagRepo.LOGGER.info("替换成功");
    }

    /**
     * 获取标签关联直播
     *
     * @param dbLiveTagRelQuery 条件
     * @return 关系列表
     */
    public List<LiveTagRelDTO> list(DBLiveTagRelQuery dbLiveTagRelQuery) {
        List<LiveTagRelPO> liveTagRelPOS = pureLiveTagRelMapper.list(dbLiveTagRelQuery);
        return liveTagRelPOS.stream().map(LiveTagRelDTO::new).collect(Collectors.toList());
    }

    /**
     * 获取标签关联直播数量
     *
     * @param dbLiveTagRelQuery 条件
     * @return 关联关系数量
     */
    public Integer count(DBLiveTagRelQuery dbLiveTagRelQuery) {
        return pureLiveTagRelMapper.count(dbLiveTagRelQuery);
    }


    /**
     * 根据tagId获取其所有有效关联的纯直播
     *
     * @param dbLiveTagQuery 标签ID
     * @return 纯直播列表
     */
    public List<LiveTagDTO> list(DBLiveTagQuery dbLiveTagQuery) {

        List<LiveTagPO> pureLiveTagPOS = pureLiveTagMapper.list(dbLiveTagQuery);
        return pureLiveTagPOS.stream().map(LiveTagDTO::new).collect(Collectors.toList());
    }

    /*
     * List<PureLivePlayBack> listPureLivePlayBacksById(Long tagId, Integer limit){
     * //构建检索条件
     * PureLiveTagRelSearch pureLiveTagRelSearch = new PureLiveTagRelSearch();
     * pureLiveTagRelSearch.setTagId(tagId);
     * pureLiveTagRelSearch.setLimit(limit);
     * pureLiveTagRelSearch.setStatus(STATUS_TAG_REL_NORMAL);
     * List<Long> liveIds =
     * pureLiveTagRelMapper.searchPureLivesIsOver(pureLiveTagRelSearch);
     * return liveIds.stream().urlMap(l ->
     * pureLiveService.getPlayBackById(l)).collect(Collectors.toList());
     *
     * }
     */

    // =====================================================

    /**
     * 增加标签和超级模版关联关系
     *
     * @param templateTagRelDTO 关系数据
     * @return 关系ID
     */
    public Long addTemplateTagRel(TemplateTagRelDTO templateTagRelDTO) {
        templateTagRelDTO.setStatus(LiveConst.STATUS_TAG_REL_NORMAL);
        templateTagRelDTO.setCreateTime(millis2Timestamp(Utils.getCurrentMillis()));
        LiveTagRepo.LOGGER.info("增加标签模版关联关系为: {}", templateTagRelDTO);
        TemplateTagRelPO templateTagRelPO = templateTagRelDTO.toDO();
        if (templateTagRelMapper.insert(templateTagRelPO) > 0) {
            LiveTagRepo.LOGGER.info("增加标签模版关联关系成功");
        } else {
            LiveTagRepo.LOGGER.info("增加标签模版关联关系失败");
        }
        return templateTagRelPO.getId();
    }

    /**
     * 替换标签和超级模版关系
     *
     * @param templateTagRelDTO 关系数据
     * @return 是否成功
     */
    public boolean replaceTemplateTagRel(TemplateTagRelDTO templateTagRelDTO) {
        LiveTagRepo.LOGGER.info("替换标签模版关联关系为: {}", templateTagRelDTO);

        DBTemplateRelQuery dbTemplateRelQuery = DBTemplateRelQuery.builder().tagIds(Arrays.asList(templateTagRelDTO.getTagId())).status(LiveConst.STATUS_TAG_REL_NORMAL).build();
        List<TemplateTagRelPO> templateTagRelPOS = templateTagRelMapper.list(dbTemplateRelQuery);
        if (!Utils.isEmpty(templateTagRelPOS)) {
            // 替换关系
            TemplateTagRelPO tagRel = templateTagRelPOS.get(0);
            LiveTagRepo.LOGGER.info("原有的关联关系为: " + tagRel);
            tagRel.setUrl(templateTagRelDTO.getUrl());
            return update(new TemplateTagRelDTO(tagRel));
        } else {
            // 若没有增加关系
            addTemplateTagRel(templateTagRelDTO);
            return true;
        }
    }

    /**
     * 更新标签和超级模版关系
     *
     * @param templateTagRelDTO 关联数据
     * @return 是否成功
     */
    private boolean update(TemplateTagRelDTO templateTagRelDTO) {
        templateTagRelDTO.setModifiedTime(millis2Timestamp(Utils.getCurrentMillis()));
        LiveTagRepo.LOGGER.info("更新标签模版关联关系内容为: {}", templateTagRelDTO);
        TemplateTagRelPO templateTagRelPO = templateTagRelDTO.toDO();
        boolean isSuccess = templateTagRelMapper.update(templateTagRelPO, Utils.getCurrentMillis()) > 0;
        if (isSuccess) {
            LiveTagRepo.LOGGER.info("更新关系成功");
        } else {
            LiveTagRepo.LOGGER.info("更新关系失败");
        }
        return isSuccess;
    }

    private boolean update(LiveTagDTO liveTagDTO) {
        liveTagDTO.setModifiedTime(millis2Timestamp(Utils.getCurrentMillis()));
        LiveTagRepo.LOGGER.info("更新标签模版关联关系内容为: {}", liveTagDTO);
        LiveTagPO liveTagPO = liveTagDTO.toDO();
        boolean isSuccess = pureLiveTagMapper.update(liveTagPO, Utils.getCurrentMillis()) > 0;
        if (isSuccess) {
            LiveTagRepo.LOGGER.info("更新关系成功");
        } else {
            LiveTagRepo.LOGGER.info("更新关系失败");
        }
        return isSuccess;
    }

    /**
     * 删除标签和超级模版关联关系
     *
     * @param templateTagRelDTO 关系数据
     * @return 是否成功
     */
    public boolean delete(TemplateTagRelDTO templateTagRelDTO) {
        List<TemplateTagRelPO> templateTagRelPOS = templateTagRelMapper.list(DBTemplateRelQuery.builder().tagIds(Arrays.asList(templateTagRelDTO.getTagId())).status(LiveConst.STATUS_TAG_REL_NORMAL).build());
        if (!Utils.isEmpty(templateTagRelPOS)) {
            templateTagRelDTO.setId(templateTagRelPOS.get(0).getId());
            templateTagRelDTO.setStatus(LiveConst.STATUS_TAG_REL_DEL);
            templateTagRelDTO.setModifiedTime(millis2Timestamp(Utils.getCurrentMillis()));
            LiveTagRepo.LOGGER.info("删除标签模版内容为:{} ", templateTagRelDTO);
            return update(templateTagRelDTO);
        } else {
            return true;
        }
    }

    /**
     * 根据标签ID获取超级模版关联关系
     *
     * @param tagId 标签ID
     * @return 关联关系
     */
    public TemplateTagRelDTO getTemplateTagRel(Long tagId) {
        DBTemplateRelQuery dbTemplateRelQuery = DBTemplateRelQuery.builder().tagIds(Arrays.asList(tagId)).status(LiveConst.STATUS_TAG_REL_NORMAL).build();
        List<TemplateTagRelPO> templateTagRelPOS = templateTagRelMapper.list(dbTemplateRelQuery);
        if (!Utils.isEmpty(templateTagRelPOS)) {
            return new TemplateTagRelDTO(templateTagRelPOS.get(0));
        } else {
            return null;
        }
    }

    /**
     * 列出标签关联超级模版关系
     *
     * @param dbTemplateRelQuery 检索条件
     * @return 关联关系列表
     */
    private List<TemplateTagRelDTO> list(DBTemplateRelQuery dbTemplateRelQuery) {
        List<TemplateTagRelPO> templateTagRelPOS = templateTagRelMapper.list(dbTemplateRelQuery);
        return templateTagRelPOS.stream().map(TemplateTagRelDTO::new).collect(Collectors.toList());
    }

    private Map<Long, TemplateTagRelDTO> map(DBTemplateRelQuery dbTemplateRelQuery) {
        List<TemplateTagRelPO> templateTagRelPOS = templateTagRelMapper.list(dbTemplateRelQuery);
        return templateTagRelPOS.stream().map(TemplateTagRelDTO::new).collect(Collectors.toMap(TemplateTagRelDTO::getTagId, v1 -> v1, (v1, v2) -> v1));
    }

    /**
     * 根据tagId获取其所有有效关联的超级模版
     *
     * @param tagId 标签ID
     * @return 超级模版
     */
    public Map<String, String> listTemplatesById(Long tagId) {
        Map<String, String> reMap = new HashMap<>();
        DBTemplateRelQuery dbTemplateRelQuery = DBTemplateRelQuery.builder().tagIds(Arrays.asList(tagId)).build();
        List<TemplateTagRelDTO> tagRels = list(dbTemplateRelQuery);
        if (!Utils.isEmpty(tagRels)) {
            reMap.put("url", tagRels.get(0).getUrl());
        }
        return reMap;
    }

    /**
     * 根据纯直播ID获取关联关系
     *
     * @param lid 纯直播ID
     * @return 关联关系列表
     */
    public List<LiveTagRelDTO> listTagRelsByLiveId(Long lid) {
        DBLiveTagRelQuery dbLiveTagRelQuery = DBLiveTagRelQuery.builder().liveIds(Arrays.asList(lid)).status(LiveConst.STATUS_TAG_NORMAL).build();
        return list(dbLiveTagRelQuery);
    }

    /**
     * 根据type获取热门tag
     *
     * @param type tag类型
     * @return tag信息
     */
    List<LiveTagRelDTO> getTagsByType(Integer type) {
        DBLiveTagRelQuery dbLiveTagRelQuery = DBLiveTagRelQuery.builder().type(type).status(LiveConst.STATUS_TAG_NORMAL).orderBy("weight desc").build();
        List<LiveTagRelDTO> liveTagRelDTOS = list(dbLiveTagRelQuery);
        List<Long> tagIds = liveTagRelDTOS.stream().map(LiveTagRelDTO::getId).collect(Collectors.toList());
        DBTemplateRelQuery dbTemplateRelQuery = DBTemplateRelQuery.builder().tagIds(tagIds).status(LiveConst.STATUS_TAG_REL_NORMAL).build();
        List<TemplateTagRelDTO> templateTagRelDTOS = list(dbTemplateRelQuery);
        List<Long> removeTagIds = templateTagRelDTOS.stream().map(TemplateTagRelDTO::getTagId).collect(Collectors.toList());
        return liveTagRelDTOS.stream().filter(liveTagRelDTO -> !removeTagIds.contains(liveTagRelDTO.getId())).collect(Collectors.toList());
    }


    public boolean delete(Long tagId) {
        LiveTagDTO liveTagDTO = getTagById(tagId);
        liveTagDTO.setStatus(LiveConst.STATUS_TAG_DEL);
        return update(liveTagDTO);
    }
}
