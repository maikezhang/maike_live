package cn.idongjia.live.api.purelive;

import cn.idongjia.common.pagination.Pagination;
import cn.idongjia.live.pojo.purelive.banner.PureLiveBannerDO;
import cn.idongjia.live.query.purelive.banner.PureLiveBannerSearch;
import cn.idongjia.live.pojo.purelive.banner.PureLiveBannerDO;
import cn.idongjia.live.query.purelive.banner.PureLiveBannerSearch;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.BeanParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Created by dongjia_lj on 17/3/9.
 */

@Path("banner")
@Produces({ContentType.APPLICATION_JSON_UTF_8})
public interface PureLiveBannerService {

    @Path("/add")
    void addBanner(PureLiveBannerDO pureLiveBannerDO);

    @PUT
    @Path("/{bid:\\+}")
    void updateBanner(@PathParam("bid") Long bid, PureLiveBannerDO pureLiveBannerDO);

    @DELETE
    @Path("/{bid:\\d+}")
    void deleteBanner(@PathParam("bid") Long bid);

    @GET
    @Path("/page")
    Pagination queryBannerByPage(@BeanParam PureLiveBannerSearch search);

    @GET
    List<PureLiveBannerDO> queryBannerList(@BeanParam PureLiveBannerSearch search);

    @GET
    @Path("/count")
    int countBanner(@BeanParam PureLiveBannerSearch search);
}
