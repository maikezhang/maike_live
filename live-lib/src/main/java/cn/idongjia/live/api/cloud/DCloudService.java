package cn.idongjia.live.api.cloud;

import cn.idongjia.live.pojo.live.LivePullUrl;
import cn.idongjia.live.query.cloud.DLiveCloudSearch;
import org.springframework.stereotype.Component;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.Map;

@Component
@Path("cloud")
public interface DCloudService {

    @Path("modify/{userId:\\d+}")
    @POST
    boolean modifyDCloud(@PathParam("uid") Long uid, LivePullUrl livePullUrl);

    @Path("uid/{userId:\\d+}")
    @GET
    LivePullUrl getPullUrlByUid(@PathParam("uid") Long uid);

    @Path("/")
    @GET
    List<Map<String, Object>> listAllDLiveCloud(@BeanParam DLiveCloudSearch dLiveCloudSearch);
}
