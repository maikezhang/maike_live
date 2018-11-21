package cn.idongjia.live.restructure.repo;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.common.query.BaseSearch;
import cn.idongjia.defend.exception.SentinelExcetionHandler;
import cn.idongjia.essearch.lib.query.live.LiveEsSearch;
import cn.idongjia.live.api.live.pojo.LiveIndexResp;
import cn.idongjia.live.api.live.pojo.LiveIndexSearch;
import cn.idongjia.live.db.mybatis.mapper.LivePureMapper;
import cn.idongjia.live.db.mybatis.mapper.LiveShowMapper;
import cn.idongjia.live.db.mybatis.po.LiveAuctionSessionPO;
import cn.idongjia.live.db.mybatis.po.LivePurePO;
import cn.idongjia.live.db.mybatis.po.LiveShow4IndexPO;
import cn.idongjia.live.db.mybatis.po.LiveShowPO;
import cn.idongjia.live.db.mybatis.query.DBLiveShowQuery;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.live.LivePullUrl;
import cn.idongjia.live.pojo.live.LiveTypeConfig;
import cn.idongjia.live.pojo.live.LiveVideoCover;
import cn.idongjia.live.pojo.purelive.PureLive;
import cn.idongjia.live.restructure.cloud.qcloud.QCloudClient;
import cn.idongjia.live.restructure.domain.entity.user.Category;
import cn.idongjia.live.restructure.domain.entity.user.LiveAnchor;
import cn.idongjia.live.restructure.domain.entity.zoo.LiveZoo;
import cn.idongjia.live.restructure.dto.LivePureDTO;
import cn.idongjia.live.restructure.dto.LiveRoomDTO;
import cn.idongjia.live.restructure.dto.LiveSearch4HomepageDTO;
import cn.idongjia.live.restructure.dto.LiveShow4IndexDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.dto.SearchIndexRespDTO;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.restructure.manager.LiveEsSearchManager;
import cn.idongjia.live.restructure.manager.MqProducerManager;
import cn.idongjia.live.restructure.manager.SearchManager;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.restructure.manager.ZooManager;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.live.v2.pojo.LiveAround;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.search.pojo.query.Sort;
import cn.idongjia.search.pojo.query.live.LiveQuery;
import cn.idongjia.util.Utils;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static cn.idongjia.live.support.DateTimeUtil.millis2Timestamp;
import static cn.idongjia.util.Utils.getCurrentMillis;

//import cn.idongjia.live.pojo.LiveIndexSearch;

/**
 * @author longchuan
 */
@Component
public class LiveShowRepo {

    private static final Log LOGGER = LogFactory.getLog(LiveShowRepo.class);
    @Resource
    private LiveRoomRepo      liveRoomRepo;
    @Resource
    private LiveShowMapper    liveShowMapper;
    @Resource
    private UserManager       userManager;
    @Resource
    private MqProducerManager mqProducerManager;
    @Resource
    private ZooManager        zooManager;
    @Resource
    private SearchManager     searchManager;
    @Resource
    private ConfigManager     configManager;

    @Resource
    private LivePureMapper livePureMapper;

    private final static List<Integer> liveTypes = Arrays.asList(LiveEnum.LiveType.PURE_LIVE.getCode(), LiveEnum.LiveType.CRAFTS_PURCHASE_TYPE.getCode(), LiveEnum.LiveType.TREASURE_TYPE.getCode(),
            LiveEnum.LiveType.PURE_LIVE.getCode(), LiveEnum.LiveType.CRAFTS_TALK_TYPE.getCode(), LiveEnum.LiveType.PURE_LIVE.getCode(), LiveEnum.LiveType.ELSE_TYPE.getCode(),
            LiveEnum.LiveType.OPEN_MATERIAL_TYPE.getCode());

    @Resource
    private LiveEsSearchManager liveEsSearchManager;
    private static final List<Integer> states = Arrays.asList(LiveConst.STATE_LIVE_NOT_BEGIN, LiveConst.STATE_LIVE_IN_PROGRESS);

    private static final List<Integer> allUseableStates = Arrays.asList(LiveConst.STATE_LIVE_NOT_BEGIN, LiveConst.STATE_LIVE_IN_PROGRESS, LiveConst.STATE_LIVE_OVER);

    /**
     * 添加直播  by zhangyingjie
     *
     * @param dto 直播dto
     * @return 直播id
     */
    public Long addLiveShow(LiveShowDTO dto) {
        liveShowMapper.insert(dto.toDO());
        return dto.getId();
    }

    public boolean updateLiveShowOnline(Long lid, Integer online) {
        LiveShowPO po = new LiveShowPO();
        po.setId(lid);
        po.setOnline(online);
        return liveShowMapper.update(po, Utils.getCurrentMillis()) > 0;

    }

    public boolean updateLiveShowAutoOnline(Long lid, Integer online) {
        LiveShowPO po = new LiveShowPO();
        po.setId(lid);
        po.setAutoOnline(online);
        return liveShowMapper.update(po, Utils.getCurrentMillis()) > 0;

    }

    public void modifyGeneralWeight(Long lid, Integer weight) {
        LiveShowPO po = new LiveShowPO();
        po.setId(lid);
        po.setGeneralWeight(weight);
        liveShowMapper.update(po, Utils.getCurrentMillis());
    }

    public boolean resetStartTimeAndEndTime(Long liveId) {

        return liveShowMapper.resetStartTimeAndEndTime(liveId) > 0;
    }


    /**
     * 添加
     *
     * @param dto 纯直播
     * @return 直播id
     * @author zhangyingjie
     */
    public Long addPureLive(LivePureDTO dto) {
        livePureMapper.insert(dto.toDO());
        return dto.getId();
    }

    /**
     * 更新纯直播
     *
     * @param dto 纯直播dto
     * @return true or false
     * @author zhangyingjie
     */
    public boolean updatePureLive(LivePureDTO dto) {
        return livePureMapper.update(dto.toDO()) > 0;

    }


    private LiveVideoCover buildVideoCover(PureLive pureLive) {
        LiveVideoCover cover = new LiveVideoCover();
        cover.setDuration(pureLive.getVideoCoverDuration());
        cover.setPic(pureLive.getVideoCoverPic());
        cover.setUrl(pureLive.getVideoCoverUrl());
        cover.setId(pureLive.getVideoCoverId());
        return cover;
    }

    public void updatePureLiveOnline(long liveId, int status) {
        LivePurePO po = LivePurePO.builder().status(status).id(liveId).build();
        livePureMapper.update(po);
    }


    /**
     * 根据主播id和预计开始时间获取预计结束时间
     *
     * @param uid          主播id
     * @param preStartTime 预计开始时间
     * @return 预计结束时间
     * @author zhangyingjie
     */
    public Long nextLive(Long uid, Long preStartTime) {
        DBLiveShowQuery dbLiveShowQuery = DBLiveShowQuery.builder().userIds(Arrays.asList(uid)).minEstimatedStartTime
                (new Timestamp(preStartTime)).build();
        List<LiveShowDTO> liveShowDTOS    = listLiveShows(dbLiveShowQuery);
        LiveShowDTO       nextLiveShowDTO = null;
        if (!Utils.isEmpty(liveShowDTOS)) {
            liveShowDTOS = liveShowDTOS.stream().filter(liveShowDTO -> !liveShowDTO.getPreviewTime().equals
                    (preStartTime)).collect(Collectors.toList());
            if (!Utils.isEmpty(liveShowDTOS)) {
                nextLiveShowDTO = liveShowDTOS.get(0);
            }
        }
        if (Objects.isNull(nextLiveShowDTO)) {
            return preStartTime + TimeUnit.HOURS.toMillis(configManager.getLiveStartInterval());
        }
        Long nextPreStartTime = nextLiveShowDTO.getPreviewTime();
        Long now              = Utils.getCurrentMillis();
        Long preEndTime;
        //如果下一场开播时间 - 当前时间< 6小时
        if (nextPreStartTime - now < TimeUnit.HOURS.toMillis(configManager.getLiveStartInterval())) {
            preEndTime = nextPreStartTime - TimeUnit.MINUTES.toMillis(configManager.getLiveEndInterval());
        } else {
            preEndTime = preStartTime + TimeUnit.HOURS.toMillis(configManager.getLiveStartInterval());
        }
        return preEndTime;
    }

    /**
     * 更新liveShow 数据
     *
     * @param liveShowPO 直播数据
     * @return 更新结果
     * @author zhangyingjie
     */
    public boolean updateLiveShow(LiveShowPO liveShowPO) {

        return liveShowMapper.update(liveShowPO, Utils.getCurrentMillis()) > 0;

    }

    /**
     * 获取直播主播同一时间其它的直播
     *
     * @param dbLiveShowQuery
     * @return
     * @author zhangyingjie
     */
    public List<LiveShowPO> countByUpdateTime(DBLiveShowQuery dbLiveShowQuery) {
        return liveShowMapper.countByUpdateTime(dbLiveShowQuery);
    }


    /**
     * 根据直播ID开始直播
     *
     * @param liveShowDTO 直播内容
     * @return 返回是否开播成功
     */
    public boolean startLiveShow(LiveShowDTO liveShowDTO) {
        LiveShowPO liveShowPO = liveShowDTO.toDO();
        return liveShowMapper.update(liveShowPO, Utils.getCurrentMillis()) == 1;
    }

    /**
     * 根据直播ID来停止直播
     *
     * @param liveShowDTO 直播ID
     * @return 返回是否停止直播成功
     */
    public boolean stopLiveShow(LiveShowDTO liveShowDTO) {
        // 补全直播信息并更新数据表
        LiveShowPO liveShowPO = liveShowDTO.toDO();
        liveShowPO.setEndTime(millis2Timestamp(getCurrentMillis()));
        liveShowPO.setModifiedTime(millis2Timestamp(getCurrentMillis()));
        liveShowPO.setState(LiveConst.STATE_LIVE_OVER);
        liveShowMapper.update(liveShowPO, Utils.getCurrentMillis());
        return true;
    }

    /**
     * 根据直播ID恢复直播频道
     *
     * @param liveShowId 直播ID
     * @return 是否成功
     */
    public boolean resumeLiveShow(Long liveShowId) {
        // 获取直播主播ID
        LiveShowDTO liveShowDTO = getById(liveShowId);
        boolean     isSuccess   = liveRoomRepo.startLiveShow(liveShowDTO.getRoomId());
        // 恢复直播
        LiveShowRepo.LOGGER.info("直播ID: " + liveShowId + "恢复直播成功");
        return isSuccess;
    }

    /**
     * 根据直播ID修改直播聊天室基准随机数并立即生效
     *
     * @param liveShowId 直播ID
     * @param zrc        聊天室基准随机数
     * @return 是否修改成功
     */
    public boolean modifyZrcAndEffectNow(Long liveShowId, Integer zrc) {
        LiveShowRepo.LOGGER.info("更改聊天室随机数——直播ID: " + liveShowId + " 人数: " + zrc);
        // 根据直播ID获取直播信息DO
        LiveShowDTO liveShowFromDB = getById(liveShowId);
        // 更新聊天室信息
        zooManager.modifyZooRoom(liveShowFromDB.getZooId(), liveShowFromDB.getTitle(), zrc, null);
        // 立即生效修改的随机数
        zooManager.turnZooRoomUserCountToTarget(liveShowFromDB.getZooId(), zrc);
        return true;
    }

    /**
     * 根据直播ID获取直播聊天室用户数实时人数
     *
     * @param liveShowId 直播ID
     * @return 聊天室实时人数
     */
    public Integer getLiveUserCountRealTime(Long liveShowId) {
        LiveShowDTO liveShowDTO = getById(liveShowId);
        return zooManager.getZooRoomUserCountTimely(liveShowDTO.getZooId());
    }

    /**
     * 根据直播ID获取直播推流地址
     *
     * @param liveShowId 直播ID
     * @return 推流地址
     */
    public String getLivePushUrl(Long liveShowId) {
        LiveShowDTO liveShowDTO = getById(liveShowId);
        return liveRoomRepo.getPushUrlById(liveShowDTO.getRoomId(), liveShowDTO.getTitle());
    }

    public LivePullUrl getLivePullUrls(Long roomId) {
        if (null != roomId) {
            return liveRoomRepo.getPullUrls(roomId);
        }
        return null;
    }

    private LivePullUrl getLivePullUrls(LiveRoomDTO liveRoomDTO) {
        if (null != liveRoomDTO) {
            return liveRoomRepo.getPullUrls(liveRoomDTO);
        }
        return null;
    }

    public LiveShowDTO getLiveShowByZid(Long zooId) {
        DBLiveShowQuery  dbLiveShowQuery = DBLiveShowQuery.builder().zooId(zooId).build();
        List<LiveShowPO> liveShows       = liveShowMapper.list(dbLiveShowQuery);
        if (liveShows != null && liveShows.size() != 0) {
            return new LiveShowDTO(liveShows.get(0));
        } else {
            return null;
        }
    }

    /**
     * 按照主播uid查询其正在直播的标题
     *
     * @param uid 主播uid
     * @return 直播列表
     */
    @SentinelResource(value = "LiveShowRepo.getLiveShowByUid", entryType = EntryType.IN, blockHandler = "handleException", blockHandlerClass = {SentinelExcetionHandler.class})
    public LiveShowDTO getLiveShowByUid(Long uid) {
        LiveShowPO liveShowPO = liveShowMapper.getLiveShowByUidValid(uid, millis2Timestamp(getCurrentMillis()));
        if (liveShowPO == null) {
            throw new LiveException(-12138, "主播该时间端没有对应的直播");
        }
        return new LiveShowDTO(liveShowPO);
    }


    /**
     * 根据检索条件获取直播列表
     *
     * @param dbLiveShowQuery 检索条件
     * @return 直播信息列表
     */
    @SentinelResource(value = "LiveShowRepo.listLiveShows", entryType = EntryType.IN, blockHandler = "handleException", blockHandlerClass = {SentinelExcetionHandler.class})
    public List<LiveShowDTO> listLiveShows(DBLiveShowQuery dbLiveShowQuery) {
        List<Integer> liveTypes = dbLiveShowQuery.getTypes();
        if (!CollectionUtils.isEmpty(liveTypes)) {
            if (liveTypes.contains(LiveEnum.LiveType.PURE_LIVE.getCode())) {
                List<Integer> type = new ArrayList<>();
                type.add(3);
                type.add(4);
                type.add(5);
                type.add(6);
                type.add(7);
                type.addAll(liveTypes);
                dbLiveShowQuery.setTypes(type);
            }

        }
        List<LiveShowPO> liveShowPOS = liveShowMapper.list(dbLiveShowQuery);
        if (Utils.isEmpty(liveShowPOS)) {
            return new ArrayList<>();
        }
        return liveShowPOS.stream().map(LiveShowDTO::new).collect(Collectors.toList());
    }

    @SentinelResource(value = "LiveShowRepo.count", entryType = EntryType.IN, blockHandler = "handleException", blockHandlerClass = {SentinelExcetionHandler.class})
    public Integer count(DBLiveShowQuery dbLiveShowQuery) {
        List<Integer> liveTypes = dbLiveShowQuery.getTypes();
        if (!CollectionUtils.isEmpty(liveTypes)) {
            if (liveTypes.contains(LiveEnum.LiveType.PURE_LIVE.getCode())) {
                List<Integer> type = new ArrayList<>();
                type.add(3);
                type.add(4);
                type.add(5);
                type.add(6);
                type.add(7);
                type.addAll(liveTypes);
                dbLiveShowQuery.setTypes(type);
            }

        }
        return liveShowMapper.count(dbLiveShowQuery);
    }

    /**
     * 重置直播实际开始时间和结束时间
     *
     * @param id 直播ID
     */
    public boolean restRealTime(Long id) {
        return liveShowMapper.restRealTime(id) > 0;
    }

    /**
     * 根据主播ID获取其当前有效的直播场次
     *
     * @param uid 主播ID
     * @return 直播列表（暂时只返回一场）
     */
    public List<LiveShowDTO> listEndAfterNowByUId(Long uid) {
        LiveShowPO liveShowPO = liveShowMapper.getByUidEndAfterNow(uid, new Timestamp(Utils.getCurrentMillis()));
        if (liveShowPO == null) {
            return new ArrayList<>();
        }
        List<LiveShowDTO> reList = new ArrayList<>();
        reList.add(new LiveShowDTO(liveShowPO));
        return reList;
    }

    /**
     * 根据主播ID获取其正在直播的纯直播
     *
     * @param uid 主播ID
     * @return 直播
     */
    @SentinelResource(value = "LiveShowRepo.getLiveShowOnByUid", entryType = EntryType.IN, blockHandler = "handleException", blockHandlerClass = {SentinelExcetionHandler.class})
    public LiveShowDTO getLiveShowOnByUid(Long uid) {
        DBLiveShowQuery dbLiveShowQuery = DBLiveShowQuery.builder().userIds(Arrays.asList(uid)).states(Arrays.asList
                (LiveConst.STATE_LIVE_IN_PROGRESS)).orderBy("createtm desc").build();
        List<LiveShowPO> liveShowPOS = liveShowMapper.list(dbLiveShowQuery);
        if (!Utils.isEmpty(liveShowPOS)) {
            return new LiveShowDTO(liveShowPOS.get(0));
        } else {
            return null;
        }
    }

    /**
     * 获取时间冲突直播列表
     *
     * @param preViewTime 直播预展时间
     * @param preEndTm    直播结束时间
     * @param uid         直播ID
     * @return 冲突直播列表
     */
    public List<LiveShowPO> overlapLiveList(Timestamp preViewTime, Timestamp preEndTm, Long uid) {
        return liveShowMapper.countByTime(preViewTime, preEndTm, uid);
    }

    /**
     * 获取时间冲突直播列表
     *
     * @param preViewTime 直播预展时间
     * @param preEndTm    直播结束时间
     * @param uid         直播ID
     * @return 冲突直播列表
     */
    public List<LiveShowPO> overlapLiveNewList(Long preViewTime, Long preEndTm, Long uid) {
        return liveShowMapper.countOverlapByPreViewTime(preViewTime, preEndTm, uid);
    }

    @SentinelResource(value = "LiveShowRepo.overlapLiveByPreStartTmList", entryType = EntryType.IN, blockHandler = "handleException", blockHandlerClass = {SentinelExcetionHandler.class})
    public List<LiveShowPO> overlapLiveByPreStartTmList(Long preStartTime, Long preEndTm, Long uid, Long liveId) {
        return liveShowMapper.countOverlapByPreStartTime(preStartTime, preEndTm, uid, liveId);
    }

    /**
     * 根据直播ID获取直播云类型
     *
     * @param liveShowId 直播ID
     * @return 直播云类型
     */
    public Integer getCloudType(Long liveShowId, Long roomId) {
        if (null != roomId) {
            return liveRoomRepo.getById(roomId).getCloudType();
        }
        LiveShowDTO liveShowDTO = getById(liveShowId);
        return liveRoomRepo.getById(liveShowDTO.getRoomId()).getCloudType();
    }

    @SentinelResource(value = "LiveShowRepo.getById", entryType = EntryType.IN, blockHandler = "handleException", blockHandlerClass = {SentinelExcetionHandler.class})
    public LiveShowDTO getById(Long id) {
        LiveShowPO liveShowPO = liveShowMapper.get(id);
        if (liveShowPO != null) {
            return new LiveShowDTO(liveShowPO);
        }
        throw LiveException.failure("直播不存在");
    }

    /**
     * 根据用户ID获取刚结束的直播ID
     *
     * @param uid 用户ID
     * @return 直播ID
     */
    @SentinelResource(value = "LiveShowRepo.getLiveShowRecentlyEnd", entryType = EntryType.IN, blockHandler = "handleException", blockHandlerClass = {SentinelExcetionHandler.class})
    public LiveShowDTO getLiveShowRecentlyEnd(Long uid) {
        DBLiveShowQuery liveShowSearch = DBLiveShowQuery.builder().states(Arrays.asList(LiveConst.STATE_LIVE_OVER))
                .userIds(Arrays.asList(uid)).limit(1).orderBy("endtm desc").build();
        List<LiveShowPO> list = liveShowMapper.list(liveShowSearch);
        if (list != null && list.size() != 0) {
            return new LiveShowDTO(list.get(0));
        } else {
            throw new LiveException(-12138, "该用户没有已结束的直播");
        }
    }


    @SentinelResource(value = "LiveShowRepo.searchLive", entryType = EntryType.IN, blockHandler = "handleException", blockHandlerClass = {SentinelExcetionHandler.class})
    public List<LiveIndexResp> searchLive(LiveIndexSearch liveIndexSearch) {
        if (liveIndexSearch.getLimit() == null) {
            liveIndexSearch.setLimit(500);
        }
        List<LiveShow4IndexPO> liveShow4IndexPOS = liveShowMapper.listForIndex(null, liveIndexSearch.getIds(),
                liveIndexSearch.getUpdateTime(), liveIndexSearch.getOffset(), liveIndexSearch.getLimit());
        if (Utils.isEmpty(liveShow4IndexPOS)) {
            return null;
        }

        List<Long>         zids   = liveShow4IndexPOS.stream().map(LiveShow4IndexPO::getZid).collect(Collectors.toList());
        Map<Long, LiveZoo> zooMap = zooManager.map(zids);
        Map<Long, User> userMap = userManager.takeBatchUser(liveShow4IndexPOS.stream().map
                (LiveShow4IndexPO::getUserId).collect(Collectors.toList()));
        return liveShow4IndexPOS.stream().map(liveShow4IndexPO -> {
            LiveShow4IndexDTO liveShow4IndexDTO = new LiveShow4IndexDTO(liveShow4IndexPO);
            return liveShow4IndexDTO.assembleLive4Index(zooMap.getOrDefault(liveShow4IndexPO.getZid(), null), userMap
                    .get(liveShow4IndexPO.getUserId()));
        }).collect(Collectors.toList());
    }


    public List<LiveShow4IndexDTO> searchLiveWithCategory(List<Long> ids, Long updateTime, Integer offset, Integer limit) {
        if (limit == null) {
            limit = 500;
        }
        List<LiveShow4IndexPO> liveShow4IndexPOS = liveShowMapper.listForIndex(allUseableStates, ids,
                updateTime, offset, limit);
        if (Utils.isEmpty(liveShow4IndexPOS)) {
            return null;
        }
        return liveShow4IndexPOS.stream().map(liveShow4IndexPO -> {
            return new LiveShow4IndexDTO(liveShow4IndexPO);
        }).collect(Collectors.toList());
    }

    public List<SearchIndexRespDTO> getLiveList(Integer page, Integer limit, Integer type) {
        if (limit == null) {
            limit = 10;
        }
        if (type == null) {
            type = 0;
        }
        LiveQuery liveQuery = new LiveQuery();
        liveQuery.setNum(limit);
        BaseSearch baseSearch = new BaseSearch();
        baseSearch.setLimit(limit);
        baseSearch.setPage(page);
        Integer offset = baseSearch.getOffset();
        liveQuery.setStart(offset);
        liveQuery.setOnLine(1);
        liveQuery.setStatusList(Arrays.asList(0, 1, 2));
        List<Sort> sorts = new ArrayList<>();
        // state desc,weight desc,uv desc, pre_start_time desc, create_time desc
        sorts.add(new Sort("state", SolrQuery.ORDER.desc));
        sorts.add(new Sort("weight", SolrQuery.ORDER.desc));
        sorts.add(new Sort("uv", SolrQuery.ORDER.desc));
        sorts.add(new Sort("pre_start_time", SolrQuery.ORDER.asc));
        sorts.add(new Sort("create_time", SolrQuery.ORDER.desc));
        liveQuery.setSortList(sorts);
        // 0-全部直播数据  1-匠购(纯直播数据 进行中和未开始)  2-拍卖直播  3-回放
        List<Integer> lTtype  = new ArrayList<>();
        List<Integer> lStatus = new ArrayList<>();

        switch (type) {
            case 1:
                lTtype.addAll(liveTypes);
                liveQuery.setTypeList(lTtype);
                lStatus.add(LiveEnum.LiveState.UNSTART.getCode());
                lStatus.add(LiveEnum.LiveState.PLAYING.getCode());
                liveQuery.setStateList(lStatus);
                break;
            case 2:
                lTtype.add(LiveEnum.LiveType.LIVE_AUCTION.getCode());
                lStatus.add(LiveEnum.LiveState.UNSTART.getCode());
                lStatus.add(LiveEnum.LiveState.PLAYING.getCode());
                liveQuery.setStateList(lStatus);
                liveQuery.setTypeList(lTtype);
                break;
            case 3:
                liveQuery.setHasPlayback(LiveEnum.HasPlayback.YES.getCode());
                liveQuery.setStateList(Collections.singletonList(LiveEnum.LiveState.FINISHED.getCode()));
                liveQuery.setTypeList(liveTypes);
                sorts.clear();
                sorts.add(new Sort("end_time", SolrQuery.ORDER.desc));
                sorts.add(new Sort("create_time", SolrQuery.ORDER.desc));
                liveQuery.setSortList(sorts);
                break;
            default:
                liveQuery.setStateList(Arrays.asList(LiveEnum.LiveState.PLAYING.getCode(), LiveEnum.LiveState.UNSTART
                        .getCode()));
                break;
        }


        BaseList<SearchIndexRespDTO> searchIndexRespDTOBaseList = searchManager.takeLiveFromIndex(liveQuery);
        List<SearchIndexRespDTO>     items                      = searchIndexRespDTOBaseList.getItems();

        if (type.equals(0) && items != null && items.size() < limit) {
            liveQuery.setHasPlayback(LiveEnum.HasPlayback.YES.getCode());
            liveQuery.setStateList(Collections.singletonList(LiveEnum.LiveState.FINISHED.getCode()));
            liveQuery.setTypeList(liveTypes);
            sorts.clear();
            sorts.add(new Sort("end_time", SolrQuery.ORDER.desc));
            sorts.add(new Sort("create_time", SolrQuery.ORDER.desc));
            liveQuery.setNum(limit - items.size());
            offset = offset - searchIndexRespDTOBaseList.getCount();
            liveQuery.setStart(offset <= 0 ? 0 : offset);
            liveQuery.setSortList(sorts);
            liveQuery.setOnLine(1);

            BaseList<SearchIndexRespDTO> searchIndexRespDTOBaseList1 = searchManager.takeLiveFromIndex(liveQuery);
            if (Utils.isEmpty(items)) {
                items = new ArrayList<>();
            }
            List<SearchIndexRespDTO> tmpList = searchIndexRespDTOBaseList1.getItems();
            if (Utils.isEmpty(tmpList)) {
                tmpList = new ArrayList<>();
            }
            items.addAll(tmpList);
            searchIndexRespDTOBaseList.setItems(items);
        }
        return searchIndexRespDTOBaseList.getItems();
    }


    public String getLiveListShareUrl() {
        // ?share=0&djtitle=&djdesc=&djpic="
        try {
            return configManager.getLiveListUrl() + "?share=0&djtitle=" + URLEncoder.encode(configManager
                    .getLiveListShareTitle(), "UTF-8") + "&djdesc=" + URLEncoder.encode(configManager
                    .getLiveListShareDesc(), "UTF-8") + "&djpic=" + configManager.getLiveListSharePic();
        } catch (UnsupportedEncodingException e) {
            LiveShowRepo.LOGGER.error("获取直播列表分享链接失败{}", e);
        }
        return "";
    }

    public long updateLiveUVTime() {
        return liveShowMapper.updateForIndexUV(new Timestamp(Utils.getCurrentMillis()), LiveEnum.LiveState.PLAYING
                .getCode());
    }

    public LiveShowDTO getNextLive(DBLiveShowQuery dbLiveShowQuery, Long currentId) {
        List<LiveShowDTO> liveShowDTOS = listLiveShows(dbLiveShowQuery);
        if (Utils.isEmpty(liveShowDTOS)) {
            return null;
        }
        //获取当前直播的下一个直播
        LiveShowDTO liveShowDTO = null;
        for (int i = 0; i < liveShowDTOS.size(); i++) {
            liveShowDTO = liveShowDTOS.get(i);
            if (liveShowDTO.getId().equals(currentId)) {
                //最后一个
                if (i == liveShowDTOS.size() - 1) {
                    liveShowDTO = null;
                    break;
                }
                liveShowDTO = liveShowDTOS.get(i + 1);
            }
        }


        return liveShowDTO;
    }

//    public void liveShowAutoOnline(long liveId, int autoOnline) {
//        LiveShowDO liveShowDO = new LiveShowDO();
//        liveShowDO.setId(liveId);
//        liveShowDO.setAutoOnline(autoOnline);
//        liveShowDO.setModifiedTm(new Timestamp(Utils.getCurrentMillis()));
//        liveShowMapper.update(liveShowDO);
//    }

    public String getLivePushUrl(String cloudId, Long txTime) {
        //通过cloudId 和txTime 获取推流地址
        QCloudClient qCloudClient = new QCloudClient();
        String       url          = qCloudClient.getPushUrl(cloudId, txTime / 1000);

        return url;


    }

    public List<Map<String, Object>> getLiveMixTabContent() {
        String                    tabContent = configManager.getLiveMixTabContent();
        JsonParser                parser     = new JsonParser();
        JsonArray                 array      = parser.parse(tabContent).getAsJsonArray();
        List<Map<String, Object>> result     = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {

            JsonArray           arr     = array.get(i).getAsJsonArray();
            Integer             type    = arr.get(0).getAsInt();
            String              content = arr.get(1).getAsString();
            Map<String, Object> map1    = new HashMap<>();
            map1.put("type", type);
            map1.put("content", content);
            result.add(map1);
        }

        return result;
    }


    /**
     * 根据纯直播ID获取纯直播人数
     *
     * @param pureLiveId 纯直播ID
     * @return 人数结构
     */
    public Map<String, Object> getLiveOnlineNum(Long pureLiveId) {
        //根据ID获取纯直播信息
        LiveShowDTO liveShowDTO = getById(pureLiveId);
        //组装结果数据
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("id", pureLiveId);
        reMap.put("title", liveShowDTO.getTitle());
        reMap.put("count", zooManager.getZooRoomUserCountTimely(liveShowDTO.getZooId()));
        return reMap;
    }


    public BaseList<LiveSearch4HomepageDTO> searchHomepage(LiveEsSearch search) {
        return liveEsSearchManager.homeSearch(search);
    }

    public List<LiveAuctionSessionPO> getLiveAuctionSession(Integer limit, Integer offset) {
        return liveShowMapper.getLiveSessionInfo(limit, offset);
    }

    /**
     * 获取直播类型配置
     *
     * @return
     */
    public List<LiveTypeConfig> getLiveTypeConfig() {

        String               liveType = configManager.getLiveTypeConfig();
        JsonParser           parser   = new JsonParser();
        JsonArray            array    = parser.parse(liveType).getAsJsonArray();
        List<LiveTypeConfig> result   = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {

            JsonArray      arr     = array.get(i).getAsJsonArray();
            Integer        type    = arr.get(0).getAsInt();
            String         content = arr.get(1).getAsString();
            LiveTypeConfig config  = new LiveTypeConfig();
            config.setId(type);
            config.setName(content);
            result.add(config);
        }
        return result;
    }

    /**
     * map  获取直播类型配置
     *
     * @return
     */
    public Map<Integer, String> mapLiveTypeConfig() {

        List<LiveTypeConfig> liveTypeConfig = getLiveTypeConfig();
        if (CollectionUtils.isEmpty(liveTypeConfig)) {
            return null;
        }
        return liveTypeConfig.stream().collect(Collectors.toMap(v1 -> v1.getId(), v2 -> String.format("【%s】", v2.getName()), (v1, v2) -> v1));
    }

}
