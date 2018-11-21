package cn.idongjia.live.api;

import cn.idongjia.live.api.live.pojo.response.MultiResponse;
import cn.idongjia.live.api.live.pojo.response.SingleResponse;
import cn.idongjia.live.restructure.pojo.cmd.UserStageAddCmd;
import cn.idongjia.live.restructure.pojo.cmd.UserStageDelCmd;
import cn.idongjia.live.restructure.pojo.cmd.UserStageUpdateCmd;
import cn.idongjia.live.restructure.pojo.co.UserStageLiveCO;
import cn.idongjia.live.restructure.pojo.co.UserStageLiveDetailCO;
import cn.idongjia.live.restructure.pojo.query.ESUserStageLiveQry;
import cn.idongjia.live.restructure.pojo.query.UserStageLiveQry;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author lc on 2018/7/7.
 * @class cn.idongjia.live.api.UserStageServiceI
 */
@Path("userStage")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public interface UserStageLiveServiceI {
    /**
     * 添加用户强运营直播数据
     *
     * @param userStageAddCmd
     * @return id
     * @see cn.idongjia.live.restructure.pojo.cmd.UserStageAddCmd
     */
    @Path("add")
    @POST
    SingleResponse<Integer> add(@BeanParam UserStageAddCmd userStageAddCmd);

    /**
     * 删除强运营数据
     *
     * @param userStageDelCmd
     * @return
     */
    @Path("delete")
    @DELETE
    SingleResponse<Integer> delete(@BeanParam UserStageDelCmd userStageDelCmd);

    /**
     * 获取新老用户强运营 <br/> 本接口过滤已结束直播
     * 默认每页响应100条数据
     *
     * @param esUserStageLiveQry 当前页数 默认请传1
     * @return SingleResponse<UserStageLive>
     * @see cn.idongjia.live.restructure.pojo.co.UserStageLiveCO
     */
    @Path("searchWithUserStage")
    @GET
    SingleResponse<UserStageLiveCO> searchWithUserStage(@BeanParam ESUserStageLiveQry esUserStageLiveQry);


    /**
     * 分页查询强运营数据
     *
     * @param userStageLiveQry 查询条件 目前只有类型 和分页数据
     * @return
     */
    MultiResponse<UserStageLiveDetailCO> page(@BeanParam UserStageLiveQry userStageLiveQry);


    /**
     * 更新接口
     * @param userStageUpdateCmd 更新参数
     * @return  SingleResponse<Integer>
     */
    @POST
    @Path("update")
    SingleResponse<Integer> update(@BeanParam UserStageUpdateCmd userStageUpdateCmd);

}
