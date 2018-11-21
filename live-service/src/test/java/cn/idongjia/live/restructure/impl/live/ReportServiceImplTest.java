package cn.idongjia.live.restructure.impl.live;

import cn.idongjia.live.api.live.ReportService;
import cn.idongjia.live.pojo.live.LiveReport;
import cn.idongjia.live.query.live.LiveReportSearch;
import cn.idongjia.live.restructure.SpringJUnitNoRollbackTest;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by zhangmaike on 2018/6/26.
 */
public class ReportServiceImplTest extends SpringJUnitNoRollbackTest {

    @Resource
    private ReportService reportService;
    private static final Log LOGGER= LogFactory.getLog(ReportServiceImplTest.class);
    @Test
    public void getAddress() throws Exception {
        Map<String, Object> address = reportService.getAddress();
        LOGGER.info(address.get("play_addr")+" "+address.get("report_addr"));

    }

    @Test
    @Rollback(false)
    public void addLiveReport() throws Exception {
        LiveReport report=new LiveReport();
        report.setType(1);
        report.setLid(2102);
        report.setReportUid(765971L);
        report.setContent("直播中含有不正当内容");
        report.setDesc("你猜啊");
        report.setStatus(0);
        reportService.addLiveReport(report);


    }

    @Test
    public void getLiveReports() throws Exception {
        LiveReportSearch search=new LiveReportSearch();
        search.setContent("直播");
        LOGGER.info("count:{},items:{}",reportService.countLiveReport(search),reportService.getLiveReports(search));

    }

    @Test
    public void countLiveReport() throws Exception {
    }

}