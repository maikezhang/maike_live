package cn.idongjia.live.api.live;

import cn.idongjia.live.api.live.pojo.response.MultiResponse;
import cn.idongjia.live.restructure.pojo.co.live.LiveMPCO;
import cn.idongjia.live.restructure.pojo.co.live.LiveMPDetailCO;
import cn.idongjia.live.restructure.pojo.co.live.LiveMPFormIdCO;
import cn.idongjia.live.restructure.pojo.query.LiveMPQry;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.*;
import java.util.List;

/**
 * Created by zhangmaike on 2018/9/4.
 */
@Path("liveMP")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public interface LiveMPServiceI {

    @GET
    @Path("list")
    MultiResponse<LiveMPCO> list(@BeanParam LiveMPQry qry);

    @POST
    @Path("updateWeight/{liveId:\\d+}/{weight:\\d+}")
    boolean updateWeight(@PathParam("liveId") Long liveId,@PathParam("weight") Integer weight);

    @GET
    @Path("pageApi")
    List<LiveMPCO> mpPageApi(@BeanParam LiveMPQry qry);


    @GET
    @Path("detail/{liveId:\\d+}/{userId:\\d+}")
    LiveMPDetailCO getDetail(@PathParam("liveId") Long liveId,@PathParam("userId") Long userId);

    @POST
    @Path("collectFormId")
    boolean collectFormId(LiveMPFormIdCO liveMPFormIdCO);
}
