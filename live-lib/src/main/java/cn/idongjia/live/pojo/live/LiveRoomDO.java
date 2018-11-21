package cn.idongjia.live.pojo.live;

import cn.idongjia.common.base.Base;

import java.sql.Timestamp;

/**
 * Created by zhang on 2017/2/19.
 * 直播间数据表对应DO
 */
public class LiveRoomDO extends Base{

    private static final long serialVersionUID = -3788876840526043690L;

    //直播间ID
    private Long id;
    //直播间云类型
    private Integer cloudType;
    //直播间云ID
    private String cloudId;
    //直播间状态
    private Integer status;
    //直播间直播ID
    private Long uid;
    //直播间创建时间
    private Timestamp createTm;
    //直播间更新时间
    private Timestamp modifiedTm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCloudType() {
        return cloudType;
    }

    public void setCloudType(Integer cloudType) {
        this.cloudType = cloudType;
    }

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Timestamp getCreateTm() {
        return createTm;
    }

    public void setCreateTm(Timestamp createTm) {
        this.createTm = createTm;
    }

    public Timestamp getModifiedTm() {
        return modifiedTm;
    }

    public void setModifiedTm(Timestamp modifiedTm) {
        this.modifiedTm = modifiedTm;
    }
}
