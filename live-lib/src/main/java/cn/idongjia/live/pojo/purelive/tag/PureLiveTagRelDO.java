package cn.idongjia.live.pojo.purelive.tag;

import cn.idongjia.common.base.Base;

import java.sql.Timestamp;

/**
 * Created by zhang on 2017/3/9.
 * 纯直播标签关联表类
 */
public class PureLiveTagRelDO extends Base{
    private static final long serialVersionUID = -6891039038185487145L;

    //唯一ID
    private Long id;
    //标签ID
    private Long tagId;
    //关联直播ID
    private Long lid;
    //关联状态
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

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
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

    public Long getLid() {
        return lid;
    }

    public void setLid(Long lid) {
        this.lid = lid;
    }

    public Timestamp getModifiedTm() {
        return modifiedTm;
    }

    public void setModifiedTm(Timestamp modifiedTm) {
        this.modifiedTm = modifiedTm;
    }
}
