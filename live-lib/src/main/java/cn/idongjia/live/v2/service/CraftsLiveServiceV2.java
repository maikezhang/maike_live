package cn.idongjia.live.v2.service;

import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.query.LiveSearch;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.query.LiveSearch;
import cn.idongjia.live.v2.pojo.query.CraftsLivePPUrl;
import cn.idongjia.live.v2.pojo.query.ResourceSearch;
import cn.idongjia.live.v2.pojo.*;
import cn.idongjia.live.v2.pojo.query.CraftsLivePPUrl;
import cn.idongjia.live.v2.pojo.query.ResourceSearch;

import javax.ws.rs.*;
import java.util.List;
import java.util.Map;

/**
 * 匠人端直播接口
 *
 * @author zhang created on 2018/1/17 下午12:51
 * @version 1.0
 */
@Path("crafts/live")
public interface CraftsLiveServiceV2 {

    /**
     * 匠人端创建直播
     *
     * @param craftsLive 直播详情
     * @return 直播id
     */
    @POST
    Long craftsLiveAdd(CraftsLive craftsLive);

    /**
     * 匠人端修改直播
     *
     * @param craftsLive 直播
     */
    @PUT
    Long craftsLiveUpdate(CraftsLive craftsLive);

    /**
     * 关联直播资源接口
     *
     * @param liveId    直播id
     * @param resources 资源
     * @return 失败原因
     */
    @POST
    @Path("resource/gather/{liveId:\\d+}")
    Integer craftsLiveResourceManage(@PathParam("liveId") Long liveId, List<LiveResource> resources);

    /**
     * 重排直播资源列表
     *
     * @param liveId    直播id
     * @param resources 直播资源
     */
    @POST
    @Path("resource/resort/{liveId:\\d+}")
    Integer craftsLiveResourceReSort(@PathParam("liveId") Long liveId, List<LiveResource> resources);

    /**
     * 未开始直播列表
     *
     * @param search 分页参数，主播id
     * @return
     */
    @POST
    @Path("unstartList")
    BaseList<CraftsLive4List> getUnstartList(LiveSearch search);

    /**
     * 已结束直播列表
     *
     * @param search 分页参数，主播id
     * @return
     */
    @POST
    @Path("/endList")
    BaseList<CraftsLive4List> getEndList(LiveSearch search);

    /**
     * 获取主播进行中的直播
     *
     * @param uid
     * @return
     */
    @GET
    @Path("inProgress/{userId:\\d+}")
    CraftsLive getInProgressLiveByUid(@PathParam("userId") Long uid);

    /**
     * 获取直播详情
     *
     * @param id  直播id，必填
     * @param uid 主播uid， 主要用户获取主播下一场直播时间，非必填
     * @return
     */
    @GET
    @Path("detail/{id:\\d+}/{userId:\\d+}")
    CraftsLiveDetail getLiveDetail(@PathParam("id") Long id, @PathParam("userId") Long uid);

    /**
     * 获取直播资源列表
     *
     * @param liveId 直播id
     * @return 直播商品资源
     */
    @GET
    @Path("resource/item/{liveId:\\d+}")
    List<ItemResource> listItemResource(@PathParam("liveId") Long liveId);

    /**
     * 设置直播自动上线功能
     *
     * @param liveId     直播id
     * @param autoOnline 直播是否自动上线
     */
    @POST
    @Path("auto/online/{liveId:\\d+}/{online:\\d+}")
    void liveAutoOnline(@PathParam("liveId") Long liveId, Integer autoOnline);

    /**
     * 获取匠人作品（商品）列表
     *
     * @param search
     */
    @POST
    @Path("items")
    ItemResourcePackage listCraftsItems(ResourceSearch search);

    @POST
    @Path("selectedItems/{liveId:\\d+}")
    List<ItemResource> getSelectedItems(@PathParam("liveId") Long liveId);

    /**
     * 获取匠人直播
     *
     * @param liveId 直播id
     * @return 匠人直播
     */
    @GET
    @Path("{liveId:\\d+}")
    CraftsLive getCraftsLive(@PathParam("liveId") Long liveId);

    /**
     * 服务电话
     *
     * @return
     */
    @GET
    @Path("service")
    Map<String, String> getServicePhoneNumber();

    /**
     * 获取推拉流地址
     *
     * @param liveId 直播id
     * @return
     */
    @GET
    @Path("ppurls/{liveId:\\d+}")
    CraftsLivePPUrl getPushPullUrl(@PathParam("liveId") Long liveId);

    /**
     * 获取直播的一些状态 status state
     * @param liveId
     * @return
     */
    @GET
    @Path("state/{liveId:\\d+}")
    CraftsLive getLiveStatus(@PathParam("liveId") Long liveId);

}
