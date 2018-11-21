package cn.idongjia.live.restructure.query;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.gem.lib.pojo.ItemPext;
import cn.idongjia.live.db.mybatis.query.DBLiveBookQuery;
import cn.idongjia.live.db.mybatis.query.DBLivePureQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveResourceQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveRoomQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveShowQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveTagQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveTagRelQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveVideoCoverQuery;
import cn.idongjia.live.db.mybatis.query.DBPlayBackQuery;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.purelive.PureLive;
import cn.idongjia.live.pojo.purelive.PureLive4Article;
import cn.idongjia.live.pojo.purelive.PureLiveDO;
import cn.idongjia.live.query.purelive.PureLiveSearch;
import cn.idongjia.live.restructure.convert.ConvertorI;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.domain.entity.zoo.LiveZoo;
import cn.idongjia.live.restructure.domain.entity.zoo.ZooCount;
import cn.idongjia.live.restructure.dto.ItemPExtDTO;
import cn.idongjia.live.restructure.dto.LiveBookDTO;
import cn.idongjia.live.restructure.dto.LivePullUrlDTO;
import cn.idongjia.live.restructure.dto.LivePureDTO;
import cn.idongjia.live.restructure.dto.LiveResourceDTO;
import cn.idongjia.live.restructure.dto.LiveRoomDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.dto.LiveTagDTO;
import cn.idongjia.live.restructure.dto.LiveTagRelDTO;
import cn.idongjia.live.restructure.dto.PlayBackDTO;
import cn.idongjia.live.restructure.dto.PureLive4ArticleDTO;
import cn.idongjia.live.restructure.dto.PureLiveDTO;
import cn.idongjia.live.restructure.dto.VideoCoverDTO;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.restructure.manager.TemplateManager;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.restructure.manager.ZooManager;
import cn.idongjia.live.restructure.repo.LivePureRepo;
import cn.idongjia.live.restructure.repo.LiveResourceRepo;
import cn.idongjia.live.restructure.v2.support.enumeration.LiveResourceType;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.user.lib.pojo.vo.CustomerVo;
import cn.idongjia.util.Utils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/6/11.
 */
@Component
public class LivePureQueryHandler {


    private static final Log logger = LogFactory.getLog(LivePureQueryHandler.class);
    @Resource
    private LivePureRepo livePureRepo;

    @Resource
    private LiveShowQueryHandler liveShowQueryHandler;

    @Resource
    private LiveRoomQueryHandler liveRoomQueryHandler;

    @Resource
    private VideoCoverHandler videoCoverHandler;

    @Resource
    private LiveBookQueryHandler liveBookQueryHandler;
    @Resource
    private ZooManager           zooManager;

    @Resource
    private LiveResourceQueryHandler liveResourceQueryHandler;

    @Resource
    private LiveTagQueryHandler liveTagQueryHandler;

    @Resource
    private LiveZooQueryHandler liveZooQueryHandler;
    @Resource
    private ConfigManager       configManager;

    @Resource
    private UserManager userManager;

    @Resource
    private PlayBackQueryHandler playBackQueryHandler;

    @Resource
    private ItemQueryHandler itemQueryHandler;

    @Resource
    private LiveResourceRepo liveResourceRepo;


    @Resource
    private TemplateManager templateManager;

    @Resource
    private ConvertorI<PureLive, LiveEntity, PureLiveDTO> pureLiveConvert;


    @Resource
    private ConvertorI<PureLive4Article, LiveEntity, PureLive4ArticleDTO> pureLive4ArticleConvertor;

    @Async
    public Future<List<LivePureDTO>> list(DBLivePureQuery dbLivePureQuery) {
        List<LivePureDTO> livePureDTOS = livePureRepo.list(dbLivePureQuery);
        return new AsyncResult(livePureDTOS);
    }

    @Async
    public Future<Map<Long, LivePureDTO>> map(DBLivePureQuery dbLivePureQuery) {
        List<LivePureDTO>      livePureDTOS   = livePureRepo.list(dbLivePureQuery);
        Map<Long, LivePureDTO> livePureDTOMap = livePureDTOS.stream().collect(Collectors.toMap(LivePureDTO::getId, v1 -> v1, (v1, v2) -> v1));
        return new AsyncResult(livePureDTOMap);
    }


    public LivePureDTO getById(Long id) {
        return livePureRepo.getByLiveId(id);
    }

    public Integer countOpeningPureLiveByAnchor(Long anchorId) {

        return livePureRepo.countOpeningPureLiveByAnchor(anchorId);

    }

    /**
     * 根据主播ID获取其回放lid
     *
     * @param anchorId 主播ID
     * @return 回放数量
     */
    public List<Long> listOverPureLiveByAnchor(Long anchorId) {
        return livePureRepo.listOverPureLiveByAnchor(anchorId);
    }


    public PureLiveDO getPureLiveDO(Long liveId) {
        LivePureDTO livePureDTO = getById(liveId);
        return livePureDTO.assemblePureLiveDO();
    }

    public PureLive getPureLive(Long pureLiveId, Long uid) {
        DBLiveShowQuery dbLiveShowQuery = DBLiveShowQuery.builder().status(Arrays.asList(LiveConst.STATUS_LIVE_NORMAL)).ids(Arrays.asList(pureLiveId)).build();
        try {
            List<LiveShowDTO> liveShowDTOS = liveShowQueryHandler.list(dbLiveShowQuery).get();
            if (Utils.isEmpty(liveShowDTOS)) {
                return null;
            }
            List<PureLive> pureLives = assemblePureLive(liveShowDTOS, uid, true, false);
            PureLive       pureLive  = pureLives.get(0);
            Long           zid       = pureLive.getZid();
            if (pureLive.getState().equals(LiveConst.STATE_LIVE_NOT_BEGIN)) {
//                pureLive.setStarttime(pureLive.getPrestarttm());
                pureLive.setStarttm(pureLive.getPrestarttm());
                Map<Long, ZooCount> zooCountMap = zooManager.countZoos(Arrays.asList(zid));
                ZooCount            zooCount    = zooCountMap.get(zid);
                pureLive.setUserCount(zooCount.getReal());
            } else {
                Integer zooRoomUserCount = zooManager.getZooRoomUserCount(zid);
                pureLive.setUserCount(zooRoomUserCount);
            }
            return pureLive;
        } catch (Exception e) {
            logger.error("查询失败{}", e);
            throw LiveException.failure("查询失败");
        }
    }

    public List<PureLive> assemblePureLive(List<LiveShowDTO> liveShowDTOS, Long uid, boolean needTemplate, boolean real) {
        if (Utils.isEmpty(liveShowDTOS)) {
            return new ArrayList<>();
        }
        List<Long> liveIds         = liveShowDTOS.stream().map(LiveShowDTO::getId).collect(Collectors.toList());
        List<Long> playbackLiveIds = liveShowDTOS.stream().filter(liveShowDTO -> liveShowDTO.getType().intValue() != LiveEnum.LiveType.LIVE_AUCTION.getCode()).map(LiveShowDTO::getId).collect(Collectors.toList());
        List<Long> roomIds         = liveShowDTOS.stream().map(LiveShowDTO::getRoomId).collect(Collectors.toList());
        List<Long> zooIds          = liveShowDTOS.stream().map(LiveShowDTO::getZooId).collect(Collectors.toList());
        List<Long> uids            = liveShowDTOS.stream().map(LiveShowDTO::getUserId).collect(Collectors.toList());
        try {
            Future<Map<Long, LivePureDTO>>           livePureDTOMapFuture  = map(DBLivePureQuery.builder().liveIds(liveIds).build());
            Future<Map<Long, LiveRoomDTO>>           liveRoomDTOMapFuture  = liveRoomQueryHandler.map(DBLiveRoomQuery.builder().ids(roomIds).build());
            Map<Long, VideoCoverDTO>                 videoCoverDTOMap      = videoCoverHandler.map(DBLiveVideoCoverQuery.builder().liveIds(liveIds).build());
            Future<Map<Long, List<LiveResourceDTO>>> liveResourceMapFuture = liveResourceQueryHandler.map(DBLiveResourceQuery.builder().status(0).liveIds(liveIds).build());
            List<LiveTagRelDTO> liveTagRelDTOS = liveTagQueryHandler.list(DBLiveTagRelQuery.builder().liveIds(liveIds).status(LiveConst.STATUS_TAG_REL_NORMAL).build())
                    .get();
            List<Long>            tagIds        = liveTagRelDTOS.stream().map(LiveTagRelDTO::getTagId).collect(Collectors.toList());
            Map<Long, LiveTagDTO> liveTagDTOMap = null;
            if (Utils.isEmpty(tagIds)) {
                liveTagDTOMap = liveTagQueryHandler.map(DBLiveTagQuery.builder().tagIds(tagIds).status(LiveConst.STATUS_TAG_NORMAL).build()).get();
            } else {
                liveTagDTOMap = new HashMap<>();
            }


            Map<Long, CustomerVo> customerVoMap = userManager.takeBatchCustomer(uids);

            Future<Map<Long, LiveZoo>>           liveZooMapFuture  = liveZooQueryHandler.map(zooIds);
            String                               shareDescTemplate = configManager.getShareDescTemplate();
            Long                                 leastDuration     = configManager.getLeastDuration();
            Map<Long, LivePureDTO>               livePureDTOMap    = livePureDTOMapFuture.get();
            Map<Long, LiveRoomDTO>               liveRoomDTOMap    = liveRoomDTOMapFuture.get();
            Future<Map<Long, LivePullUrlDTO>>    pullUrlMapFuture  = liveRoomQueryHandler.pullUrlMap(liveRoomDTOMap.values().stream().collect(Collectors.toList()));
            Future<Map<Long, List<PlayBackDTO>>> playBackDTOFuture = playBackQueryHandler.map(DBPlayBackQuery.builder().liveIds(playbackLiveIds).status(0).build());
            List<LiveBookDTO>                    liveBookDTOS      = null;

            if (uid != null) {
                Future<List<LiveBookDTO>> liveBookFuture = liveBookQueryHandler.list(DBLiveBookQuery.builder().status(LiveConst.STATUS_BOOK_NORMAL).userId(uid).build());
                liveBookDTOS = liveBookFuture.get();
            } else {
                liveBookDTOS = new ArrayList<>();
            }
            Map<Long, LiveZoo>               liveZooMap         = liveZooMapFuture.get();
            Map<Long, LivePullUrlDTO>        livePullUrlDTOMap  = pullUrlMapFuture.get();
            Map<Long, List<LiveResourceDTO>> liveResourceDTOMap = liveResourceMapFuture.get();
            // key->直播id ,v->资源id
            Map<Long, Long>       liveResourceMap = new HashMap<>();
            List<LiveResourceDTO> liveResources   = new ArrayList<>();
            liveResourceDTOMap.values().stream().forEach(liveResourceDTOS -> {
                List<LiveResourceDTO> resourceDTOS = liveResourceDTOS.stream()
                        .filter(liveResourceDTO -> liveResourceDTO.getResourceType().intValue() == LiveResourceType.ITEM.getCode())
                        .collect(Collectors.toList());
                liveResources.addAll(resourceDTOS);
            });
            Map<Long, ItemPExtDTO> itemPExtDTOMap = null;
            if (!Utils.isEmpty(liveResources)) {
                List<Long>     itemIds   = liveResources.stream().map(LiveResourceDTO::getResourceId).collect(Collectors.toList());
                List<ItemPext> itemPexts = itemQueryHandler.list(itemIds);
                if (!Utils.isEmpty(itemPexts)) {
                    itemPExtDTOMap = itemPexts.stream().collect(Collectors.toMap(ItemPext::getIid, v1 -> new ItemPExtDTO(v1), (v1, v2) -> v1));
                }
            }
            if (itemPExtDTOMap == null) {
                itemPExtDTOMap = new HashMap<>();
            }
            liveIds.stream().forEach(liveId -> {
                Long mainResource = liveResourceRepo.getMainResource(liveId, LiveResourceType.ITEM.getCode());
                liveResourceMap.put(liveId, mainResource);
            });
            List<Long>                   bookedLiveIds       = liveBookDTOS.stream().map(LiveBookDTO::getLiveId).collect(Collectors.toList());
            Map<Long, List<PlayBackDTO>> playBackDTOMap      = playBackDTOFuture.get();
            Map<Long, LiveTagDTO>        finalLiveTagDTOMap  = liveTagDTOMap;
            Map<Long, ItemPExtDTO>       finalItemPExtDTOMap = itemPExtDTOMap;
            return liveShowDTOS.stream().map(liveShowDTO -> {
                Long                  liveId           = liveShowDTO.getId();
                Long                  roomId           = liveShowDTO.getRoomId();
                Long                  zid              = liveShowDTO.getZooId();
                Long                  userId           = liveShowDTO.getUserId();
                Long                  mainResourceId   = liveResourceMap.get(liveId);
                ItemPExtDTO           itemPExtDTO      = finalItemPExtDTOMap.get(mainResourceId);
                Long                  templateId       = null;
                List<LiveResourceDTO> liveResourceDTOS = liveResourceDTOMap.get(liveId);
                if (!CollectionUtils.isEmpty(liveResourceDTOS)) {
                    for (LiveResourceDTO liveResourceDTO : liveResourceDTOS) {
                        if (liveResourceDTO.getResourceType().equals(LiveConst.TYPE_DETAIL_TEMPLATE)) {
                            templateId = liveResourceDTO.getResourceId();
                            break;
                        }
                    }
                }
                String detail = null;
                if (templateId != null && needTemplate) {
                    detail = templateManager.acquireTemplate(templateId);
                }
                PureLiveDTO pureLiveDTO = new PureLiveDTO();
                pureLiveDTO.setLiveShowDTO(liveShowDTO);
                pureLiveDTO.setShareDesc(shareDescTemplate);
                pureLiveDTO.setMinDuration(leastDuration);
                pureLiveDTO.setDefaultTemplateId(configManager.getDefaultTemplateId());
                pureLiveDTO.setDetail(detail);
                LivePureDTO livePureDTO = livePureDTOMap.get(liveId);
                pureLiveDTO.setLivePureDTO(livePureDTO);
                LiveRoomDTO liveRoomDTO = liveRoomDTOMap.get(roomId);
                pureLiveDTO.setLiveRoomDTO(liveRoomDTO);

                VideoCoverDTO videoCoverDTO = videoCoverDTOMap.get(liveId);
                pureLiveDTO.setVideoCoverDTO(videoCoverDTO);
                LiveZoo liveZoo = liveZooMap.get(zid);
                pureLiveDTO.setLiveZoo(liveZoo);

                LivePullUrlDTO livePullUrlDTO = livePullUrlDTOMap.get(roomId);
                pureLiveDTO.setLivePullUrlDTO(livePullUrlDTO);
                CustomerVo customerVo = customerVoMap.get(userId);
                pureLiveDTO.setCustomerVo(customerVo);
                pureLiveDTO.setLiveResourceDTOS(liveResourceDTOS);
                LiveTagDTO pureLiveTagRelDTO = finalLiveTagDTOMap.get(liveId);
                pureLiveDTO.setPureLiveTagRelDTO(pureLiveTagRelDTO);
                pureLiveDTO.setMainItem(itemPExtDTO);
                List<PlayBackDTO> playBackDTOS = playBackDTOMap.get(liveId);
                pureLiveDTO.setPlayBackDTOS(playBackDTOS);
                boolean isBook = bookedLiveIds.contains(liveId);
                pureLiveDTO.setBook(isBook);
                String h5Prefix = configManager.getH5Prefix();
                pureLiveDTO.setH5Prefix(h5Prefix);
                String h5Suffix = configManager.getH5Suffix();
                pureLiveDTO.setH5Suffix(h5Suffix);
                pureLiveDTO.setReal(real);
                return pureLiveConvert.dataToClient(pureLiveDTO);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("查询失败{}", e);
            throw LiveException.failure("查询失败");
        }

    }

    public BaseList<PureLive> page(PureLiveSearch pureLiveSearch) {
        Integer status = pureLiveSearch.getStatus();

        DBLiveShowQuery dbLiveShowQuery = QueryFactory.getInstance(pureLiveSearch);
        dbLiveShowQuery.setStatus(Arrays.asList(LiveConst.STATUS_LIVE_NORMAL));
        dbLiveShowQuery.setTypes(Arrays.asList(LiveConst.TYPE_LIVE_NORMAL));
        if (status != null) {
            switch (status) {
                case 1:
                    dbLiveShowQuery.setOnline(LiveConst.STATUS_LIVE_ONLINE);
                    break;
                case 0:
                    dbLiveShowQuery.setOnline(LiveConst.STATUS_LIVE_OFFLINE);
                    break;
                case -1:
                    dbLiveShowQuery.setStatus(Arrays.asList(LiveConst.STATUS_DETAIL_DEL));
                default:
                    break;
            }
        }
        Integer            count    = liveShowQueryHandler.count(dbLiveShowQuery);
        BaseList<PureLive> baseList = new BaseList<>();
        baseList.setCount(count);
        if (count > 0) {
            try {
                List<LiveShowDTO> liveShowDTOS = liveShowQueryHandler.list(dbLiveShowQuery).get();
                List<PureLive>    pureLives    = assemblePureLive(liveShowDTOS, null, false, true);
                List<PureLive> pureLiveList = pureLives.stream().map(pureLive -> {
                    pureLive.setStarttm(pureLive.getPrestarttm());
                    pureLive.setStarttime(pureLive.getPrestarttm());
                    return pureLive;
                }).collect(Collectors.toList());
                baseList.setItems(pureLiveList);
            } catch (Exception e) {
                logger.error("查询失败{}", e);
                throw LiveException.failure("查询失败");
            }
        }
        return baseList;
    }

    public BaseList<PureLive> page(Long uid, PureLiveSearch pureLiveSearch) {
        pureLiveSearch.setUid(uid);
        return page(pureLiveSearch);
    }

    public Integer countCraftsPureLives(Long uid, PureLiveSearch pureLiveSearch) {
        pureLiveSearch.setUid(uid);
        DBLiveShowQuery dbLiveShowQuery = QueryFactory.getInstance(pureLiveSearch);
        return liveShowQueryHandler.count(dbLiveShowQuery);
    }

    public List<PureLive> list(PureLiveSearch pureLiveSearch) {
        DBLiveShowQuery dbLiveShowQuery = QueryFactory.getInstance(pureLiveSearch);

        List<LiveShowDTO> liveShowDTOS = null;
        try {
            liveShowDTOS = liveShowQueryHandler.list(dbLiveShowQuery).get();
            return assemblePureLive(liveShowDTOS, null, false, true);
        } catch (Exception e) {
            logger.error("查询失败{}", e);
            throw LiveException.failure("查询失败");
        }
    }

    public Integer count(PureLiveSearch pureLiveSearch) {
        DBLiveShowQuery dbLiveShowQuery = QueryFactory.getInstance(pureLiveSearch);
        return liveShowQueryHandler.count(dbLiveShowQuery);
    }

    public PureLive4Article get4Article(Long liveId) {
        LiveShowDTO         liveShowDTO         = liveShowQueryHandler.getById(liveId);
        LivePureDTO         livePureDTO         = getById(liveId);
        User                user                = userManager.getUser(liveShowDTO.getUserId());
        PureLive4ArticleDTO pureLive4ArticleDTO = new PureLive4ArticleDTO();
        pureLive4ArticleDTO.setLivePureDTO(livePureDTO);
        pureLive4ArticleDTO.setLiveShowDTO(liveShowDTO);
        pureLive4ArticleDTO.setUser(user);
        return pureLive4ArticleConvertor.dataToClient(pureLive4ArticleDTO);
    }
}
