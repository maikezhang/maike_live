package cn.idongjia.live.pojo.purelive.audit;

import cn.idongjia.common.base.Base;

import java.sql.Timestamp;

/**
 * 纯直播审核数据表类
 */
public class PureLiveAuditDO extends Base {
    private static final long serialVersionUID = 8756585469107957915L;

    //唯一ID
    private Long id;
    //审核人员
    private Long suid;
    //审核信息
    private String auditMessage;
    //审核结果
    private Integer auditResult;
    //审核状态
    private Integer status;
    //纯直播ID
    private Long lid;
    //创建时间
    private Timestamp createTm;
    //最后修改时间
    private Timestamp modifiedTm;

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

    public String getAuditMessage() {
        return auditMessage;
    }

    public void setAuditMessage(String auditMessage) {
        this.auditMessage = auditMessage;
    }

    public Integer getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(Integer auditResult) {
        this.auditResult = auditResult;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getLid() {
        return lid;
    }

    public void setLid(Long lid) {
        this.lid = lid;
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
    public PureLiveAuditDO(){}
}
