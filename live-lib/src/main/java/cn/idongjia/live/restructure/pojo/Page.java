package cn.idongjia.live.restructure.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.ws.rs.QueryParam;

/**
 * 分页数
 *
 * @author lc
 * @create at 2018/7/6.
 */
@Getter
@Setter
@ToString
public class Page extends Query {
    public Page() {

    }

    @QueryParam(cn.idongjia.consts.Query.PAGE)
    private Integer page;

    @QueryParam(cn.idongjia.consts.Query.LIMIT)
    private Integer limit;

    private Integer offset;

    @QueryParam(cn.idongjia.consts.Query.ORDERBY)
    private String orderBy;

    public Integer getOffset() {
        if (null == offset && page != null && limit != null) {
            offset = (page - 1) * limit;
        }
        return offset;
    }
}
