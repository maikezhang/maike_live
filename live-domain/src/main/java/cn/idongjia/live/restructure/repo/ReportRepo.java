package cn.idongjia.live.restructure.repo;

import cn.idongjia.live.db.mybatis.mapper.LiveReportMapper;
import cn.idongjia.live.db.mybatis.po.LiveReportPO;
import cn.idongjia.live.db.mybatis.query.DBLiveReportQuery;
import cn.idongjia.live.restructure.dto.LiveReportDTO;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/26
 * Time: 下午6:43
 */
@Component
public class ReportRepo {

    @Resource
    private LiveReportMapper liveReportMapper;


    public int addReport(LiveReportDTO dto){
        LiveReportPO po=dto.toDO();
        po.setUpdateTime(Utils.getCurrentMillis());
        po.setCreateTime(Utils.getCurrentMillis());
        return liveReportMapper.insert(po);

    }


    public List<LiveReportDTO>  list(DBLiveReportQuery query){
        List<LiveReportPO> pos=liveReportMapper.liveReports(query);
        return pos.stream().map(LiveReportDTO::new).collect(Collectors.toList());
    }

    public int count(DBLiveReportQuery query){
        return liveReportMapper.countLiveReport(query);
    }
}
