package cn.idongjia.live.api.homePage;

import cn.idongjia.live.api.live.pojo.LiveSearchApiResp;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.homePage.LiveHomePageCO;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.*;
import java.util.List;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/7/20
 * Time: 上午9:54
 */
@Path("liveHomePage")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public interface LiveHomePageServiceI {


    /**
     * 直播首页基本数据  banner  module  tab
     * @param isNewVersion 是否为新版本版本
     * @return
     */
    @GET
    @Path("getHomePageBase/{isNewVersion}")
    LiveHomePageCO getHomePageBase(@PathParam("isNewVersion") boolean isNewVersion);

    @GET
    @Path("search/{queryStr}/{page:\\d+}/{limit:\\d+}")
    BaseList<LiveSearchApiResp> searchLive(@PathParam("queryStr") String queryStr, @PathParam("page")Integer page, @PathParam("limit")Integer limit);

}
