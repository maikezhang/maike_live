package cn.idongjia.live.api.live;

import cn.idongjia.live.pojo.live.LiveBanner;
import cn.idongjia.live.query.live.LiveBannerSearch;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

/**
 *
 * @author dongjia_lj
 */

@Path("banner")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public interface BannerService {

    /**
     * 创建banner
     * @param liveBanner
     * @return
     */
    @Path("/add")
    @POST
    Long createBanner(LiveBanner liveBanner);

    @Path("/{bid:\\d+}")
    @PUT
    void updateBanner(@PathParam("bid") long bid, LiveBanner liveBanner);

    @Path("/{bid:\\d+}")
    @DELETE
    boolean deleteBanner(@PathParam("bid") long bid);

    @Path("list")
    @GET
    List<LiveBanner> list(@BeanParam LiveBannerSearch search);

    @Path("count")
    @GET
    int count(@BeanParam LiveBannerSearch search);
}
