package cn.idongjia.live.api.purelive;

import cn.idongjia.live.pojo.purelive.book.AnchorsBookDO;
import cn.idongjia.live.pojo.purelive.book.PureLiveBookDO;
import cn.idongjia.live.pojo.user.Anchor;
import cn.idongjia.live.query.purelive.book.AnchorsBookSearch;
import cn.idongjia.live.query.purelive.book.PureLiveBookSearch;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.*;
import java.util.List;
import java.util.Map;

@Path("book")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public interface PureLiveBookService {

    /**
     * 增加用户订阅直播信息
     * @param pureLiveBookDO 订阅信息
     * @return 订阅ID
     */
    @Path("live")
    @POST
    Long addPureLiveBook(PureLiveBookDO pureLiveBookDO);

    /**
     * 删除用户直播订阅信息
     * @param pureLiveBookDO 订阅信息
     * @return 是否成功
     */
    @Path("live")
    @DELETE
    boolean deletePureLiveBook(PureLiveBookDO pureLiveBookDO);

    /**
     * 更新用户直播订阅信息
     * @param pureLiveBookDO 订阅信息
     * @return 是否成功
     */
    @Path("live")
    @PUT
    boolean updatePureLiveBook(PureLiveBookDO pureLiveBookDO);

    /**
     * 获取订阅列表
     * @param pureLiveBookSearch 检索条件
     * @return 订阅信息列表
     */
    @Path("live")
    @GET
    List<PureLiveBookDO> listPureLiveBooks(@BeanParam PureLiveBookSearch pureLiveBookSearch);

    /**
     * 获取订阅信息数量
     * @param pureLiveBookSearch 检索条件
     * @return 订阅信息数量
     */
    @Path("live/count")
    @GET
    Integer countPureLiveBooks(@BeanParam PureLiveBookSearch pureLiveBookSearch);

    /**
     * 增加订阅匠人信息
     * @param anchorsBookDO 订阅信息
     * @return 订阅ID
     */
    @Path("anchors")
    @POST
    Long addAnchorsBook(AnchorsBookDO anchorsBookDO);

    /**
     * 删除订阅匠人信息
     * @param anchorsBookDO 订阅信息
     * @return 是否成功
     */
    @Path("anchors")
    @DELETE
    boolean deleteAnchorsBook(AnchorsBookDO anchorsBookDO);

    /**
     * 更新订阅匠人信息
     * @param anchorsBookDO 订阅信息
     * @return 是否成功
     */
    @Path("anchors")
    @PUT
    boolean updateAnchorsBook(AnchorsBookDO anchorsBookDO);

    /**
     * 获取订阅匠人列表
     * @param anchorsBookSearch 检索信息
     * @return 订阅列表
     */
    @Path("anchors")
    @GET
    List<AnchorsBookDO> listAnchorsBooks(@BeanParam AnchorsBookSearch anchorsBookSearch);

    /**
     * 获取订阅匠人信息数量
     * @param anchorsBookSearch 检索信息
     * @return 订阅信息数量
     */
    @Path("anchors/count")
    @GET
    Integer countAnchorsBooks(@BeanParam AnchorsBookSearch anchorsBookSearch);

    /**
     * 根据匠人ID批量增加用户订阅纯直播关系
     * @param cuid 匠人ID
     * @param lid 纯直播ID
     * @return 是否成功
     */
    @Path("batch/{cuid:\\d+}/{liveId:\\d+}")
    @POST
    boolean batchAddPureLiveBook(@PathParam("cuid") Long cuid, @PathParam("lid") Long lid);

    /**
     * 根据匠人ID批量删除用户订阅直播关系
     * @param cuid 匠人ID
     * @param lid 直播ID
     * @return 是否成功
     */
    @Path("batch/{cuid:\\d+}/{liveId:\\d+}")
    @DELETE
    boolean batchRemovePureLiveBook(@PathParam("cuid") Long cuid, @PathParam("lid") Long lid);

    /**
     * 根据用户ID查询所关注的直播
     * @param uid 用户uid
     * @param pureLiveBookSearch 检索条件
     * @return 直播信息列表
     */
    @Path("uid/{userId:\\d+}")
    @GET
    List<Long> listPureLivesByUid(@PathParam("uid") Long uid
            , @BeanParam PureLiveBookSearch pureLiveBookSearch);

    /**
     * 根据直播ID查询所有订阅的用户ID
     * @param lid 直播ID
     * @return 用户ID列表
     */
    @Path("lid/{liveId:\\d+}")
    @GET
    List<Long> listBookUidsByLid(@PathParam("lid") Long lid);

    /**
     * 根据用户ID查询其所有订阅主播信息
     * @param uid 用户ID
     * @param anchorsBookSearch 检索条件
     * @return 主播信息
     */
    @Path("anchor/{userId:\\d+}")
    @GET
    List<Anchor> listAnchorsByUid(@PathParam("uid") Long uid
            , @BeanParam AnchorsBookSearch anchorsBookSearch);

    /**
     * 根据用户ID获取订阅直播数量
     * @param uid 用户ID
     * @return 数量
     */
    @Path("live/count/{userId:\\d+}")
    @GET
    Integer countPureLivesByUid(@PathParam("uid") Long uid);

    /**
     * 根据用户ID获取订阅匠人数量
     * @param uid 用户ID
     * @return 数量
     */
    @Path("anchor/count/{userId:\\d+}")
    @GET
    Integer countAnchorsByUid(@PathParam("uid") Long uid);

    /**
     * 根据用户ID获取订阅数量
     * @param uid 用户ID
     * @return 数量
     */
    @Path("count/{userId:\\d+}")
    @GET
    Map<String, Object> countBooksByUid(@PathParam("uid") Long uid);

    /**
     * 根据匠人ID和用户ID获取匠人中心提醒文案
     * @param uid 用户ID
     * @param anchorId 主播ID
     * @return 提醒文案
     */
    @Path("remind/{uid:\\d+}/{anchorId:\\d+}")
    @GET
    Map<String, String> getLiveRemindTxt(@PathParam("uid") Long uid, @PathParam("anchorId") Long anchorId);

}
