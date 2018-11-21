package cn.idongjia.live.query.purelive.tag;

import cn.idongjia.common.query.BaseSearch;

import javax.ws.rs.QueryParam;

/**
 * Created by zhang on 2017/3/9.
 * 纯直播标签关联检索类
 */
public class PureLiveTagRelSearch extends BaseSearch {
    private static final long serialVersionUID = -2389779140949383531L;

    @QueryParam("id")
    private Long id;
    @QueryParam("tagId")
    private Long tagId;
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

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public PureLiveTagRelSearch(){}
}
