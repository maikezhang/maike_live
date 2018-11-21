package cn.idongjia.live.db.mybatis.mapper;

import cn.idongjia.live.db.mybatis.po.LiveReportPO;
import cn.idongjia.live.db.mybatis.query.DBLiveReportQuery;
import cn.idongjia.live.pojo.live.LiveReport;
import cn.idongjia.live.query.live.LiveReportSearch;

import java.util.List;

public interface LiveReportMapper {

    int insert(LiveReportPO liveReportPO);

    int delete(long lrid);

    List<LiveReportPO> liveReports(DBLiveReportQuery query);

    int countLiveReport(DBLiveReportQuery query);
}
