package cn.idongjia.live.query.purelive.book;

import cn.idongjia.common.query.BaseSearch;

import javax.ws.rs.QueryParam;

/**
 * Created by zhang on 2017/3/9.
 * 订阅匠人检索类
 */
public class AnchorsBookSearch extends BaseSearch{
    private static final long serialVersionUID = 6151393033194684460L;

    @QueryParam("id")
    private Long id;
    @QueryParam("userId")
    private Long uid;
    @QueryParam("anchorId")
    private Long anchorId;
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

    public Long getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(Long anchorId) {
        this.anchorId = anchorId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
