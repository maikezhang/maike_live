package cn.idongjia.live.pojo.user;

import cn.idongjia.common.base.Base;

public class Anchor extends Base{

    //主播ID
    private Long huid;
    //主播头像
    private String havatar;
    //主播名字
    private String husername;
    //主播头衔
    private String htitle;
    //主播粉丝数
    private Integer fans;
    //是否被当前用户订阅0、未订阅1、已订阅
    private Integer isBooked;

    public Long getHuid() {
        return huid;
    }

    public void setHuid(Long huid) {
        this.huid = huid;
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

    public String getHtitle() {
        return htitle;
    }

    public void setHtitle(String htitle) {
        this.htitle = htitle;
    }

    public Integer getFans() {
        return fans;
    }

    public void setFans(Integer fans) {
        this.fans = fans;
    }

    public Integer getIsBooked() {
        return isBooked;
    }

    public void setIsBooked(Integer isBooked) {
        this.isBooked = isBooked;
    }
    public Anchor(){}
}
