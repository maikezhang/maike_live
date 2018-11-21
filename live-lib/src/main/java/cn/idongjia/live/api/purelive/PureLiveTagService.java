package cn.idongjia.live.api.purelive;

import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.purelive.tag.ColumnTag;
import cn.idongjia.live.pojo.purelive.tag.PureLiveTagDO;
import cn.idongjia.live.pojo.purelive.tag.PureLiveTagRelDO;
import cn.idongjia.live.pojo.purelive.tag.TemplateTagRelDO;
import cn.idongjia.live.query.purelive.tag.PureLiveTagRelSearch;
import cn.idongjia.live.query.purelive.tag.PureLiveTagSearch;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.*;
import java.util.List;
import java.util.Map;

@Path("tag")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public interface PureLiveTagService {

    /**
     * 增加纯直播标签
     * @param pureLiveTagDO 标签信息
     * @return 标签ID
     */
    @POST
    Long addPureLiveTag(PureLiveTagDO pureLiveTagDO);

    /**
     * 删除标签
     * @param tagId 标签ID
     * @return 是否成功
     */
    @Path("{tid:\\d+}")
    @DELETE
    boolean deletePureLiveTag(@PathParam("tid") Long tagId);

    /**
     * 更新标签信息
     * @param tagId 标签ID
     * @param pureLiveTagDO 标签信息
     * @return 是否成功
     */
    @Path("{tid:\\d+}")
    @PUT
    boolean updatePureLiveTag(@PathParam("tid") Long tagId, PureLiveTagDO pureLiveTagDO);

    /**
     * 根据ID获取标签信息
     * @param tagId 标签ID
     * @return 标签信息
     */
    @Path("{tid:\\d+}")
    @GET
    PureLiveTagDO getPureLiveTag(@PathParam("tid") Long tagId);

    /**
     * 获取标签列表
     * @param pureLiveTagSearch 检索条件
     * @return 标签列表
     */
    @GET
    List<PureLiveTagDO> listPureLiveTags(@BeanParam PureLiveTagSearch pureLiveTagSearch);

    /**
     * 标签数量
     * @param pureLiveTagSearch 检索条件
     * @return 标签数量
     */
    @Path("count")
    @GET
    Integer countPureLiveTags(@BeanParam PureLiveTagSearch pureLiveTagSearch);

    /**
     * 根据检索条件获取数量和内容
     * @param tagSearch 检索条件
     * @return 数量和内容
     */
    @Path("countlist")
    @GET
    BaseList<PureLiveTagDO> listPureLiveTagsWithCount(@BeanParam PureLiveTagSearch tagSearch);

    /**
     * 根据检索条件获取已经关联内容的tag信息
     * @param tagSearch 检索条件
     * @return tag列表
     */
    @Path("haveRels")
    @GET
    List<PureLiveTagDO> listPureLiveTagWithRels(@BeanParam PureLiveTagSearch tagSearch);

    /**
     * 增加纯直播标签关联关系
     * @param pureLiveTagRelDO 关联关系信息
     * @return 关联信息ID
     */
    @Path("rel")
    @POST
    Long addPureLiveTagRel(PureLiveTagRelDO pureLiveTagRelDO);

    /**
     * 更新纯直播标签关联关系
     * @param pureLiveTagRelDO 关联关系信息
     * @return 关联信息ID
     */
    @Path("rel")
    @PUT
    boolean updatePureLiveTagRel(PureLiveTagRelDO pureLiveTagRelDO);

    /**
     * 删除纯直播标签关联关系
     * @param pureLiveTagRelDO 关联关系信息
     * @return 关联信息ID
     */
    @Path("rel")
    @DELETE
    boolean deletePureLiveTagRel(PureLiveTagRelDO pureLiveTagRelDO);

    /**
     * 标签-直播关联关系
     * @param pureLiveTagRelSearch 检索条件
     * @return 关联列表
     */
    @Path("rel")
    @GET
    List<PureLiveTagRelDO> listPureLiveTagRels(@BeanParam PureLiveTagRelSearch pureLiveTagRelSearch);

    /**
     * 关联数量
     * @param pureLiveTagRelSearch 检索条件
     * @return 关联数量
     */
    @Path("rel/count")
    @GET
    Integer countPureLiveTagRels(@BeanParam PureLiveTagRelSearch pureLiveTagRelSearch);

    /**
     * 根据检索条件获取数量和内容
     * @param tagRelSearch 检索条件
     * @return 数量和内容
     */
    @Path("rel/countlist")
    @GET
    BaseList<PureLiveTagRelDO> listPureLiveTagRelsWithCount(@BeanParam PureLiveTagRelSearch tagRelSearch);

    /**
     * 增加tag关联超级模版信息
     * @param tagRelDO 关联信息
     * @return 关联ID
     */
    @Path("rel/template")
    @POST
    Long addTemplateTagRel(TemplateTagRelDO tagRelDO);

    /**
     * 删除tag对超级模版关联信息
     * @param tagRelDO 关联信息
     * @return 是否成功
     */
    @Path("rel/template")
    @DELETE
    boolean deleteTemplateTagRel(TemplateTagRelDO tagRelDO);

    /**
     * 更新tag对超级模版关联信息
     * @param tagRelDO 关联信息
     * @return 是否成功
     */
    @Path("rel/template")
    @PUT
    boolean updateTemplateTagRel(TemplateTagRelDO tagRelDO);

    /**
     * 根据tagID获取关联关系
     * @param tagId tagID
     * @return 关联信息
     */
    @Path("rel/template/{tagId:\\d+}")
    @GET
    TemplateTagRelDO getTemplateTagRel(@PathParam("tagId") Long tagId);

    /**
     * 根据tag ID获取关联的直播
     * @param tagId tag ID
     * @return 直播信息
     */
    @Path("rel/live/{tagId:\\d+}")
    @GET
    List<Long> listPureLivesByTagId(@PathParam("tagId") Long tagId
            , @BeanParam PureLiveTagRelSearch pureLiveTagRelSearch);

    /**
     * 根据tag ID获取关联的超级模版
     * @param tagId tag ID
     * @return 超级模版信息
     */
    @Path("rel/template/{tagId:\\d+}")
    Map<String, String> listTemplatesByTagId(@PathParam("tagId") Long tagId);

    /**
     * 根据纯直播ID获取其关联的分类信息
     * @param lid 直播ID
     * @return 分类信息列表
     */
    @Path("lid/{liveId://d+}")
    @GET
    List<PureLiveTagRelDO> listTypeTagRelsiByLid(@PathParam("lid") Long lid);

    /**
     * 根据栏目tag ID获取对应的信息
     * @param tid tag ID
     * @return 栏目信息
     */
    @Path("column/{tagIds:\\d+}")
    @GET
    ColumnTag getColumnTagById(@PathParam("tid") Long tid);
}
