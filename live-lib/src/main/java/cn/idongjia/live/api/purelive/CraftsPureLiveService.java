package cn.idongjia.live.api.purelive;

import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.purelive.PureLive;
import cn.idongjia.live.pojo.purelive.PureLiveDetailDO;
import cn.idongjia.live.query.purelive.PureLiveSearch;
import cn.idongjia.live.v2.pojo.ItemResource;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.*;
import java.util.List;
import java.util.Map;

/**
 * 匠人PC端接口
 * @author zhang
 */
@Path("crafts")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public interface CraftsPureLiveService {

    /**
     * 匠人创建纯直播接口
     * @param pureLive 创建数据
     * @return 纯直播ID
     */
    @POST
    Long createCraftsPureLive(PureLive pureLive);

    /**
     * 匠人更新纯直播接口
     * @param pureLiveId 更新ID
     * @param pureLive 更新数据
     * @return 是否更新成功
     */
    @Path("{lid:\\d+}")
    @PUT
    boolean updateCraftsPureLive(@PathParam("lid") Long pureLiveId, PureLive pureLive);

    /**
     * 匠人删除纯直播接口
     * @param pureLiveId 纯直播ID
     * @return 是否删除成功
     */
    @Path("{lid:\\d+}")
    @DELETE
    boolean deleteCraftsPureLive(@PathParam("lid") Long pureLiveId);

    /**
     * 根据主播ID获取其直播
     * @param uid 主播ID
     * @param pureLiveSearch 检索条件
     * @return 纯直播列表
     */
    @Path("uid/{uid:\\d+}")
    @GET
    BaseList<PureLive> listCraftsPureLives(@PathParam("uid") Long uid
            , @BeanParam PureLiveSearch pureLiveSearch);

    /**
     * 获取匠人创建纯直播信息
     * @param pureLiveId 纯直播ID
     * @return 纯直播信息
     */
    @Path("{lid:\\d+}")
    @GET
    PureLive getCraftsPureLive(@PathParam("lid") Long pureLiveId);

    /**
     * 根据主播ID获取其直播数量
     * @param uid 主播ID
     * @param pureLiveSearch 检索条件
     * @return 直播数量
     */
    @Path("count/{uid:\\d+}")
    @GET
    Integer countCraftsPureLives(@PathParam("uid") Long uid
            , @BeanParam PureLiveSearch pureLiveSearch);

    /**
     * 设置主播在匠人后台修改主播时状态为修改中
     * @param lid 主播ID
     * @return 是否成功
     */
    @Path("update/{lid:\\d+}")
    @POST
    boolean setPureLiveUpdate(@PathParam("lid") Long lid);

    /**
     * 获取直播商品资源列表
     * @param liveId 直播id
     * @return 资源列表
     */
    @Path("resource/{liveId:\\d+}")
    @GET
    List<ItemResource> listItemResource(@PathParam("liveId") Long liveId);

    /**
     * 关联直播商品资源
     * @param liveId 直播id
     * @param resources 直播资源
     */
    @POST
    @Path("resource/{liveId:\\d+}")
    Map<Long, String> addItemResource(@PathParam("liveId") Long liveId, List<PureLiveDetailDO> resources);

    /**
     * 删除直播商品资源
     * @param liveId 直播id
     * @param resources 直播资源列表
     */
    @DELETE
    @Path("resource/{liveId:\\d+}")
    void deleteItemResource(@PathParam("liveId") Long liveId, List<PureLiveDetailDO> resources);

    /**
     * 管理直播商品资源
     * @param liveId 直播id
     * @param resources 直播资源列表
     */
    @PUT
    @Path("resource/{liveId:\\d+}")
    void manageItemResource(@PathParam("liveId") Long liveId, List<PureLiveDetailDO> resources);
}
