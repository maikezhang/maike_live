package cn.idongjia.live.restructure.biz;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.live.db.mybatis.po.LiveReportPO;
import cn.idongjia.live.db.mybatis.query.DBLiveReportQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveShowQuery;
import cn.idongjia.live.pojo.live.LiveReport;
import cn.idongjia.live.query.live.LiveReportSearch;
import cn.idongjia.live.restructure.dto.LiveReportDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.restructure.query.LiveShowQueryHandler;
import cn.idongjia.live.restructure.repo.ReportRepo;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/26
 * Time: 上午11:17
 */
@Component
public class ReportBO {

    @Resource
    private LiveShowQueryHandler liveShowQueryHandler;

    private static final Log LOGGER= LogFactory.getLog(ReportBO.class);


    @Resource
    private ConfigManager configManager;

    @Resource
    private UserManager userManager;

    @Resource
    private ReportRepo reportRepo;

    /**
     * 获取举报和玩法的h5地址
     * @return
     */
    public Map<String, Object> getAddress(){
        Map<String,Object> map=new HashMap<>();
        map.put("play_addr",configManager.getPlayAddr());
        map.put("report_addr",configManager.getReportAddr());
        return map;
    }


    /**
     * 添加举报
     * @param liveReport 举报参数
     * @return
     */
    public int addLiveReport(LiveReport liveReport) {


        List<LiveShowDTO> liveShowDTOS=new ArrayList<>();
        DBLiveShowQuery query=DBLiveShowQuery.builder().ids(Arrays.asList(liveReport.getLid())).build();
        try {
            liveShowDTOS= liveShowQueryHandler.list(query).get();
        }catch (Exception e){
            LOGGER.warn("查询直播数据失败，{}",e);
        }
        String title=null;
        Long huid=null;
        if(!CollectionUtils.isEmpty(liveShowDTOS)){
            title=liveShowDTOS.get(0).getTitle();
            huid=liveShowDTOS.get(0).getUserId();
            liveReport.setTitle(title);
        }
        List<Long> uids=new ArrayList<>();
        uids.add(huid);

        if(null !=liveReport.getReportUid()){
            uids.add(liveReport.getReportUid());
        }
        Map<Long,User> userMap=userManager.takeBatchUser(uids);
        //匠人信息
        User hoster=userMap.get(huid);
        liveReport.setHostUid(huid);
        liveReport.setHostName(hoster.getUsername());
        //用户信息
        User reportUser=userMap.get(liveReport.getReportUid());
        if(null !=reportUser) {
            liveReport.setReportName(reportUser.getUsername());
        }
        LiveReportDTO dto=new LiveReportDTO(new LiveReportPO());
        dto.buildFromReq(liveReport);


        return reportRepo.addReport(dto);

    }

    public List<LiveReport> reportList(LiveReportSearch search){

        DBLiveReportQuery query=DBLiveReportQuery.builder()
                .content(search.getContent())
                .limit(search.getLimit())
                .liveId(search.getLid())
                .offset(search.getOffset())
                .orderBy(search.getOrderBy())
                .page(search.getPage())
                .reportUid(search.getReportUid())
                .userId(search.getUid())
                .build();
        List<LiveReportDTO> dtos=reportRepo.list(query);

        return dtos.stream().map(dto -> {
            return dto.po2LiveReport(dto.toDO());
        }).collect(Collectors.toList());
    }

    public int countLiveReport(LiveReportSearch search){
        DBLiveReportQuery query=DBLiveReportQuery.builder()
                .content(search.getContent())
                .limit(search.getLimit())
                .liveId(search.getLid())
                .offset(search.getOffset())
                .orderBy(search.getOrderBy())
                .page(search.getPage())
                .reportUid(search.getReportUid())
                .userId(search.getUid())
                .build();

        return reportRepo.count(query);
    }





}
