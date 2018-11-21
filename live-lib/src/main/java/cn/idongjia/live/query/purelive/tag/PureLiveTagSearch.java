package cn.idongjia.live.query.purelive.tag;

import cn.idongjia.common.query.BaseSearch;

import javax.ws.rs.QueryParam;

/**
 * Created by zhang on 2017/3/9.
 * 纯直播标签检索类
 */
public class PureLiveTagSearch extends BaseSearch{
    private static final long serialVersionUID = 7100316106870885681L;

    @QueryParam("id")
    private Long id;
    @QueryParam("name")
    private String name;
    @QueryParam("type")
    private Integer type;
    @QueryParam("status")
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public PureLiveTagSearch(){}
}
