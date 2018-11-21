package cn.idongjia.live.query.purelive.book;

import cn.idongjia.common.query.BaseSearch;

import javax.ws.rs.QueryParam;

/**
 * Created by zhang on 2017/3/9.
 * 纯直播订阅检索表
 */
public class PureLiveBookSearch extends BaseSearch{
    private static final long serialVersionUID = 6558277514408961483L;

    @QueryParam("id")
    private Long id;
    @QueryParam("userId")
    private Long uid;
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
    public PureLiveBookSearch(){}
}
