package cn.idongjia.live.api.live;

import cn.idongjia.live.pojo.live.LiveReport;
import cn.idongjia.live.query.live.LiveReportSearch;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.*;
import java.util.List;
import java.util.Map;

@Path("report")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public interface ReportService {
    /**
     * 获取举报和玩法的h5地址
     * @return
     */
    @GET
    @Path("getH5Address")
    Map<String,Object> getAddress();

    /**
     * 添加举报
     * @param liveReport
     * @return
     */
    @POST
    @Path("addReport")
    int addLiveReport(LiveReport liveReport);

    /**
     * 获取举报的列表
     * @param search
     * @return
     */
    @GET
    @Path("reports")
    List<LiveReport> getLiveReports(@BeanParam LiveReportSearch search);

    /**
     * count
     * @param search
     * @return
     */
    @GET
    @Path("count")
    int countLiveReport(@BeanParam LiveReportSearch search);
}
