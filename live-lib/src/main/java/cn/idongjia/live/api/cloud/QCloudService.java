package cn.idongjia.live.api.cloud;

import cn.idongjia.live.pojo.live.LiveCloudStat;
import cn.idongjia.live.pojo.live.LiveCloudStat;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Map;

@Path("qcloud")
public interface QCloudService {

    @Path("callback")
    @POST
    @Consumes(ContentType.APPLICATION_JSON_UTF_8)
    Map<String, Integer> qCloudCallBack(Map<String, Object> callBackMsg);

    @Path("static/{lsid:\\d+}")
    @GET
    @Consumes(ContentType.APPLICATION_JSON_UTF_8)
    LiveCloudStat getLiveFlowStatic(@PathParam("lsid") Long lsid);
}
