package cn.idongjia.live.api.live;

import cn.idongjia.live.api.live.pojo.LiveApiResp;
import cn.idongjia.live.api.live.pojo.LiveIndexResp;
import cn.idongjia.live.api.live.pojo.LiveIndexSearch;
import cn.idongjia.live.api.live.pojo.LiveResp;
import cn.idongjia.live.api.live.pojo.LiveSearchApiResp;
import cn.idongjia.live.api.live.pojo.PreLiveResp;
import cn.idongjia.live.api.live.pojo.response.MultiResponse;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.live.*;
import cn.idongjia.live.query.LiveSearch;
import cn.idongjia.live.query.live.LiveListApiSearch;
import cn.idongjia.live.query.live.LivePreSearch;
import cn.idongjia.live.query.live.LiveShowSearch;
import cn.idongjia.live.restructure.pojo.co.LiveWithCategoryCO;
import cn.idongjia.live.restructure.pojo.co.live.LiveDetailForApiCO;
import cn.idongjia.live.restructure.pojo.co.tab.LiveCO;
import cn.idongjia.live.restructure.pojo.query.ESLiveQry;
import cn.idongjia.live.v2.pojo.LiveAround;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Map;

/**
 * 直播后台接口（暂定）
 */
@Path("liveAdmin")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public interface LiveService {

    /**
     * 创建直播
     *
     * @param liveShowAdmin 直播数据
     * @return 返回直播数据表中直播ID
     */
    @Path("create")
    @POST
    Long createLiveShow(LiveShow liveShowAdmin);

    /**
     * 根据直播ID更新直播数据表中数据
     *
     * @param liveShowAdmin 直播数据
     * @return 是否更新成功
     */
    @Path("{lid:\\d+}")
    @PUT
    boolean updateLiveShowById(@PathParam("lid") Long lid, LiveShow liveShowAdmin);

    /**
     * 根据直播ID删除数据表中对应数据，逻辑删除
     *
     * @param id 直播ID
     * @return 是否删除成功
     */
    @Path("{lid:\\d+}")
    @DELETE
    boolean deleteLiveShowById(@PathParam("lid") Long id);

    /**
     * 根据直播ID从数据表中获取直播数据
     *
     * @param id 直播ID
     * @return 返回直播数据pojo
     */
    @GET
    @Path("{lid:\\d+}")
    LiveShow getLiveShowById(@PathParam("lid") Long id);

    /**
     * 根据检索条件获取符合条件的数据
     *
     * @param liveShowSearch 直播检索条件
     * @return 返回符合条件列表
     */
    @GET
    List<LiveShow> listLiveShows(LiveShowSearch liveShowSearch);

    /**
     * 继续直播功能
     *
     * @param lid 直播ID
     * @return 返回是否成功
     */
    @Path("resume/{lid:\\d+}")
    @POST
    boolean resumeLiveShowById(@PathParam("lid") Long lid);

    /**
     * 开始直播功能
     *
     * @param lid 直播ID
     * @return 返回是否成功
     */
    @Path("start/{lid:\\d+}")
    @POST
    boolean startLiveShowById(@PathParam("lid") Long lid);

    /**
     * 停止直播功能
     *
     * @param lid 直播ID
     * @return 返回是否成功
     */
    @Path("stop/{lid:\\d+}")
    @POST
    boolean stopLiveShowById(@PathParam("lid") Long lid);

    /**
     * 根据聊天室ID获取直播
     *
     * @param zid 聊天室ID
     * @return 返回直播
     */
    @Path("getLiveShow/{zid:\\d+}")
    @GET
    LiveShow getLiveShowByZid(@PathParam("zid") Long zid);

    /**
     * 根据直播ID获取直播拉流地址（播放地址）
     *
     * @param lid 直播ID
     * @return 返回拉流地址(播放地址)
     */
    @Path("pullurl/{lid:\\d+}")
    @GET
    LivePullUrl getPullUrl(@PathParam("lid") Long lid);

    /**
     * 根据直播ID获取推流地址（录制地址）
     *
     * @param lid 直播ID
     * @return 返回推流地址（录制地址）
     */
    @Path("pushurl/{lid:\\d+}")
    @GET
    String getPushUrl(@PathParam("lid") Long lid);

    /**
     * 修改直播随机人数并立即生效
     */
    @Path("modifyZrc/{lid:\\d+}/zrc/{zrc:\\d+}")
    @POST
    boolean modifyLiveShowZrc(@PathParam("lid") Long lid, @PathParam("zrc") Integer zrc);

    /**
     * 推流app端根据主播uid获取推流地址
     *
     * @param uid 主播uid
     * @return 返回主播推流地址
     */
    @GET
    @Path("/pushurl/{uid:\\d+}")
    Map<String, String> getLiveShowPushUrlByUid(@PathParam("uid") Long uid);

    /**
     * 推流app端根据主播手机号获取推流地址
     *
     * @param mobile 主播手机号
     * @return 返回主播推流地址
     */
    @GET
    @Path("/pushurl/mobile/{mobile:\\.+}")
    Map<String, String> getLiveShowPushUrlByMobile(@PathParam("mobile") String mobile);

    /**
     * 根据uid获取所有录制视频地址
     *
     * @param uid 用户ID
     * @return 录制视频地址
     */
    @GET
    @Path("records")
    List<LiveRecord> listAllRecordsByUid(@QueryParam("userId") Long uid);

    /**
     * 根据lid获取直播相关信息，To Article
     *
     * @param lid 直播id
     * @return 信息
     */
    @Path("article/{lid:\\d+}")
    @GET
    LiveShow4Article acquireLiveInfo(@PathParam("lid") Long lid);

    /**
     * 根据lid获取实时在线人数
     *
     * @param lid 直播id
     * @return 实时在线人数
     */
    @Path("realcount/{lid:\\d+}")
    @GET
    Integer acquireRealTimeUserCount(@PathParam("lid") Long lid);

    /**
     * 根据直播ID获取直播云类型
     *
     * @param lid 直播ID
     * @return 直播云类型
     */
    @Path("cloudType/{lid:\\d+}")
    @GET
    Integer getCloudTypeByLid(@PathParam("lid") Long lid);

    /**
     * 重置直播实际开始和结束时间
     *
     * @param lid 直播ID
     * @return 是否成功
     */
    @Path("resetTime/{lid:\\d+}")
    @POST
    boolean resetStartTmAndEndTm(@PathParam("lid") Long lid);

    /**
     * 根据主播ID获取其有效的直播场次信息
     *
     * @param uid 主播ID
     * @return 直播场次
     */
    @Path("valid/{uid:\\d+}")
    @GET
    List<LiveShow> listLiveShowByUid(@PathParam("uid") Long uid);

    /**
     * 直播拍列表索引更新查询
     *
     * @param liveIndexSearch
     * @return
     */
    @Path("searchLiveForIndex")
    @GET
    BaseList<LiveIndexResp> searchLiveForIndex(@BeanParam LiveIndexSearch liveIndexSearch);

    /**
     * 获取直播列表
     *
     * @param page  page
     * @param limit limit
     * @param type  直播列表数据  0-全部直播数据  1-匠购  2-拍卖直播  3-回放
     * @return 直播列表数据
     */
    @Path("getLiveList")
    @GET
    List<LiveApiResp> getLiveList(@QueryParam("page") Integer page, @QueryParam("limit") Integer limit, @QueryParam
            ("type") Integer type);

    @Path("getLiveListShareUrl")
    @GET
    String getLiveListShareUrl();

    @Path("getPreLivList")
    @GET
    List<PreLiveResp> getPreLivList(Integer page);

    /**
     * 管理后台 通用的直播列表，包含纯直播、直播拍……
     *
     * @param search 查询条件
     * @return
     */
    @Path("getGeneralLiveList")
    @POST
    BaseList<LiveResp> getGeneralLiveList4Admin(LiveSearch search);

    /**
     * 直播列表的通用权重修改
     *
     * @param id
     * @param weight
     */
    @Path("modifyGeneralWeight/{id:\\d+}/{weight:\\d+}")
    @POST
    void modifyGeneralWeight(Long id, Integer weight);

    /**
     *
     */
    @Path("updateLiveOnline")
    @POST
    void updateLiveOnline(LiveShow liveShow);

    /**
     * 获取当前用户的当前直播的下一个直播
     */
    @Path("nextLive")
    @GET
    LiveShow getNextLive(LiveShowSearch search);

    /**
     * 获取直播的推流地址
     *
     * @param lid    直播id
     * @param txTime 过期时间
     * @return 推流地址
     */
    @Path("getLivePushUrl/{lid:\\d+}/{txTime:\\d+}")
    @GET
    String getLivePushUrl(@PathParam("lid") Long lid, @PathParam("txTime") Long txTime);

    @Path("getLiveMixTabContent")
    @GET
    List<Map<String, Object>> getLiveMixTabContent();

    /**
     * 批量获取直播数据  提供给押窑的接口
     *
     * @param lids 批量直播id
     * @return 直播数据
     */
    @GET
    @Path("getBatchLive")
    List<LiveApiResp> getBatchLive(@QueryParam("liveIds") List<Long> lids);

    /**
     * 附件直播获取
     *
     * @param preStartTime 13位时间戳
     * @return
     */
    @GET
    @Path("getListAround/{preStartTime:\\d+}")
    List<LiveAround> getListAround(@PathParam("preStartTime") Long preStartTime);


    /**
     * 直播拍列表索引更新查询
     *
     * @param esLiveQry 查询条件
     * @return MultiResponse<LiveWithCategory>
     * @see LiveWithCategoryCO
     */
    @Path("searchLiveWithCategory")
    @POST
    MultiResponse<LiveWithCategoryCO> searchLiveWithCategroy( ESLiveQry esLiveQry);


    @GET
    @Path("getLivePreDataGroup")
    List<LivePreDateGroup> getLivePreDataGroup(@BeanParam LivePreSearch search);


    @GET
    @Path("getliveTypeConfig")
    List<LiveTypeConfig> getliveTypeConfig();


    @GET
    @Path("getliveListApi")
    LiveResponseApi getliveListApi(@BeanParam LiveListApiSearch search);


    /**
     * 修复直播拍的小视频没有数据的问题  只执行一次
     * @return
     */
    @POST
    @Path("modifyLiveAuctionVideo")
    boolean modifyLiveAuctionVideo();


    /**
     * 批量获取直播数据  提供给关注流使用
     * @param liveIds 直播id
     * @return 直播数据
     */
    @GET
    @Path("getLivelistForConcernFeed")
    List<LiveListCO> getLivelistForConcernFeed(@QueryParam("liveIds") List<Long> liveIds);

    @GET
    @Path("getNextLivePreStartTime/{liveId:\\d+}/{userId:\\d+}")
    Long getNextLivePreStartTime(@PathParam("liveId") Long liveId,@PathParam("userId") Long userId);
    /**
     * 提供给超级模板的接口
     * @param lids 直播id
     * @param uid 用户id
     * @return
     */
    @GET
    @Path("getBatchH5Live")
    List<LiveCO> getBatchH5Live(@QueryParam("liveIds") List<Long> lids,Long uid);


    /**
     * 新版直播详情 适用app内的直播详情
     * @param uid 用户id
     * @param lid 直播id
     * @return
     */
    @GET
    @Path("getLiveDetailForApi")
    LiveDetailForApiCO getLiveDetailForApi(Long uid, Long lid);

}
