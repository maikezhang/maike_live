package cn.idongjia.live.api.purelive;

import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.purelive.PureLive;
import cn.idongjia.live.pojo.purelive.PureLive4Article;
import cn.idongjia.live.pojo.purelive.PureLiveDO;
import cn.idongjia.live.pojo.purelive.PureLiveDetailDO;
import cn.idongjia.live.query.purelive.PureLiveSearch;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.*;
import java.util.List;
import java.util.Map;

/**
 * 纯直播对管理后台接口
 */
@Path("pureLive")
@Produces(ContentType.APPLICATION_JSON_UTF_8)
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
public interface PureLiveService {

    /**
     * 创建纯直播
     * @param pureLive 纯直播数据
     * @return 如果创建成功，返回直播lid；否则返回错误信息
     */
    @Path("create")
    @POST
    Long createPureLive(PureLive pureLive);

    /**
     * 更新纯直播数据
     * @param lid 纯直播lid
     * @param pureLive 要更新的直播数据
     * @return 是否更新成功
     */
    @Path("{lid:\\d+}")
    @PUT
    boolean updatePureLiveById(@PathParam("lid") Long lid, PureLive pureLive);

    /**
     * 删除纯直播数据
     * @param lid 纯直播lid
     * @return 是否删除成功
     */
    @Path("{lid:\\d+}")
    @DELETE
    boolean deletePureLiveById(@PathParam("lid") Long lid);

    /**
     * 管理后台根据纯直播lid获取直播详情
     * @param lid 纯直播lid
     * @return 纯直播详情
     */
    @Path("{lid:\\d+}")
    @GET
    PureLive getPureLiveById(@PathParam("lid") Long lid);

    /**
     * 根据搜索条件查询纯直播列表
     * @param pureLiveSearch 搜索条件
     * @return 纯直播列表
     */
    @Path("list")
    @GET
    List<PureLive> listPureLive(@BeanParam PureLiveSearch pureLiveSearch);

    /**
     * 根据搜索条件查询直播数量
     * @param pureLiveSearch 搜索条件
     * @return 纯直播数量
     */
    @Path("count")
    @GET
    Integer getPureLiveCount(@BeanParam PureLiveSearch pureLiveSearch);

    /**
     * 根据检索条件获取直播数量和直播内容
     * @param pureLiveSearch 检索条件
     * @return 数量和内容
     */
    @Path("countlist")
    @GET
    BaseList<PureLive> listPureLiveWithCount(@BeanParam PureLiveSearch pureLiveSearch);

    /**
     * 根据纯直播id获取在线人数
     * @param lid 纯直播id
     * @return 纯直播人数
     */
    @Path("usercount")
    @GET
    Map<String, Object> getPureLiveUserCount(@QueryParam("plid") Long lid);

    /**
     * 修改随机数并立即生效
     */
    @Path("modifyzrc/{plid:\\d+}/zrc/{zrc:\\d+}")
    @POST
    boolean modifyPureLiveZrc(@PathParam("plid") Long plid, @PathParam("zrc") Integer zrc);

    /**
     * App端根据资源id列表获取资源详情列表
     * @param resIds 资源id列表
     * @param resourceType 资源类型
     * @return 资源详情列表
     */
    @Path("resource")
    @POST
    List<Map<String, Object>> getPureLiveResources(List<Long> resIds, Integer resourceType);

    /**
     * 根据id获取纯直播相关信息，To Article
     * @param lid 纯直播ID
     * @return 信息
     */
    @Path("article/{liveId:\\d+}")
    @GET
    PureLive4Article acquirePureLiveData(@PathParam("lid") Long lid);

    /**
     * 根据主播ID查询所有有效的纯直播ID
     * @param uid 主播ID
     * @return 纯直播ID列表
     */
    @Path("uid/{userId:\\d+}")
    @GET
    List<Long> listPureLivesByUid(@PathParam("uid") Long uid);

    /**
     * 根据纯直播ID获取其对应的超级模版ID
     * @param pureLiveId 纯直播ID
     * @return 超级模版ID
     */
    @Path("template/{lid:\\d+}")
    @GET
    Long getTemplateByLid(@PathParam("lid") Long pureLiveId);

    /**
     * 纯直播首页Feeds接口
     * @param pureLiveSearch 检索条件
     * @param uid 用户ID
     * @return feeds数据
     */
    @Path("feeds/{uid:\\d+}")
    @GET
    Map<String, Object> getFeeds(@PathParam("uid") Long uid, @BeanParam PureLiveSearch pureLiveSearch);

    /**
     * 获取分类页面feeds
     * @param tid 标签ID
     * @param pureLiveSearch 检索条件
     * @return feeds流
     */
    @Path("classified/feeds/{tagIds:\\d+}")
    @GET
    List<Object> getClassifiedFeeds(@PathParam("tid") Long tid, @BeanParam PureLiveSearch pureLiveSearch);

    /**
     * 获取订阅中心feeds
     * @param uid 用户ID
     * @param pureLiveSearch 检索条件
     * @return feeds流
     */
    @Path("bookcenter/feeds/{userId:\\d+}")
    @GET
    List<Object> getBookCenterFeeds(@PathParam("userId") Long uid, @BeanParam PureLiveSearch pureLiveSearch);

    /**
     * 根据检索条件获取纯直播预告
     * @param lastDate 最后拉取日期
     * @return 预告信息
     */
    @Path("foreshow/{userId:\\d+}")
    @GET
    List<Map<String, Object>> getForeShows(@PathParam("userId") Long uid, @QueryParam("last") String lastDate);

    /**
     * 根据ID获取纯直播基本信息
     * @param lid 直播ID
     * @return 直播基本信息
     */
    @Path("base/{liveId:\\d+}")
    @GET
    PureLiveDO getBasePureLive(@PathParam("liveId") Long lid);

    /**
     * 根据匠人ID获取直播
     * @param uid 匠人ID
     * @param pureLiveSearch 检索条件
     * @param
     * @return 纯直播列表
     */
    @Path("crafts/userId/{userId:\\d+}")
    @GET
    List<Object> listPureLiveByCraftsUid(@PathParam("userId") Long uid,
                                         @BeanParam PureLiveSearch pureLiveSearch,
                                         boolean isOld);

    /**
     * 根据直播ID获取直播（带锁）
     * @param lid 直播ID
     * @return 纯直播数据
     */
    @Path("lock/{liveId:\\d+}")
    @GET
    PureLive getPureLiveByLidWithLock(@PathParam("liveId") Long lid);

    /**
     * 根据用户ID和直播ID获取直播信息
     * @param uid 用户ID
     * @param lid 直播id
     * @return 直播详情
     */
    @Path("{lid:\\d+}/{userId:\\d+}")
    @GET
    PureLive getPureLiveByLidWithUid(@PathParam("userId") Long uid, @PathParam("lid") Long lid);

    /**
     * 根据直播ID复制聊天室信息
     * @param fromLid 直播ID
     * @param toLid 直播ID
     * @return 是否成功
     */
    @Path("{from:\\d+}/{to:\\d+}")
    @POST
    boolean replicateZooMessage(@PathParam("from") Long fromLid, @PathParam("to") Long toLid);

    /**
     * 获取直播商品资源列表
     * @param liveId 直播id
     * @return 商品资源
     */
    @GET
    @Path("resource/{liveId:\\d+}")
    Map<String, Object> listItemResource(@PathParam("liveId") Long liveId);

    /**
     * 增加直播资源
     * @param liveId 直播id
     * @param resources 直播资源
     * @return 失败原因
     */
    @POST
    @Path("resource/add/{liveId:\\d+}")
    Map<Long, String> addResource(@PathParam("liveId") Long liveId, List<PureLiveDetailDO> resources);

    /**
     * 排序直播资源
     * @param liveId 直播id
     * @param resources 直播资源
     */
    @POST
    @Path("resource/sort/{liveId:\\d+}")
    void reSortResource(@PathParam("liveId") Long liveId, List<PureLiveDetailDO> resources);

    /**
     * 删除直播资源
     * @param liveId 直播id
     * @param resources 直播资源
     */
    @POST
    @Path("resource/delete/{liveId:\\d+}")
    void deleteResource(@PathParam("liveId") Long liveId, List<PureLiveDetailDO> resources);

    /**
     * 直播自动上线
     * @param liveId 直播id
     * @param autoLine 是否自动上线
     */
    @POST
    @Path("auto/online")
    void liveAutoOnline(@PathParam("liveId") Long liveId, @PathParam("autoOnline") Integer autoLine);

    /**
     * 用户商品操作
     * @param userId
     * @param itemId
     * @param type
     */
    void pushUserItemOperation(Long userId, Long itemId, Integer type);
}
