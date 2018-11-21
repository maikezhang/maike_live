package cn.idongjia.live.query.cloud;

import cn.idongjia.common.query.BaseSearch;

import javax.ws.rs.QueryParam;

public class DLiveCloudSearch extends BaseSearch {

    @QueryParam("id")
    private Long id;
    @QueryParam("userId")
    private Long uid;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
