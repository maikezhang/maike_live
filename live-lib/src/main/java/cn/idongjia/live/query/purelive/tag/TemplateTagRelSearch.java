package cn.idongjia.live.query.purelive.tag;

import cn.idongjia.common.query.BaseSearch;

import javax.ws.rs.QueryParam;

public class TemplateTagRelSearch extends BaseSearch{

    @QueryParam("id")
    private Long id;
    @QueryParam("tagId")
    private Long tagId;
    @QueryParam("status")
    private Integer status;

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
}
