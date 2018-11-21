package cn.idongjia.live.restructure.biz;


import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.divine.lib.pojo.request.live.LiveQry;
import cn.idongjia.divine.lib.pojo.request.sort.SortType;
import cn.idongjia.divine.lib.pojo.response.live.general.GeneralLiveCO;
import cn.idongjia.gem.lib.pojo.ItemPext;
import cn.idongjia.live.api.live.pojo.LiveApiResp;
import cn.idongjia.live.db.mybatis.po.DLiveCloudPO;
import cn.idongjia.live.db.mybatis.po.LiveAuctionSessionPO;
import cn.idongjia.live.db.mybatis.po.LiveBookPO;
import cn.idongjia.live.db.mybatis.po.LiveShowPO;
import cn.idongjia.live.db.mybatis.po.VideoCoverPO;
import cn.idongjia.live.db.mybatis.query.DBAnchorBookQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveBookQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveResourceQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveShowQuery;
import cn.idongjia.live.db.mybatis.query.DBPlayBackQuery;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.live.LiveListCO;
import cn.idongjia.live.pojo.live.LivePre;
import cn.idongjia.live.pojo.live.LivePreDateGroup;
import cn.idongjia.live.pojo.live.LivePreTimeGroup;
import cn.idongjia.live.pojo.live.LivePullUrl;
import cn.idongjia.live.pojo.live.LiveResponseApi;
import cn.idongjia.live.pojo.live.LiveShow;
import cn.idongjia.live.pojo.purelive.PureLive;
import cn.idongjia.live.pojo.purelive.PureLiveDetailDO;
import cn.idongjia.live.query.live.LiveListApiSearch;
import cn.idongjia.live.query.live.LivePreSearch;
import cn.idongjia.live.restructure.cache.LiveAuctionSessionCache;
import cn.idongjia.live.restructure.cache.LiveStartPushCahche;
import cn.idongjia.live.restructure.cloud.dcloud.DCloudRepo;
import cn.idongjia.live.restructure.convert.ConvertorI;
import cn.idongjia.live.restructure.convert.LiveListCOConvertor;
import cn.idongjia.live.restructure.convert.LivePrelistConvertor;
import cn.idongjia.live.restructure.convert.PureLiveConvertor;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.domain.entity.live.LivePureBook;
import cn.idongjia.live.restructure.domain.entity.live.LiveShowResource;
import cn.idongjia.live.restructure.domain.manager.LiveEntityManager;
import cn.idongjia.live.restructure.dto.AnchorBookDTO;
import cn.idongjia.live.restructure.dto.DLiveCloudDTO;
import cn.idongjia.live.restructure.dto.ItemPExtDTO;
import cn.idongjia.live.restructure.dto.LiveBookDTO;
import cn.idongjia.live.restructure.dto.LiveListDTO;
import cn.idongjia.live.restructure.dto.LivePullUrlDTO;
import cn.idongjia.live.restructure.dto.LivePureDTO;
import cn.idongjia.live.restructure.dto.LiveResourceDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.dto.PlayBackDTO;
import cn.idongjia.live.restructure.dto.PureLiveDTO;
import cn.idongjia.live.restructure.enums.AnchorBlackWhiteEnum;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.factory.CraftLiveEntityFactory;
import cn.idongjia.live.restructure.factory.LiveAbstractFactory;
import cn.idongjia.live.restructure.factory.PureLiveEntityFactory;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.restructure.manager.DivineSearchManager;
import cn.idongjia.live.restructure.manager.GemManager;
import cn.idongjia.live.restructure.manager.ItemOperationPushManager;
import cn.idongjia.live.restructure.manager.MqProducerManager;
import cn.idongjia.live.restructure.manager.NotifyManager;
import cn.idongjia.live.restructure.manager.OutcryManager;
import cn.idongjia.live.restructure.manager.RedisManager;
import cn.idongjia.live.restructure.manager.SearchManager;
import cn.idongjia.live.restructure.manager.TaskManager;
import cn.idongjia.live.restructure.manager.TemplateManager;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.restructure.manager.ZooManager;
import cn.idongjia.live.restructure.pojo.co.live.LiveDetailForApiCO;
import cn.idongjia.live.restructure.pojo.co.tab.LiveCO;
import cn.idongjia.live.restructure.query.AnchorBookQueryHandler;
import cn.idongjia.live.restructure.query.LiveBookQueryHandler;
import cn.idongjia.live.restructure.query.LivePureQueryHandler;
import cn.idongjia.live.restructure.query.LiveResourceQueryHandler;
import cn.idongjia.live.restructure.query.LiveRoomQueryHandler;
import cn.idongjia.live.restructure.query.LiveShowQueryHandler;
import cn.idongjia.live.restructure.query.PlayBackQueryHandler;
import cn.idongjia.live.restructure.query.SessionQueryHandler;
import cn.idongjia.live.restructure.repo.HotAnchorsRepo;
import cn.idongjia.live.restructure.repo.LiveResourceRepo;
import cn.idongjia.live.restructure.repo.LiveShowRepo;
import cn.idongjia.live.restructure.repo.VideoCoverRepo;
import cn.idongjia.live.restructure.v2.support.enumeration.LiveResourceType;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.DateTimeUtil;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.live.v2.pojo.CraftsLive;
import cn.idongjia.live.v2.pojo.LiveResource;
import cn.idongjia.log.Log;
import cn.idongjia.mq.message.body.PureLiveMessage;
import cn.idongjia.mq.topic.PureLiveTopic;
import cn.idongjia.outcry.pojo.Session4Live;
import cn.idongjia.se.lib.engine.query.Direction;
import cn.idongjia.user.lib.pojo.vo.CustomerVo;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static cn.idongjia.log.LogFactory.getLog;
import static cn.idongjia.util.Utils.isEmpty;

/**
 * 直播业务
 *
 * @author lc
 * @create at 2018/6/8.
 */
@Component
public class LiveShowBO {

    private static final String CHANNEL_ID    = "channel_id";
    private static final String VIDEO_URL     = "video_url";
    private static final String START_TIME    = "start_time";
    private static final String END_TIME      = "end_time";
    private static final int    LIVE_PUSH_ON  = 1;
    private static final int    LIVE_PUSH_OFF = 0;
    private static final Log    logger        = getLog(LiveShowBO.class);
    @Resource
    private HotAnchorsRepo       hotAnchorsRepo;
    @Resource
    private ZooManager           zooManager;
    @Resource
    private LiveEntityManager    liveEntityManager;
    @Resource
    private LiveShowQueryHandler liveShowQueryHandler;

    @Resource
    private LiveShowRepo             liveShowRepo;
    @Resource
    private TemplateManager          templateManager;
    @Resource
    private LiveResourceRepo         liveResourceRepo;
    @Resource
    private LiveResourceQueryHandler liveResourceQueryHandler;
    @Resource
    private UserManager              userManager;
    @Resource
    private ConfigManager            configManager;
    @Resource
    private SessionQueryHandler      sessionQueryHandler;
    @Resource
    private PlayBackQueryHandler     playBackQueryHandler;

    @Resource
    private VideoCoverRepo videoCoverRepo;
    @Resource
    private OutcryManager  outcryManager;

    @Resource
    private GemManager           gemManager;
    @Resource
    private LiveBookQueryHandler liveBookQueryHandler;


    @Resource
    private MqProducerManager mqProducerManager;

    @Resource
    private RedisManager             redisManager;
    @Resource
    private ItemOperationPushManager itemOperationPushManager;

    @Resource
    private NotifyManager notifyManager;
    @Resource
    private DCloudRepo    dCloudRepo;

    @Resource
    private AnchorBookQueryHandler anchorBookQueryHandler;

    @Resource
    private PageTabBO pageTabBO;


    @Resource
    private ConvertorI<LiveListCO, LiveEntity, LiveListDTO> liveListConvert;


    @Resource
    private ConvertorI<PureLiveDetailDO, LiveShowResource, LiveResourceDTO> liveResourceConvertor;


    @Resource
    private LiveAuctionSessionCache liveAuctionSessionCache;

    @Resource
    private DivineSearchManager divineSearchManager;

    @Resource
    private LiveRoomQueryHandler liveRoomQueryHandler;

    @Resource
    private LivePureQueryHandler livePureQueryHandler;

    @Resource
    private TaskManager         taskManager;
    @Resource
    private LiveStartPushCahche liveStartPushCahche;

    public void start(Long liveId, Boolean isValidate) {
        LiveEntity liveEntity = liveEntityManager.load(liveId);
        liveEntity.start(isValidate);
    }

    public void stop(Long liveId) {
        LiveEntity liveEntity = liveEntityManager.load(liveId);
        liveEntity.stop();
    }

    public void resume(Long liveId) {
        LiveEntity liveEntity = liveEntityManager.load(liveId);
        liveEntity.resume();
    }


    /**
     * 创建直播  提供推流端创建直播
     *
     * @param craftsLive 直播参数
     * @return 直播id
     */
    public Long createPushPureLive(CraftsLive craftsLive) {
        //验证主播是否存在
        userManager.getUser(craftsLive.getAnchorId());
        Long zid = null;
        //获取或者创建聊天室id
        if (Objects.isNull(craftsLive.getZid())) {
            zid = zooManager.addZooRoom(craftsLive.getTitle(), configManager.getZooRandomCount(), configManager.getLiveSuid());
            craftsLive.setZid(zid);
        }
        //预留推流端创建直播图文详情
        Long templateId = null;
        templateId = templateManager.addAppTemplate(craftsLive.getTemplateJsonStr(), craftsLive.getTitle());

        setTemplateReource(craftsLive, templateId);
        LiveAbstractFactory factory    = new CraftLiveEntityFactory();
        LiveEntity          liveEntity = factory.getEntity(craftsLive);

        //如果直播关联商品，需要检验商品的状态
        liveEntity.setResource(checkItems(liveEntity.getResource()));

        //获取关注该主播的所有用户
        initCreateLiveBook(liveEntity);

        liveEntity.create();

        return liveEntity.getId();

    }

    private void initCreateLiveBook(LiveEntity entity) {

        //获取关注该主播的所有用户
        DBAnchorBookQuery   query          = DBAnchorBookQuery.builder().anchorIds(Arrays.asList(entity.getHuid())).status(Arrays.asList(0)).build();
        List<AnchorBookDTO> anchorBookDTOS = new ArrayList<>();
        try {
            anchorBookDTOS = anchorBookQueryHandler.list(query).get();
        } catch (Exception e) {
            logger.warn("查询主播关系数据失败,{}", e);
        }
        if (!CollectionUtils.isEmpty(anchorBookDTOS)) {
            List<LivePureBook> livePureBooks = new ArrayList<>();
            anchorBookDTOS.forEach(anchorBookDTO -> {
                LivePureBook livePureBook = new LivePureBook();
                livePureBook.setUid(anchorBookDTO.getUserId());
                livePureBooks.add(livePureBook);
            });
            entity.setLivePureBooks(livePureBooks);
        }
    }

    public Long updatePushPurelive(CraftsLive craftsLive) {

        //获取更新前的直播数据
        LiveEntity oldEntity = liveEntityManager.load(craftsLive.getLid());


        //预留推流端创建直播图文详情
        Long templateId = null;
        //调用前端的更新接口
        LiveResource liveResource = new LiveResource();
        if (CollectionUtils.isEmpty(craftsLive.getResources())) {
            liveResource = null;
        } else {
            liveResource = craftsLive.getResources().stream()
                    .filter(resource ->
                            resource.getResourceType() == LiveConst.TYPE_DETAIL_TEMPLATE)
                    .findFirst()
                    .orElse(null);
        }
        if (Objects.isNull(liveResource)) {
            templateId = liveResourceQueryHandler.getTemplateId(craftsLive.getLid());
        } else {
            templateId = liveResource.getResourceId();
        }

        if (!Objects.isNull(templateId)) {
            templateManager.modifyAppTemplate(templateId, craftsLive.getTemplateJsonStr());
            templateId = null;
        } else {
            templateId = templateManager.addAppTemplate(craftsLive.getTemplateJsonStr(), craftsLive.getTitle());
        }

        setTemplateReource(craftsLive, templateId);

        LiveAbstractFactory factory   = new CraftLiveEntityFactory();
        LiveEntity          newEntity = factory.getEntity(craftsLive);

        oldEntity.update(newEntity);

        return oldEntity.getId();

    }

    private void setTemplateReource(CraftsLive craftsLive, Long templateId) {


        if (!Objects.isNull(templateId)) {
            LiveResource resource = new LiveResource();
            resource.setResourceId(templateId);
            resource.setResourceType(LiveConst.TYPE_DETAIL_TEMPLATE);
            List<LiveResource> resources = new ArrayList<>();
            resources.add(resource);
            if (Objects.isNull(craftsLive.getResources())) {
                craftsLive.setResources(resources);
            } else {
                craftsLive.getResources().add(resource);
            }
        }
    }

    /**
     * 更新直播  提供运营后台 和匠人后台
     *
     * @param pureLiveId 直播id
     * @param pureLive   更新的直播数据
     * @param type       1-匠人后台创建直播 2-运营后台创建纯直播
     * @return true or false
     */
    public boolean updatePureLive(Long pureLiveId, PureLive pureLive, Integer type) {

        Long       templateId = null;
        LiveEntity oldEntity  = liveEntityManager.load(pureLiveId);
        if (Objects.equals(type, LiveConst.CRAFT_LIVE_CREATE)) {

            //超级模版ID为空则重新创建，否则更新
            if (Objects.nonNull(pureLive.getDetail())) {
                //根据直播ID获取其对应的超级模版ID
                templateId = liveResourceQueryHandler.getTemplateId(pureLiveId);
                if (templateId == null) {
                    templateId = templateManager.addTemplate(pureLive.getDetail(), pureLive.getTitle());
                    PureLiveDetailDO detailDO = new PureLiveDetailDO();
                    detailDO.setResourceType(LiveConst.TYPE_DETAIL_TEMPLATE);
                    detailDO.setResourceId(templateId);

                    List<PureLiveDetailDO> details = pureLive.getDetails();
                    if (CollectionUtils.isEmpty(details)) {
                        List<PureLiveDetailDO> detailDOS = new ArrayList<>();
                        detailDOS.add(detailDO);
                        pureLive.setDetails(detailDOS);
                    } else {
                        pureLive.getDetails().add(detailDO);
                    }
                } else {
                    templateManager.modifyTemplate(templateId, pureLive.getDetail());
                }

            }

        } else {
            DBLiveResourceQuery query = DBLiveResourceQuery.builder()
                    .resourceType(LiveResourceType.ITEM.getCode())
                    .liveIds(Arrays.asList(pureLiveId))
                    .status(LiveConst.STATUS_DETAIL_NORMAL)
                    .build();
            try {
                List<LiveResourceDTO>  list    = liveResourceQueryHandler.list(query).get();
                List<PureLiveDetailDO> details = pureLive.getDetails();
                details.addAll(list.stream().map(dto -> {
                    return liveResourceConvertor.dataToClient(dto);
                }).collect(Collectors.toList()));
                pureLive.setDetails(details);
            } catch (Exception e) {
                logger.warn("查询直播资源失败{}", e);
                throw LiveException.failure("查询直播资源失败");
            }

        }
        LiveAbstractFactory factory = new PureLiveEntityFactory();
        LiveEntity          entity  = factory.getEntity(pureLive);
        oldEntity.update(entity);

        if (Objects.isNull(pureLive.getMuid())) {
            pureLive.setMuid(configManager.getLiveSuid());
        }
        zooManager.modifyZooRoom(oldEntity.getZid(), pureLive.getTitle(), pureLive.getZrc(), pureLive.getMuid());

        return true;

    }


    /**
     * 删除直播
     *
     * @param lid 直播id
     * @return true or false
     */
    public boolean delete(Long lid) {
        LiveEntity entity = liveEntityManager.load(lid);

        entity.delete();

        return true;
    }

    /**
     * 直播上下线操作
     *
     * @param liveShow 直播数据
     * @return true or false
     */
    public boolean updateLiveOnline(LiveShow liveShow) {

        LiveEntity entity = liveEntityManager.load(liveShow.getId());
        entity.updateLiveOnline(liveShow.getOnline());

        return true;
    }

    public void autoOnline(long liveId, Integer autoOnline) {
        if (Objects.isNull(autoOnline)) {
            autoOnline = BaseEnum.YesOrNo.NO.getCode();
        }
        LiveEntity entity = liveEntityManager.load(liveId);
        entity.autoOnline(autoOnline);


    }

    /**
     * 修改直播聊天室增长随机数
     *
     * @param lid 直播id
     * @param zrc 增长随机数
     * @return true or false
     * @author zhangyingjie
     */
    public boolean modifyLiveShowZrc(Long lid, Integer zrc) {
        LiveShowDTO dto = liveShowQueryHandler.getById(lid);
        //修改聊天室信息
        zooManager.modifyZooRoom(dto.getZooId(), dto.getTitle(), zrc, null);
        // 立即生效修改的随机数
        zooManager.turnZooRoomUserCountToTarget(dto.getZooId(), zrc);

        mqProducerManager.pushLiveModify(lid);
        return true;
    }

    /**
     * 修改直播通用权重
     *
     * @param lid
     * @param weight
     * @author zhangyingjie
     */
    public void modifyLiveGeneralWeight(Long lid, Integer weight) {
        LiveEntity entity = liveEntityManager.load(lid);
        entity.modifyLivemodifyGeneralWeight(weight);
    }

    /**
     * 删除直播资源
     *
     * @param liveId
     * @param resources
     */
    public void delLiveResource(Long liveId, List<PureLiveDetailDO> resources) {
        LiveEntity entity = liveEntityManager.load(liveId);
        entity.deleteResource(resourceDO2LiveShowResource(resources));

    }

    private List<LiveShowResource> resourceDO2LiveShowResource(List<PureLiveDetailDO> resources) {
        List<LiveShowResource> liveShowResources = new ArrayList<>();
        resources.forEach(resource -> {
            LiveShowResource showResource = new LiveShowResource();
            showResource.setId(resource.getId());
            showResource.setResourceType(resource.getResourceType());
            showResource.setModifiedTm(resource.getModifiedTm());
            showResource.setResourceId(resource.getResourceId());
            showResource.setStatus(resource.getStatus());
            showResource.setWeight(resource.getWeight());
            showResource.setCreateTm(resource.getCreateTm());
            liveShowResources.add(showResource);

        });
        return liveShowResources;

    }


    /**
     * 添加直播资源
     *
     * @param liveId     直播id
     * @param newDetails 资源id
     * @return
     */
    public Map<Long, String> addLiveResource(long liveId, List<PureLiveDetailDO> newDetails) {
        LiveEntity entity = liveEntityManager.load(liveId);

        return entity.addResource(resourceDO2LiveShowResource(newDetails));


    }

    /**
     * 直播资源重排序
     *
     * @param liveId
     * @param resources
     */
    public void reSortResource(Long liveId, List<PureLiveDetailDO> resources) {
        LiveEntity entity = liveEntityManager.load(liveId);
        entity.updateLiveResource(resourceDO2LiveShowResource(resources));

    }


    public Integer craftsLiveResourceManage(Long liveId, List<LiveResource> resources) {
        LiveEntity        entity = liveEntityManager.load(liveId);
        Map<Long, String> map    = entity.updateLiveResource(resources2liveShowReource(resources));
        return map.size();
    }


    private List<LiveShowResource> resources2liveShowReource(List<LiveResource> resources) {
        if (Utils.isEmpty(resources)) {
            return new ArrayList<>();
        }
        List<LiveShowResource> liveShowResources = new ArrayList<>();
        for (LiveResource r : resources) {
            LiveShowResource LiveShowResource = new LiveShowResource();
            LiveShowResource.setResourceId(r.getResourceId());
            LiveShowResource.setResourceType(r.getResourceType());
            LiveShowResource.setId(r.getId());
            liveShowResources.add(LiveShowResource);
        }
        return liveShowResources;
    }


    public boolean mainResource(long lid, long rid, int rtype, int status) {
        return liveResourceRepo.mainResource(lid, rid, rtype, status);
    }

    public Long mainResource(long lid, int rtype) {
        return liveResourceRepo.getMainResource(lid, rtype);
    }

    public void pushUserItemOperation(Long userId, Long itemId, Integer type) {
        switch (type) {
            // 加购物车
            case 1:
                itemOperationPushManager.pushCartAddedOperation(userId, itemId);
                break;
            //下单
            case 2:
                itemOperationPushManager.pushOrderAddedOperation(userId, itemId);
                break;
            // 付款
            case 3:
                itemOperationPushManager.pushPaidOperation(userId, itemId);
                break;
            default:
                break;
        }
    }

    /**
     * 根据直播ID复制聊天信息
     *
     * @param fromLid 主直播ID
     * @param toLid   to直播ID
     * @return 返回是否成功
     */
    public boolean replicateZooMessage(Long fromLid, Long toLid) {
        DBLiveShowQuery query = DBLiveShowQuery.builder()
                .ids(Arrays.asList(fromLid, toLid))
                .build();
        Long fromZid = null;
        Long toZid   = null;

        try {
            List<LiveShowPO> liveShowPOS = liveShowQueryHandler.list(query).get().stream().map(LiveShowDTO::toDO).collect(Collectors.toList());
            for (LiveShowPO po : liveShowPOS) {
                if (po.getId().equals(fromLid)) {
                    fromZid = po.getZooId();
                }
                if (po.getId().equals(toLid)) {
                    toZid = po.getZooId();
                }
            }

        } catch (Exception e) {
            logger.info("查询直播数据失败+{}", e);
            throw LiveException.failure("查询直播数据失败");
        }
        return zooManager.replicateZooMessage(fromZid, toZid);
    }


    /**
     * 检查直播关联的商品
     *
     * @param resources 关联的资源
     * @return 直播资源
     */
    private List<LiveShowResource> checkItems(List<LiveShowResource> resources) {

        if (CollectionUtils.isEmpty(resources)) {
            return null;
        }
        Set<Long> itemIdSet = resources.stream().filter(x -> x.getResourceType().equals(LiveConst.TYPE_DETAIL_ITEM)).map(LiveShowResource::getResourceId).collect(Collectors.toSet());
        //首先剔除不合理资源
        Map<Long, String> failedItemResources = judgeItemResource(itemIdSet);
        Set<Long>         failedIdSet         = failedItemResources.keySet();
        resources.removeIf(detailDO -> failedIdSet.contains(detailDO.getResourceId()));
        //补充权重消息
        int maxWeight = resources.size();
        for (LiveShowResource resource : resources) {
            resource.setWeight(maxWeight);
            maxWeight--;
        }
        return resources;
    }

    /**
     * 判断商品资源是否合理，返回不合理以及原因
     *
     * @param itemIdSet 商品id集合
     * @return 不合理
     */
    private Map<Long, String> judgeItemResource(Set<Long> itemIdSet) {
        //需要校验商品状态
        Map<Long, String> reMap = new HashMap<>(13);
        Map<Long, ItemPext> itemPextMap = gemManager.takeBatchItem(itemIdSet).stream()
                .collect(Collectors.toMap(ItemPext::getIid, i -> i, (k, v) -> v));
        for (Long itemId : itemIdSet) {
            ItemPext itemPext = itemPextMap.getOrDefault(itemId, null);
            if (Objects.isNull(itemPext)) {
                reMap.put(itemId, "商品不存在");
            } else if (itemPext.getStatus().equals(ItemPext.Status.ITEM_DELETED.get())) {
                reMap.put(itemPext.getIid(), "商品已经删除");
            } else if (itemPext.getStatus().equals(ItemPext.Status.ITEM_OFFSHELF.get())) {
                reMap.put(itemPext.getIid(), "商品已经下架");
            } else if (itemPext.getStock() == 0) {
                reMap.put(itemPext.getIid(), "商品已经售罄");
            } else if (itemPext.getSaletype().equals(3)) {
                reMap.put(itemPext.getIid(), "商品是微众筹");
            }
        }
        return reMap;
    }


    /**
     * 创建直播  提供匠人后台  运营后台创建直播
     *
     * @param pureLive 纯直播参数
     * @param type     创建直播的入口   1-匠人后台创建直播 2-运营后台创建纯直播
     * @return 直播id
     */
    public Long createPureLive(PureLive pureLive, Integer type) {

        //验证主播是否存在
        userManager.getUser(pureLive.getHuid());
        //调用前端nodes接口创建直播的超级模办
        if (Objects.nonNull(pureLive.getDetail())) {
            Long             templateId = templateManager.addTemplate(pureLive.getDetail(), pureLive.getTitle());
            PureLiveDetailDO detailDO   = new PureLiveDetailDO();
            detailDO.setResourceType(LiveConst.TYPE_DETAIL_TEMPLATE);
            detailDO.setResourceId(templateId);

            List<PureLiveDetailDO> details = pureLive.getDetails();
            if (CollectionUtils.isEmpty(details)) {
                List<PureLiveDetailDO> detailDOS = new ArrayList<>();
                detailDOS.add(detailDO);
                pureLive.setDetails(detailDOS);
            } else {
                pureLive.getDetails().add(detailDO);
            }
        }
        //初始化数据
        initCreateliveDate(pureLive, type);
        Long zid = null;
        //获取或者创建聊天室id
        if (Objects.isNull(pureLive.getZid())) {
            zid = zooManager.addZooRoom(pureLive.getTitle(), pureLive.getZrc(), pureLive.getMuid());
            pureLive.setZid(zid);
        }


        //外部数据对领域内实体的简单转换
        LiveAbstractFactory factory = new PureLiveEntityFactory();
        LiveEntity          entity  = factory.getEntity(pureLive);
        initCreateLiveBook(entity);

        entity.create();

        return entity.getId();
    }

    private void initCreateliveDate(PureLive pureLive, Integer type) {
        switch (type) {
            case LiveConst.CRAFT_LIVE_CREATE:
                //在匠人后台创建直播时设置聊天室id和聊天室随机数
                if (Objects.isNull(pureLive.getMuid())) {
                    pureLive.setMuid(configManager.getLiveSuid());
                }
                if (Objects.isNull(pureLive.getZrc())) {
                    pureLive.setZrc(configManager.getZooRandomCount());
                }
                break;
            case LiveConst.BACK_LIVE_CREATE:
                break;
            default:
                return;
        }
    }


    public String getServicePhoneNumber() {
        return configManager.getServicePhoneNumber();

    }


    public boolean resetStartTimeAndEntTime(Long liveShowId) {
        LiveEntity liveEntity = liveEntityManager.load(liveShowId);
        liveEntity.resetStartTimeAndEndTime();
        return true;
    }


    public String getLiveListShareUrl() {
        try {
            return configManager.getLiveListUrl() + "?share=0&djtitle=" + URLEncoder.encode(configManager.getLiveListShareTitle(), "UTF-8") + "&djdesc=" + URLEncoder.encode(configManager
                    .getLiveListShareDesc(), "UTF-8") + "&djpic=" + configManager.getLiveListSharePic();
        } catch (UnsupportedEncodingException e) {
            logger.error("获取直播列表分享链接失败{}", e);
        }
        return "";
    }


    /**
     * 直播开始、结束处理流程
     *
     * @param lid 纯直播ID
     */
    public void dealWithLiveStateChanged(Long lid, Integer state, int liveType) {
        if (lid != null) {
            LiveEntity entity = liveEntityManager.load(lid);
            //发送mq给首页进行扩展
            if (liveType != 2) {
                PureLiveMessage pureLive = buildPureLiveMessage(entity);
                pureLive.setAlState(state);
                mqProducerManager.pushMessageWithMessage(PureLiveTopic.PURE_LIVE_UPDATE, lid.toString()
                        , pureLive);
            }
            //处理纯直播结束状态
            if (state.equals(LiveConst.STATE_LIVE_OVER)) {
//                PureLiveBannerSearch bannerSearch = new PureLiveBannerSearch();
//                bannerSearch.setType(LiveConst.TYPE_JUMP_LIVE);
//                bannerSearch.setAddr(lid.toString());
//                List<PureLiveBannerDO> bannerList = bannerService.queryBannerList(bannerSearch);
//                if (bannerList != null && bannerList.size() != 0) {
//                    PureLiveBannerDO bannerDO = bannerList.get(0);
//                    //如果下架时间为空则设置时间
//                    if (bannerDO.getEndtm() == null) {
//                        bannerDO.setEndtm(millis2Timestamp(Utils.getCurrentMillis()));
//                        bannerService.updateBanner(bannerDO.getBid(), bannerDO);
//                    }
//                }
            } else if (state.intValue() == LiveConst.STATE_LIVE_IN_PROGRESS) {
                //给订阅的用户发送推送和tips
                if (Objects.equals(entity.getOnline().getCode(), LiveConst.STATUS_LIVE_ONLINE)) {

                    List<Long> uidBookedList = new ArrayList<>();
                    try {
                        List<LiveBookDTO> liveBookDTOS = liveBookQueryHandler.list(DBLiveBookQuery.builder().status(LiveConst.STATUS_BOOK_NORMAL).liveIds(Arrays.asList(lid)).build()).get();
                        uidBookedList = liveBookDTOS.stream().map(LiveBookDTO::toDO).map(LiveBookPO::getUserId).collect(Collectors.toList());
                    } catch (Exception e) {
                        logger.warn("查询直播订阅失败：{}", e);
                        throw LiveException.failure("查询直播订阅失败");
                    }
                    if (configManager.getLiveStartPushFollowUserOnOff() == 1) {
                        List<Long> craftsManFollowUsers = userManager.getCraftsManFollowUsers(entity.getHuid());
                        logger.info("匠人：{}，的关注人：{}", entity.getHuid(), craftsManFollowUsers);
                        if (!CollectionUtils.isEmpty(craftsManFollowUsers)) {
                            uidBookedList.addAll(craftsManFollowUsers);
                        }
                    }


                    Set<Long> uidBookedSet = new HashSet<>(uidBookedList);
                    Set<Long> uidFromRedis = liveStartPushCahche.getPushUsersFromRedis(lid);
                    uidBookedSet.removeAll(uidFromRedis);
                    List<Long> uidsToPush = new ArrayList<>(uidBookedSet);
                    liveStartPushCahche.addPushUsersToRedis(lid, uidBookedSet);


                    if (configManager.getLiveStartPushFollowUserOnOff() == 1) {
//                        int        m             = 1;
//                        long       currentMillis = Utils.getCurrentMillis() + 1000;
//                        List<Long> pushlist      = new ArrayList<>();
//                        for (int i = 0; i < uidsToPush.size(); i++) {
//                            pushlist.add(uidsToPush.get(i));
//                            if (i == (configManager.getLiveStartPushBatchCount() * m) || i == (uidsToPush.size() - 1)) {
//                                taskManager.liveStartPushTask(lid, pushlist, currentMillis += 3000);
//                                m++;
//                                pushlist.clear();
//                            }
//                        }
                        uidsToPush = userManager.getCustomerIdsByUserIds(uidsToPush);
                        if (!CollectionUtils.isEmpty(uidsToPush)) {


                            logger.info("直播开播推送uids:{}", uidsToPush);

                            liveStartPushCahche.lpushLiveStartPushUserId(lid, uidsToPush);
                            int baseRepeatCount = uidsToPush.size() / configManager.getLiveStartPushBatchCount();
                            int repeatCount     = 0;
                            if (baseRepeatCount != 0 && configManager.getLiveStartPushBatchCount() != 1) {
                                repeatCount = (uidsToPush.size() % configManager.getLiveStartPushBatchCount()) > 0 ? baseRepeatCount : 0;
                            } else if (baseRepeatCount != 0 && configManager.getLiveStartPushBatchCount() == 1) {
                                repeatCount = uidsToPush.size() - 1;
                            }
                            taskManager.liveStartPushTask(lid, repeatCount, configManager.getLiveStartPushRepeatInterval());
                        }

                    } else {
                        Long sessionId = null;
                        if (Objects.equals(LiveEnum.LiveType.LIVE_AUCTION.getCode(), entity.getLiveType().getCode())) {
                            GeneralLiveCO generalLiveCO = divineSearchManager.getById(lid);
                            sessionId = generalLiveCO.getSessionId();
                        }else{
                            mqProducerManager.pushMessage2Dynamic(lid, entity.getHuid(), entity.getTitle(),
                                    uidsToPush);
                        }
                        notifyManager.sendNotify(uidsToPush, entity.getHuid()
                                , entity.getTitle(), sessionId == null ? lid : sessionId, entity.getPic(), entity.getLiveType().getCode());
                    }


                }
            }
        }
    }

    /**
     * 根据纯直播信息构建MQ消息
     *
     * @param entity 纯直播信息
     * @return MQ消息
     */
    private PureLiveMessage buildPureLiveMessage(LiveEntity entity) {
        PureLiveMessage pureLiveMessage = new PureLiveMessage();
        pureLiveMessage.setPlid(entity.getId());
        pureLiveMessage.setCover(entity.getPic());
        pureLiveMessage.setTitle(entity.getTitle());
        pureLiveMessage.setAlState(entity.getState().getCode());
        //根据主播ID查询用户信息
        User user = userManager.getUser(entity.getHuid());
        pureLiveMessage.setzUserName(user.getUsername());
        pureLiveMessage.setzAvatar(user.getAvatar());
        //根据主播ID查询匠人信息
        pureLiveMessage.setZctf(user.getAvatar());
        pureLiveMessage.setzUid(user.getUid());
        pureLiveMessage.setCreateTm(entity.getCreateTime());
        return pureLiveMessage;
    }


    public boolean updateDLiveCloud(Long uid, LivePullUrl livePullUrl) {
        DLiveCloudPO po = new DLiveCloudPO();
        po.setRtmpUrl(livePullUrl.getRtmpUrl());
        po.setHlsUrl(livePullUrl.getHlsUrl());
        po.setFlvUrl(livePullUrl.getFlvUrl());
        po.setUserId(uid);
        return dCloudRepo.updateDLiveCloud(new DLiveCloudDTO(po));
    }


    public void updateVideoCoverLiveId() {

        DBLiveShowQuery  query = DBLiveShowQuery.builder().build();
        int              limit = 100;
        int              temp  = 0;
        List<LiveShowPO> liveShowPOS;
        try {
            do {
                query.setLimit(limit);
                query.setOffset(limit * temp);
                liveShowPOS = liveShowQueryHandler.list(query).get().stream().map(LiveShowDTO::toDO).collect(Collectors.toList());
                List<VideoCoverPO> pos = liveShow2VideoCover(liveShowPOS);
                if (!CollectionUtils.isEmpty(pos)) {
                    logger.info("更新videoCover");
                    videoCoverRepo.batchUpdateVideoCoverLiveId(pos);
                }
                temp++;
            } while (liveShowPOS != null && liveShowPOS.size() != 0 && liveShowPOS.size() == limit);

        } catch (Exception e) {
            logger.warn("查询直播数据失败+{}", e);
            throw LiveException.failure("查询直播数据失败");
        }

    }

    public List<VideoCoverPO> liveShow2VideoCover(List<LiveShowPO> liveShowPOS) {
        List<VideoCoverPO> videoCoverPOS = new ArrayList<>();
        liveShowPOS.forEach(liveShowPO -> {
            if (!Objects.isNull(liveShowPO.getVideoCoverId())) {
                VideoCoverPO po = new VideoCoverPO();
                po.setId(liveShowPO.getVideoCoverId());
                po.setLiveId(liveShowPO.getId());
                videoCoverPOS.add(po);
            }

        });
        return videoCoverPOS;
    }


    public List<LivePreDateGroup> getLivePreDataGroup(LivePreSearch search) {
        List<LivePre> livePres = getLivePreList(search);
        return assemblePreLive(livePres);
    }

    public List<LivePre> getLivePreList(LivePreSearch search) {
        //获取直播的预告列表
        if (Objects.isNull(search.getLimit())) {
            search.setLimit(10);
        }
        //由于有黑名单的限制需要从索引获取预告列表
        LiveQry qry = new LiveQry();
        qry.setShowLocations(Arrays.asList(AnchorBlackWhiteEnum.AnchorBlackWhiteType.ONLY_APP_TYPE.getCode()
                , AnchorBlackWhiteEnum.AnchorBlackWhiteType.MP_APP_TYPE.getCode()));
        qry.setOnline(LiveEnum.LiveOnline.ONLINE.getCode());
        qry.setStatus(Arrays.asList(LiveConst.STATUS_LIVE_NORMAL));
        qry.setStates(Arrays.asList(LiveEnum.LiveState.UNSTART.getCode()));
        qry.setTypes(Arrays.asList(search.getLiveType()));
        qry.setPage(search.getPage());
        qry.setLimit(search.getLimit());
        qry.setSortType(SortType.TabSortType.SELF);
        List<cn.idongjia.se.lib.engine.query.sort.Sort> sorts = new ArrayList<>();
        cn.idongjia.se.lib.engine.query.sort.Sort sort = cn.idongjia.se.lib.engine.query.sort.Sort.builder()
                .field("preStartTime")
                .direction(Direction.ASC)
                .build();
        sorts.add(sort);
        qry.setSorts(sorts);
        List<GeneralLiveCO> generalLiveCOS = divineSearchManager.liveSearch(qry);
        if (CollectionUtils.isEmpty(generalLiveCOS)) {
            return new ArrayList<>();
        }

        List<LivePre> livePres = new ArrayList<>();
        List<Long>    liveIds  = generalLiveCOS.stream().map(GeneralLiveCO::getId).collect(Collectors.toList());
        List<Long>    uids     = generalLiveCOS.stream().map(GeneralLiveCO::getUid).collect(Collectors.toList());
        List<User>    users    = userManager.listUsers(uids);
        //区分直播和直播拍
        if (Objects.equals(search.getLiveType().intValue(), LiveEnum.LiveType.LIVE_AUCTION.getCode())) {
            Map<Long, Session4Live> sessionMap = sessionQueryHandler.mapByLiveId(liveIds);
            livePres = generalLiveCOS.stream().map(generalLiveCO -> {
                User user = new User();

                for (User user1 : users) {
                    if (user1.getUid().equals(generalLiveCO.getUid())) {
                        user = user1;
                    }
                }
                Session4Live session = sessionMap.get(generalLiveCO.getId());
                Long         asid    = null;
                if (!Objects.isNull(session)) {
                    asid = session.getId();
                }
//                List<Long> bookUids   = outcryManager.bookUids(asid);
                Boolean subscribed = false;
                if (!Objects.isNull(search.getUserId())) {
                    subscribed = liveBookQueryHandler.hasBook(search.getUserId(), generalLiveCO.getId());
                }
                return LivePrelistConvertor.liveShow2LivePre(user, asid, subscribed, generalLiveCO);


            }).collect(Collectors.toList());
        } else {
            livePres = generalLiveCOS.stream().map(generalLiveCO -> {
                User user = new User();
                for (User user1 : users) {
                    if (user1.getUid().equals(generalLiveCO.getUid())) {
                        user = user1;
                    }
                }
                Boolean subscribed = false;
                if (!Objects.isNull(search.getUserId())) {
                    subscribed = liveBookQueryHandler.hasBook(search.getUserId(), generalLiveCO.getId());
                }
                return LivePrelistConvertor.liveShow2LivePre(user, null, subscribed, generalLiveCO);
            }).collect(Collectors.toList());
        }

        return livePres;
    }


    public List<LivePreDateGroup> assemblePreLive(List<LivePre> livePres) {
        List<LivePreDateGroup> dateGroupList = new ArrayList<>();
        Map<String, Map<String, List<LivePre>>> map = new TreeMap<>(Comparator
                .naturalOrder());
        for (LivePre pre : livePres) {
            Map<String, List<LivePre>> timeMap = map.get(pre.getDate());
            if (null == timeMap) {
                timeMap = new TreeMap<>();
                map.put(pre.getDate(), timeMap);
            }
            List<LivePre> lives = timeMap.get(pre.getTime());
            if (isEmpty(lives)) {
                lives = new ArrayList<>();
                timeMap.put(pre.getTime(), lives);
            }

            lives.add(pre);
        }
        if (!isEmpty(map)) {
            for (Map.Entry<String, Map<String, List<LivePre>>> entry : map.entrySet()) {
                LivePreDateGroup group = new LivePreDateGroup();
                group.setDate(DateTimeUtil.getLong2DateDetail(entry.getKey()));
                List<LivePreTimeGroup> times = new ArrayList<>();
                for (Map.Entry<String, List<LivePre>> timeEntry : entry.getValue()
                        .entrySet()) {
                    LivePreTimeGroup time = new LivePreTimeGroup();
                    time.setTime(timeEntry.getKey());
                    time.setPreLives(timeEntry.getValue());
                    times.add(time);
                }
                group.setTimes(times);
                dateGroupList.add(group);
            }
        }
        return dateGroupList;
    }


    @Deprecated
    public boolean modifyLiveAuctionVideo() {
        int                        limit = 100;
        int                        temp  = 0;
        List<LiveAuctionSessionPO> liveAuctionSessionPOS;
        do {
            liveAuctionSessionPOS = liveShowRepo.getLiveAuctionSession(limit, temp * limit);
            logger.info("获取直播拍数据==>>{}", liveAuctionSessionPOS);
            List<LiveAuctionSessionPO> liveAuctionSessionPOS1 = liveAuctionSessionCache.batchGet(liveAuctionSessionPOS);
            if (CollectionUtils.isEmpty(liveAuctionSessionPOS1)) {
                temp++;
                continue;
            }
            logger.info("获取专场缓存小视频id数据==>>{}", liveAuctionSessionPOS1);
            List<VideoCoverPO> videoCoverPOS = liveAuctionSessionPOS1.stream().map(liveAuctionSessionPO -> {
                if (Objects.nonNull(liveAuctionSessionPO.getVideoCoverId())) {
                    VideoCoverPO po = new VideoCoverPO();
                    po.setId(liveAuctionSessionPO.getVideoCoverId());
                    po.setLiveId(liveAuctionSessionPO.getLiveId());
                    return po;
                } else {
                    return null;
                }
            }).filter(x -> Objects.nonNull(x)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(videoCoverPOS)) {
                videoCoverRepo.batchUpdateVideoCoverLiveId(videoCoverPOS);
                logger.info("直播拍的小视频更新成功,更新数据==>>{}", videoCoverPOS);
            }
            temp++;
        }
        while (liveAuctionSessionPOS != null && liveAuctionSessionPOS.size() != 0 && liveAuctionSessionPOS.size() == limit);

        return true;

    }

    public List<LiveCO> getBatchH5Live(List<Long> lids, Long uid) {


        Map<Long, Integer> orderMap = new HashMap<>();
        for (int i = 0; i < lids.size(); i++) {
            orderMap.put(lids.get(i), i);
        }

        List<GeneralLiveCO> generalLiveCOS = divineSearchManager.liveList(lids);

        Collections.sort(generalLiveCOS, new Comparator<GeneralLiveCO>() {
            @Override
            public int compare(GeneralLiveCO o1, GeneralLiveCO o2) {
                return orderMap.get(o1.getId()) - orderMap.get(o2.getId());
            }
        });
        cn.idongjia.live.api.live.pojo.response.MultiResponse<LiveCO> liveCOMultiResponse = pageTabBO.assembleLive(generalLiveCOS, uid);
        List<LiveCO>                                                  liveCOS             = (List<LiveCO>) liveCOMultiResponse.getData();
        if (CollectionUtils.isEmpty(liveCOS)) {
            return new ArrayList<>();
        }

        return liveCOS;
    }

    public LiveResponseApi getLiveList(LiveListApiSearch search) {
        LiveResponseApi api = new LiveResponseApi();
        if (Objects.isNull(search.getLimit())) {
            search.setLimit(10);
        }
        LiveQry qry = new LiveQry();
        qry.setStates(Arrays.asList(LiveEnum.LiveState.PLAYING.getCode(), LiveEnum.LiveState.UNSTART.getCode()));
        qry.setHasPlayback(LiveEnum.HasPlayback.YES.getCode());
        qry.setStatus(Arrays.asList(0, 1, 2));
        qry.setOnline(LiveEnum.LiveOnline.ONLINE.getCode());
        qry.setPage(search.getPage());
        if (Objects.equals(LiveEnum.LiveType.CRAFTS_TALK_TYPE.getCode(), search.getLiveType().intValue())) {
            qry.setTypes(Arrays.asList(LiveEnum.LiveType.CRAFTS_TALK_TYPE.getCode(), LiveEnum.LiveType.ELSE_TYPE.getCode()));
        } else {
            qry.setTypes(Arrays.asList(search.getLiveType()));
        }
        qry.setLimit(search.getLimit());
        qry.setSortType(SortType.TabSortType.LIVE_DEFAULT);
        qry.setShowLocations(Arrays.asList(AnchorBlackWhiteEnum.AnchorBlackWhiteType.MP_APP_TYPE.getCode()
                , AnchorBlackWhiteEnum.AnchorBlackWhiteType.ONLY_APP_TYPE.getCode()));
        List<GeneralLiveCO> generalLiveCOS = divineSearchManager.liveSearch(qry);
        if (CollectionUtils.isEmpty(generalLiveCOS)) {
            return api;
        }
        cn.idongjia.live.api.live.pojo.response.MultiResponse<LiveCO> liveCOMultiResponse = pageTabBO.assembleLive(generalLiveCOS, search.getUserId());
        List<LiveCO>                                                  liveCOS             = (List<LiveCO>) liveCOMultiResponse.getData();
        if (CollectionUtils.isEmpty(liveCOS)) {
            return api;
        }
        List<Long> playbackLiveIds = liveCOS.stream()
                .filter(x -> !Objects.equals(x.getType(), LiveEnum.LiveType.LIVE_AUCTION.getCode()))
                .map(LiveCO::getId)
                .collect(Collectors.toList());
        //获取直播回放数据
        Map<Long, List<PlayBackDTO>> playBackMap = new HashMap<>();
        try {
            playBackMap = playBackQueryHandler.map(DBPlayBackQuery.builder().liveIds(playbackLiveIds).status(0).build()).get();

        } catch (Exception e) {
            logger.warn("查询直播回放数据失败", e);
        }
        Map<Long, List<PlayBackDTO>> finalPlayBackMap = playBackMap;
        List<LiveListCO> liveListCOS = liveCOS.stream().map(liveCO -> {
            LiveListDTO liveListDTO   = new LiveListDTO();
            Long        leastDuration = configManager.getLeastDuration();
            liveListDTO.setLeastDuration(leastDuration);
            liveListDTO.setLiveCO(liveCO);
            List<PlayBackDTO> playBackDTOS = finalPlayBackMap.get(liveCO.getId());
            liveListDTO.setPlayBackDTOS(playBackDTOS);
            return liveListConvert.dataToClient(liveListDTO);
        }).collect(Collectors.toList());
        List<LiveListCO> cos = liveListCOS.stream().filter(x -> x != null).collect(Collectors.toList());
        api.setLives(cos);

        if (Objects.equals(search.getPage().intValue(), 1)) {
            LivePreSearch search1 = new LivePreSearch();
            search1.setLiveType(search.getLiveType());
            search1.setLimit(search.getLimit());
            search1.setPage(search.getPage());
            List<LivePre> livePres = getLivePreList(search1);
            api.setPre(livePres);
        }


        return api;
    }

    public List<LiveListCO> getLivelistForConcernFeed(List<Long> liveIds) {

        List<LiveListCO> liveListCOS = new ArrayList<>();

        List<GeneralLiveCO> generalLiveCOS = divineSearchManager.liveList(liveIds);
        if (CollectionUtils.isEmpty(generalLiveCOS)) {
            return liveListCOS;
        }


        cn.idongjia.live.api.live.pojo.response.MultiResponse<LiveCO> liveCOMultiResponse = pageTabBO.assembleLive(generalLiveCOS, null);

        List<LiveCO> liveCOS = (List<LiveCO>) liveCOMultiResponse.getData();

        if (CollectionUtils.isEmpty(liveCOS)) {
            return liveListCOS;
        }

        List<Long> playbackLiveIds = liveCOS.stream()
                .filter(x -> !Objects.equals(x.getType(), LiveEnum.LiveType.LIVE_AUCTION.getCode()))
                .map(LiveCO::getId)
                .collect(Collectors.toList());
        //获取直播回放数据
        Map<Long, List<PlayBackDTO>> playBackMap = new HashMap<>();
        try {
            playBackMap = playBackQueryHandler.map(DBPlayBackQuery.builder().liveIds(playbackLiveIds).status(0).build()).get();

        } catch (Exception e) {
            logger.warn("查询直播回放数据失败", e);
        }
        Map<Long, List<PlayBackDTO>> finalPlayBackMap = playBackMap;
        liveListCOS = liveCOS.stream().map(liveCO -> {
            LiveListDTO liveListDTO   = new LiveListDTO();
            Long        leastDuration = configManager.getLeastDuration();
            liveListDTO.setLeastDuration(leastDuration);
            liveListDTO.setLiveCO(liveCO);
            List<PlayBackDTO> playBackDTOS = finalPlayBackMap.get(liveCO.getId());
            liveListDTO.setPlayBackDTOS(playBackDTOS);
            return liveListConvert.dataToClient(liveListDTO);
        }).filter(x -> x != null).collect(Collectors.toList());

        return liveListCOS;
    }


    public Long getNextLivePreStartTime(Long liveId, Long userId) {


        if (userId == null || liveId == null) {
            return null;
        }
        DBLiveShowQuery dbLiveShowQuery = DBLiveShowQuery.builder()
                .userIds(Arrays.asList(userId))
                .orderBy("prestarttm asc")
                .states(Arrays.asList(LiveEnum.LiveState.UNSTART.getCode()))
                .status(Arrays.asList(LiveConst.STATUS_LIVE_NORMAL))
                .build();

        LiveShowDTO nextLiveDTO = liveShowQueryHandler.getNextLive(dbLiveShowQuery, liveId);
        if (Objects.isNull(nextLiveDTO)) {
            return null;
        }
        return nextLiveDTO.getEstimatedStartTime();
    }


    public List<LiveApiResp> getBatchLive(List<Long> lids) {
        List<LiveApiResp> liveListCOS = new ArrayList<>();
        List<LiveCO>      batchH5Live = getBatchH5Live(lids, null);
        List<Long> playbackLiveIds = batchH5Live.stream()
                .filter(x -> !Objects.equals(x.getType(), LiveEnum.LiveType.LIVE_AUCTION.getCode()))
                .map(LiveCO::getId)
                .collect(Collectors.toList());
        //获取直播回放数据
        Map<Long, List<PlayBackDTO>> playBackMap = new HashMap<>();
        try {
            playBackMap = playBackQueryHandler.map(DBPlayBackQuery.builder().liveIds(playbackLiveIds).status(0).build()).get();

        } catch (Exception e) {
            logger.warn("查询直播回放数据失败", e);
        }
        Map<Long, List<PlayBackDTO>> finalPlayBackMap = playBackMap;
        liveListCOS = batchH5Live.stream().map(liveCO -> {
            LiveListDTO liveListDTO   = new LiveListDTO();
            Long        leastDuration = configManager.getLeastDuration();
            liveListDTO.setLeastDuration(leastDuration);
            liveListDTO.setLiveCO(liveCO);
            List<PlayBackDTO> playBackDTOS = finalPlayBackMap.get(liveCO.getId());
            liveListDTO.setPlayBackDTOS(playBackDTOS);
            return LiveListCOConvertor.assembleLiveApiResp(liveListDTO);
        }).filter(x -> x != null).collect(Collectors.toList());

        return liveListCOS;

    }

    public LiveDetailForApiCO getLiveDetailForApi(Long uid, Long lid) {
        LiveDetailForApiCO liveDetailForApiCO;

        //1. 获取直播的基本数据
        LiveShowDTO liveShowDTO       = liveShowQueryHandler.getById(lid);
        LivePureDTO livePureDTO       = livePureQueryHandler.getById(lid);
        String      shareDescTemplate = configManager.getShareDescTemplate();
        Long        leastDuration     = configManager.getLeastDuration();

        //2. 获取直播匠人数据
        List<Long> userIds = new ArrayList<>();
        userIds.add(liveShowDTO.getUserId());
        Map<Long, CustomerVo> customerVoMap = userManager.takeBatchCustomer(userIds);


        //3. 获取直播的拉流地址
        LivePullUrlDTO pullUrl = null;
        if (Objects.equals(LiveEnum.LiveState.PLAYING.getCode(), liveShowDTO.getState().intValue())) {
            pullUrl = liveRoomQueryHandler.getPullUrl(liveShowDTO.getRoomId());
        }

        //4. 获取直播的主推资源及关联的资源数量
        DBLiveResourceQuery   query            = DBLiveResourceQuery.builder().status(0).liveIds(Arrays.asList(lid)).build();
        List<LiveResourceDTO> liveResourceDTOS = null;
        try {
            liveResourceDTOS = liveResourceQueryHandler.list(query).get();

        } catch (Exception e) {
            logger.warn("获取直播关联资源失败,查询参数：{}，异常：{}", query, e);
        }
        //获取直播主推的商品id
        Long     mainResourceId = liveResourceRepo.getMainResource(lid, LiveResourceType.ITEM.getCode());
        ItemPext itemPext       = null;
        if (Objects.nonNull(mainResourceId)) {
            Optional<ItemPext> itemPext1 = gemManager.takeItem(mainResourceId);
            if (itemPext1.isPresent()) {
                itemPext = itemPext1.get();
            }
        }
        Boolean isBook = liveBookQueryHandler.hasBook(uid, lid);
        //获取聊天室热度
        Map<Long, Integer>           mapHot       = zooManager.batchZooCount(Arrays.asList(liveShowDTO.getZooId()));
        Map<Long, List<PlayBackDTO>> mapPlayBlack = new HashMap<>();
        if (Objects.equals(LiveConst.STATE_LIVE_OVER, liveShowDTO.getState().intValue())) {
            Future<Map<Long, List<PlayBackDTO>>> playBackDTOFuture = playBackQueryHandler.map(DBPlayBackQuery.builder().liveIds(Arrays.asList(lid)).status(0).build());
            try {
                mapPlayBlack = playBackDTOFuture.get();

            } catch (Exception e) {
                logger.warn("获取直播回放失败,，异常：{}", e);
            }
        }


        //组装数据
        PureLiveDTO dto = new PureLiveDTO();

        dto.setLivePullUrlDTO(pullUrl);
        dto.setLiveShowDTO(liveShowDTO);
        dto.setMinDuration(leastDuration);
        dto.setBook(isBook);
        dto.setCustomerVo(customerVoMap.get(liveShowDTO.getUserId()));
        String h5Prefix = configManager.getH5Prefix();
        dto.setH5Prefix(h5Prefix);
        String h5Suffix = configManager.getH5Suffix();
        dto.setH5Suffix(h5Suffix);
        dto.setMainItem(itemPext == null ? null : new ItemPExtDTO(itemPext));
        dto.setLiveResourceDTOS(liveResourceDTOS);
        dto.setPlayBackDTOS(mapPlayBlack.get(lid));
        dto.setLivePureDTO(livePureDTO);
        dto.setShareDesc(shareDescTemplate);
        dto.setLiveHot(mapHot.get(liveShowDTO.getZooId()));
        liveDetailForApiCO = PureLiveConvertor.assembleLiveDetail(dto);


        return liveDetailForApiCO;
    }


}
