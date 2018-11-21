package cn.idongjia.live.api.purelive;

import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.purelive.PureLive;
import cn.idongjia.live.pojo.purelive.audit.PureLiveAudit;
import cn.idongjia.live.pojo.purelive.audit.PureLiveAuditDO;
import cn.idongjia.live.query.purelive.PureLiveSearch;
import cn.idongjia.live.query.purelive.audit.PureLiveAuditSearch;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.*;
import java.util.List;

@Path("audit")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public interface PureLiveAuditService {

    /**
     * 新增审核信息
     * @param pureLiveAuditDO 审核信息
     * @return 审核ID
     */
    @POST
    Long addPureLiveAudit(PureLiveAuditDO pureLiveAuditDO);

    /**
     * 删除一条审核信息
     * @param aid 审核信息ID
     * @return 是否成功
     */
    @Path("{aid:\\d+}")
    @DELETE
    boolean deletePureLiveAudit(@PathParam("aid") Long aid);

    /**
     * 更新一条审核信息
     * @param aid 审核信息ID
     * @param pureLiveAuditDO 审核信息
     * @return 是否成功
     */
    @Path("{aid:\\d+}")
    @PUT
    boolean updatePureLiveAudit(@PathParam("aid") Long aid, PureLiveAuditDO pureLiveAuditDO);

    /**
     * 列出符合条件的审核信息
     * @param pureLiveAuditSearch 检索条件
     * @return 审核信息列表
     */
    @GET
    BaseList<PureLiveAuditDO> listPureLiveAudits(PureLiveAuditSearch pureLiveAuditSearch);

    /**
     * 获取符合条件审核信息列表
     * @param pureLiveAuditSearch 检索条件
     * @return 审核信息数量
     */
    @Path("count")
    @GET
    Integer countPureLiveAudits(@BeanParam PureLiveAuditSearch pureLiveAuditSearch);

    /**
     * 结束审核
     * @param pureLiveAuditDO 审核数据
     * @return 是否通过审核
     */
    @Path("finish")
    @POST
    boolean finishPureLiveAudit(PureLiveAuditDO pureLiveAuditDO);

    /**
     * 审核页面获取直播列表
     * @param pureLiveSearch 纯直播检索条件
     * @return 直播列表
     */
    @Path("list")
    @GET
    List<PureLive> listPureLivesAudit(@BeanParam PureLiveSearch pureLiveSearch);

    /**
     * 审核页面获取直播数量
     * @param pureLiveSearch 纯直播检索条件
     * @return 直播数量
     */
    @Path("listcount")
    @GET
    Integer countPureLiveAudit(@BeanParam PureLiveSearch pureLiveSearch);

    /**
     * 审核页面获取直播信息
     * @param lid 直播ID
     * @return 直播信息
     */
    @Path("lid/{uid:\\d+}/{liveId:\\d+}")
    @GET
    PureLive getPureLiveAudit(@PathParam("uid") Long uid, @PathParam("lid") Long lid);

    /**
     * 根据直播ID获取审核信息
     * @param lid 直播ID
     * @return 审核信息
     */
    @Path("lid/{liveId:\\d+}")
    @GET
    PureLiveAudit getAuditInfoByLid(@PathParam("lid") Long lid);

    /**
     * 获取数量和内容合集
     * @param pureLiveSearch 检索条件
     * @return 合集
     */
    @Path("listcount")
    @GET
    BaseList<PureLive> listPureLivesAuditWithCount(PureLiveSearch pureLiveSearch);
}
