package cn.idongjia.live.query.live;

import cn.idongjia.common.query.BaseSearch;

import javax.ws.rs.QueryParam;

public class LiveRoomSearch extends BaseSearch {
    private static final long serialVersionUID = -3806162744239963884L;

    @QueryParam("id")
    private Long id;
    @QueryParam("cloudId")
    private String cloudId;
    @QueryParam("cloudType")
    private Integer cloudType;
    @QueryParam("status")
    private Integer status;
    @QueryParam("userId")
    private Long uid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId;
    }

    public Integer getCloudType() {
        return cloudType;
    }

    public void setCloudType(Integer cloudType) {
        this.cloudType = cloudType;
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
}
