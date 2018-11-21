package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.db.mybatis.po.TemplateTagRelPO;

import java.sql.Timestamp;

/**
 * @author lc
 * @create at 2018/6/18.
 */
public class TemplateTagRelDTO extends BaseDTO<TemplateTagRelPO> {
    public TemplateTagRelDTO(TemplateTagRelPO entity) {
        super(entity);
    }

    public String getUrl() {
        return entity.getUrl();
    }

    public void setCreateTime(Timestamp timestamp) {
        entity.setCreateTime(timestamp);
    }

    public void setStatus(int status) {
        entity.setStatus(status);
    }

    public Long getTagId() {
        return entity.getTagId();
    }

    public void setModifiedTime(Timestamp timestamp) {
        entity.setModifiedTime(timestamp);
    }

    public void setId(Long id) {
        entity.setId(id);
    }
}
