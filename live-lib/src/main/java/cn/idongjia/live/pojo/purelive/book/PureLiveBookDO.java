package cn.idongjia.live.pojo.purelive.book;

import cn.idongjia.common.base.Base;

import java.sql.Timestamp;

/**
 * Created by zhang on 2017/3/9.
 * 纯直播订阅数据表类
 */
public class PureLiveBookDO extends Base {
    private static final long serialVersionUID = 944438365510826470L;

    //唯一ID
    private Long id;
    //用户ID
    private Long uid;
    //直播ID
    private Long lid;
    //订阅状态-1、删除0、正常
    private Integer status;
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

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
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
    public PureLiveBookDO(){}
}
