package cn.idongjia.live.restructure.impl.live;

import cn.idongjia.live.api.live.ReportService;
import cn.idongjia.live.pojo.live.LiveReport;
import cn.idongjia.live.query.live.LiveReportSearch;
import cn.idongjia.live.restructure.biz.ReportBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component("reportServiceImpl")
public class ReportServiceImpl implements ReportService {

    @Resource
    private ReportBO reportBO;

    @Override
    public Map<String, Object> getAddress() {
        return reportBO.getAddress();
    }

    @Override
    public int addLiveReport(LiveReport liveReport) {
        return reportBO.addLiveReport(liveReport);
    }

    @Override
    public List<LiveReport> getLiveReports(LiveReportSearch search) {
        return reportBO.reportList(search);
    }

    @Override
    public int countLiveReport(LiveReportSearch search) {
        return reportBO.countLiveReport(search);
    }
}
