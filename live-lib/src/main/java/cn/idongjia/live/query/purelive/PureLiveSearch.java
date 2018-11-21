package cn.idongjia.live.query.purelive;

import cn.idongjia.live.query.live.LiveShowSearch;

import javax.ws.rs.QueryParam;

public class PureLiveSearch extends LiveShowSearch {
    private static final long serialVersionUID = 6201292885958685647L;

    @QueryParam("weight")
    private Integer weight;
    @QueryParam("status")
    private Integer status;

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public PureLiveSearch(){}
}
