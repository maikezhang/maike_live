package cn.idongjia.live.pojo.purelive.banner;

import cn.idongjia.common.base.Base;

import java.sql.Timestamp;

/**
 * 直播banner对象
 * Created by dongjia_lj on 17/3/9.
 */
public class PureLiveBannerDO extends Base {

    private Long bid;           //banner主键
    private String title;       //标题
    private String pic;         //封面
    private Integer weight;     //权重
    private Integer type;       //跳转类型
    private String  addr;       //跳转地址
    private Timestamp starttm;  //生效时间
    private Timestamp endtm;    //结束时间
    private Timestamp createtm; //记录创建时间
    private Timestamp modifiedtm; //记录修改时间
    private Integer status;       //状态
    private String desc;          //描述

    //主播头像
    private String havatar;
    //主播名字
    private String husername;
    //直播开始时间字符串
    private String liveStarttm;
    //直播状态
    private Integer state;
    //直播人数
    private Integer userCount;

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Timestamp getStarttm() {
        return starttm;
    }

    public void setStarttm(Timestamp starttm) {
        this.starttm = starttm;
    }

    public Timestamp getEndtm() {
        return endtm;
    }

    public void setEndtm(Timestamp endtm) {
        this.endtm = endtm;
    }

    public Timestamp getCreatetm() {
        return createtm;
    }

    public void setCreatetm(Timestamp createtm) {
        this.createtm = createtm;
    }

    public Timestamp getModifiedtm() {
        return modifiedtm;
    }

    public void setModifiedtm(Timestamp modifiedtm) {
        this.modifiedtm = modifiedtm;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getHavatar() {
        return havatar;
    }

    public void setHavatar(String havatar) {
        this.havatar = havatar;
    }

    public String getHusername() {
        return husername;
    }

    public void setHusername(String husername) {
        this.husername = husername;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLiveStarttm() {
        return liveStarttm;
    }

    public void setLiveStarttm(String liveStarttm) {
        this.liveStarttm = liveStarttm;
    }
    public PureLiveBannerDO(){}
}
