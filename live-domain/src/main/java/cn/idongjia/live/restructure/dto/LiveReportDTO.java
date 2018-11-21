package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.db.mybatis.po.LiveReportPO;
import cn.idongjia.live.pojo.live.LiveReport;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/26
 * Time: 下午5:09
 */
public class LiveReportDTO extends BaseDTO<LiveReportPO> {
    public LiveReportDTO(LiveReportPO entity) {
        super(entity);
    }

    public Long getId(){
        return entity.getId();
    }
    public Long getLid(){
        return entity.getLid();
    }
    public Long getReportUid(){
        return entity.getReportUid();
    }
    public String getReportName(){
        return entity.getReportName();
    }

    public Integer getType(){
        return entity.getType();
    }
    public String getContent(){
        return entity.getContent();
    }
    public String getDesc(){
        return entity.getDesc();
    }
    public Long getHostUid(){
        return entity.getHostUid();
    }
    public String getHostName(){
        return entity.getHostName();
    }
    public String getTitle(){
        return entity.getTitle();
    }
    public Integer getStatus(){
        return entity.getStatus();
    }
    public Long getCreateTime(){
        return entity.getCreateTime();
    }
    public Long getUpdateTime(){
        return entity.getUpdateTime();
    }

    public void buildFromReq(LiveReport liveReport){
        entity=new LiveReportPO();
        entity.setContent(liveReport.getContent());
        entity.setDesc(liveReport.getDesc());
        entity.setHostName(liveReport.getHostName());
        entity.setCreateTime(liveReport.getCreateTime());
        entity.setHostUid(liveReport.getHostUid());
        entity.setId(liveReport.getId());
        entity.setLid(liveReport.getLid());
        entity.setReportName(liveReport.getReportName());
        entity.setReportUid(liveReport.getReportUid());
        entity.setStatus(liveReport.getStatus());
        entity.setTitle(liveReport.getTitle());
        entity.setType(liveReport.getType());
        entity.setUpdateTime(liveReport.getUpdateTime());
    }

    public LiveReport po2LiveReport(LiveReportPO po){
        LiveReport liveReport=new LiveReport();
        liveReport.setTitle(po.getTitle());
        liveReport.setContent(po.getContent());
        liveReport.setCreateTime(po.getCreateTime());
        liveReport.setHostName(po.getHostName());
        liveReport.setDesc(po.getDesc());
        liveReport.setHostUid(po.getHostUid());
        liveReport.setId(po.getId());
        liveReport.setLid(po.getLid());
        liveReport.setReportName(po.getReportName());
        liveReport.setReportUid(po.getReportUid());
        liveReport.setStatus(po.getStatus());
        liveReport.setType(po.getType());
        liveReport.setUpdateTime(po.getUpdateTime());
        return liveReport;
    }

}
