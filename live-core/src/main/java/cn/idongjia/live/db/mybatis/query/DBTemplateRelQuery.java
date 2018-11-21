package cn.idongjia.live.db.mybatis.query;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * @author lc
 * @create at 2018/6/15.
 */
@Getter
@Setter
public class DBTemplateRelQuery extends BaseQuery {
    private Long id;
    private List<Long> tagIds;
    private Integer status;

    @Builder
    public DBTemplateRelQuery(String orderBy, Integer limit, Integer page, Long beginTime, Long endTime, Integer offset, Long id, List<Long> tagIds, Integer status) {
        super(orderBy, limit, page, beginTime, endTime, offset);
        this.id = id;
        this.tagIds = tagIds;
        this.status = status;
    }
}
