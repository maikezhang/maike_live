package cn.idongjia.live.pojo.purelive;

import cn.idongjia.common.base.Base;

public class ForeShow extends Base{

    //直播ID
    private Long lid;
    //主播头像
    private String havatar;
    //主播名字
    private String husername;
    //主播头衔
    private String htitle;
    //直播标题
    private String title;
    //直播描述
    private String desc;
    //直播开始时间
    private Long starttm;
    //直播是否被订阅
    private Integer isBooked;
    //开始日期
    private String startDate;
    //开始时间
    private String startTime;

    public String getHavatar() {
        return havatar;
    }

    public void setHavatar(String havatar) {
        this.havatar = havatar;
    }

    public String getHtitle() {
        return htitle;
    }

    public void setHtitle(String htitle) {
        this.htitle = htitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getStarttm() {
        return starttm;
    }

    public void setStarttm(Long starttm) {
        this.starttm = starttm;
    }

    public Integer getIsBooked() {
        return isBooked;
    }

    public void setIsBooked(Integer isBooked) {
        this.isBooked = isBooked;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Long getLid() {
        return lid;
    }

    public void setLid(Long lid) {
        this.lid = lid;
    }

    public String getHusername() {
        return husername;
    }

    public void setHusername(String husername) {
        this.husername = husername;
    }
}