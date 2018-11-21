package cn.idongjia.live.query.purelive.audit;

import cn.idongjia.common.query.BaseSearch;

import javax.ws.rs.QueryParam;

/**
 * Created by zhang on 2017/3/9.
 * 纯直播审核信息查询类
 */
public class PureLiveAuditSearch extends BaseSearch {
    private static final long serialVersionUID = 6712688552852129072L;

    @QueryParam("id")
    private Long id;
    @QueryParam("suid")
    private Long suid;
    @QueryParam("status")
    private Integer status;
    @QueryParam("auditResult")
    private Integer auditResult;
    @QueryParam("liveId")
    private Long lid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSuid() {
        return suid;
    }

    public void setSuid(Long suid) {
        this.suid = suid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(Integer auditResult) {
        this.auditResult = auditResult;
    }

    public Long getLid() {
        return lid;
    }

    public void setLid(Long lid) {
        this.lid = lid;
    }

    public PureLiveAuditSearch(){}
}
