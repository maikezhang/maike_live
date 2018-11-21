package cn.idongjia.live.restructure.impl.live;

import cn.idongjia.live.api.live.LiveService;
import cn.idongjia.live.api.live.pojo.*;
import cn.idongjia.live.api.live.pojo.response.MultiResponse;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.live.*;
import cn.idongjia.live.query.LiveSearch;
import cn.idongjia.live.query.live.LiveListApiSearch;
import cn.idongjia.live.query.live.LivePreSearch;
import cn.idongjia.live.query.live.LiveShowSearch;
import cn.idongjia.live.restructure.biz.LiveShowBO;
import cn.idongjia.live.restructure.domain.service.LiveDomainService;
import cn.idongjia.live.restructure.pojo.co.LiveWithCategoryCO;
import cn.idongjia.live.restructure.pojo.co.live.LiveDetailForApiCO;
import cn.idongjia.live.restructure.pojo.co.tab.LiveCO;
import cn.idongjia.live.restructure.pojo.query.ESLiveQry;
import cn.idongjia.live.restructure.query.*;
import cn.idongjia.live.support.ObjectUtils;
import cn.idongjia.live.v2.pojo.LiveAround;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.idongjia.util.Utils.isEmpty;

@Component("liveServiceImpl")
public class LiveServiceImpl implements LiveService {


    @Resource
    private LiveShowBO liveShowBO;

    @Resource
    private LiveShowQueryHandler liveShowQueryHandler;

    @Resource
    private LiveApiQueryHandler liveApiQueryHandler;

    @Resource
    private LiveESQueryHandler liveESQueryHandler;

    @Resource
    private LiveDomainService liveDomainService;

    @Resource
    private LivePullUrlQueryHandler livePullUrlQueryHandler;

    @Resource
    private LiveAroundQueryHandler liveAroundQueryHandler;

    @Resource
    private LiveRoomQueryHandler liveRoomQueryHandler;

    @Resource
    private LiveZooQueryHandler liveZooQueryHandler;


    @Resource
    private LivePreviewQueryHandler livePreviewQueryHandler;

    @Override
    public Long createLiveShow(LiveShow liveShow) {

        return liveDomainService.createLiveShow(liveShow);
    }

    @Override
    public boolean updateLiveShowById(Long liveShowId, LiveShow liveShow) {

        return liveDomainService.updateLiveShow(liveShowId, liveShow);
    }

    @Override
    public boolean deleteLiveShowById(Long liveShowId) {

        return liveShowBO.delete(liveShowId);
    }

    @Override
    public LiveShow getLiveShowById(Long liveShowId) {
        return liveShowQueryHandler.get(liveShowId);
    }

    @Override
    public List<LiveShow> listLiveShows(LiveShowSearch liveShowSearch) {
        return liveShowQueryHandler.listLiveShow(liveShowSearch);
    }

    @Override
    public boolean resumeLiveShowById(Long liveShowId) {
        liveShowBO.resume(liveShowId);
        return true;
    }

    @Override
    public boolean startLiveShowById(Long lsid) {
        liveShowBO.start(lsid, true);
        return true;
    }

    @Override
    public boolean stopLiveShowById(Long lsid) {
        liveShowBO.stop(lsid);
        return true;
    }

    @Override
    public LiveShow getLiveShowByZid(Long zid) {
        return liveShowQueryHandler.getLiveShowByZid(zid);
    }

    @Override
    public LivePullUrl getPullUrl(Long liveShowId) {
        return livePullUrlQueryHandler.getLivePullUrl(liveShowId);
    }

    @Override
    public String getPushUrl(Long liveShowId) {
        return liveRoomQueryHandler.getPushUrl(liveShowId);
    }

    @Override

    public boolean modifyLiveShowZrc(Long liveShowId, Integer zrc) {
        return liveShowBO.modifyLiveShowZrc(liveShowId, zrc);
    }

    @Override
    public Map<String, String> getLiveShowPushUrlByUid(Long uid) {
        Map<String, String> reMap = new HashMap<>();
        reMap.put("pushUrl", liveRoomQueryHandler.getPushUrlByUid(uid));
        return reMap;
    }

    @Override
    public Map<String, String> getLiveShowPushUrlByMobile(String mobile) {
        if (isEmpty(mobile)) {
            throw new LiveException(-12138, "手机号码不能为空");
        }
        Map<String, String> reMap = new HashMap<>();
        reMap.put("pushUrl", liveRoomQueryHandler.getPushUrlByMobile(mobile));
        return reMap;
    }

    @Override
    public List<LiveRecord> listAllRecordsByUid(Long uid) {
        return liveRoomQueryHandler.records(uid);
    }

    @Override
    public LiveShow4Article acquireLiveInfo(Long liveShowId) {
        return liveShowQueryHandler.getForArticle(liveShowId);
    }

    @Override
    public Integer acquireRealTimeUserCount(Long liveShowId) {
        return liveZooQueryHandler.onlineNum(liveShowId);
    }

    @Override
    public Integer getCloudTypeByLid(Long liveShowId) {
        return liveRoomQueryHandler.getCloudType(liveShowId);
    }

    @Override
    public boolean resetStartTmAndEndTm(Long liveShowId) {
        return liveShowBO.resetStartTimeAndEntTime(liveShowId);
    }

    @Override
    public List<LiveShow> listLiveShowByUid(Long uid) {
        return liveShowQueryHandler.getLiveShowByUid(uid);
    }

    /**
     * 直播拍列表索引更新查询
     *
     * @param liveIndexSearch
     * @return
     */
    @Override
    public BaseList<LiveIndexResp> searchLiveForIndex(LiveIndexSearch liveIndexSearch) {
        return liveShowQueryHandler.listForIndex(liveIndexSearch);

    }

    @Override
    public List<LiveApiResp> getLiveList(Integer page, Integer limit, Integer type) {
        return liveApiQueryHandler.listForApi(page, limit, type);
    }

    @Override
    public List<PreLiveResp> getPreLivList(Integer page) {
        return livePreviewQueryHandler.getPreLiveList(page);
    }

    @Override
    public void updateLiveOnline(LiveShow liveShow) {
        if (liveShow.getId() == null || liveShow.getOnline() == null) {
            throw new LiveException("参数错误");
        }
        liveShowBO.updateLiveOnline(liveShow);
    }

    /**
     * 通用的直播列表，包含纯直播、直播拍……
     *
     * @param search 查询条件
     * @return
     */
    @Override
    public BaseList<LiveResp> getGeneralLiveList4Admin(LiveSearch search) {
        return liveShowQueryHandler.getGeneralList(search);
    }

    /**
     * 直播列表的通用权重修改
     *
     * @param id
     * @param weight
     */
    @Override
    public void modifyGeneralWeight(Long id, Integer weight) {
        if (id == null || weight == null) {
            throw new LiveException("参数错误");
        }
        liveShowBO.modifyLiveGeneralWeight(id, weight);
    }

    /**
     * 获取直播列表分享数据
     */
    @Override
    public String getLiveListShareUrl() {
        return liveShowBO.getLiveListShareUrl();
    }

    @Override
    public LiveShow getNextLive(LiveShowSearch search) {
        ObjectUtils.checkNotNull(search, "查询条件不能为null");
        ObjectUtils.checkNotNull(search.getId(), "直播id不能为null");
        ObjectUtils.checkNotNull(search.getUid(), "主播id不能为null");
        return liveShowQueryHandler.getNextLive(search.getUid(), search.getId(), null);
    }

    /**
     * 获取直播的推流地址
     *
     * @param lid    直播id
     * @param txTime 过期时间
     * @return 推流地址
     */
    @Override
    public String getLivePushUrl(Long lid, Long txTime) {
        ObjectUtils.checkNotNull(lid, "直播id为空");
        ObjectUtils.checkNotNull(txTime, "过期时间为空");
        return liveRoomQueryHandler.getPushUrl(lid, txTime);
    }

    @Override
    public List<Map<String, Object>> getLiveMixTabContent() {
        return liveShowQueryHandler.getLiveMixTabContent();
    }

    @Override
    public List<LiveApiResp> getBatchLive(List<Long> lids) {
        ObjectUtils.checkNotNull(lids, "获取批量直播数据参数为空");
        ObjectUtils.checkArgument(lids.size() < 21, "批量获取直播数据超过限制，限制为20");


        return liveShowBO.getBatchLive(lids);
    }

    @Override
    public List<LiveAround> getListAround(Long preStartTime) {
        ObjectUtils.checkNotNull(preStartTime, "预计开始时间不能为空");
        final List<LiveAround> listAround = liveAroundQueryHandler.listAround(preStartTime);
        return listAround;
    }

    /**
     * 直播拍列表索引更新查询
     *
     * @param esLiveQry 查询条件
     * @return MultiResponse<LiveWithCategory> 此接口不返回总数量
     * @see LiveWithCategoryCO
     */
    @Override
    public MultiResponse<LiveWithCategoryCO> searchLiveWithCategroy(ESLiveQry esLiveQry) {
        List<LiveWithCategoryCO> liveWithCategories = liveESQueryHandler.searchLiveWithCategroy(esLiveQry);
        return MultiResponse.ofWithoutTotal(liveWithCategories);
    }

    /**
     * 获取直播预告列表
     *
     * @param search
     * @return
     */
    @Override
    public List<LivePreDateGroup> getLivePreDataGroup(LivePreSearch search) {
        return liveShowBO.getLivePreDataGroup(search);
    }

    /**
     * 获取直播类型数据
     *
     * @return
     */
    @Override
    public List<LiveTypeConfig> getliveTypeConfig() {
        return liveShowQueryHandler.getLiveTypeConfig();
    }

    @Override
    public LiveResponseApi getliveListApi(LiveListApiSearch search) {
        return liveShowBO.getLiveList(search);
    }

    @Override
    public boolean modifyLiveAuctionVideo() {
        return liveShowBO.modifyLiveAuctionVideo();
    }

    @Override
    public List<LiveListCO> getLivelistForConcernFeed(List<Long> liveIds) {
        ObjectUtils.checkNotNull(liveIds, "获取批量直播数据参数为空");
        ObjectUtils.checkArgument(liveIds.size() < 21, "批量获取直播数据超过限制，限制为20");


        return liveShowBO.getLivelistForConcernFeed(liveIds);
    }

    @Override
    public Long getNextLivePreStartTime(Long liveId, Long userId) {
        return liveShowBO.getNextLivePreStartTime(liveId, userId);

    }
    @Override
    public List<LiveCO> getBatchH5Live(List<Long> lids, Long uid) {
        return liveShowBO.getBatchH5Live(lids,uid);
    }

    @Override
    public LiveDetailForApiCO getLiveDetailForApi(Long uid, Long lid) {

        return liveShowBO.getLiveDetailForApi(uid, lid);
    }


}
