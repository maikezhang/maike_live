package cn.idongjia.live.query.purelive;

import cn.idongjia.common.query.BaseSearch;

import javax.ws.rs.QueryParam;

public class PureLiveDetailSearch extends BaseSearch {
    private static final long serialVersionUID = 3049251655074986516L;

    @QueryParam("id")
    private Long id;
    @QueryParam("resourceId")
    private Long resourceId;
    @QueryParam("resourceType")
    private Integer resourceType;
    @QueryParam("weight")
    private Integer weight;
    @QueryParam("liveId")
    private Long lid;
    @QueryParam("status")
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Long getLid() {
        return lid;
    }

    public void setLid(Long lid) {
        this.lid = lid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
