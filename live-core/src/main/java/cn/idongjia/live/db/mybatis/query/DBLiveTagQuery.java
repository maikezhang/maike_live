package cn.idongjia.live.db.mybatis.query;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author lc
 * @create at 2018/6/13.
 */
@Getter
@Setter
public class DBLiveTagQuery extends BaseQuery {
    private Integer type;
    private Integer status;
    private List<Long> tagIds;
    private String name;
    private Long id;

    @Builder
    public DBLiveTagQuery(String orderBy, Integer limit, Integer page, Long beginTime, Long endTime, Integer offset, List<Long> tagIds, Integer type, Integer status, String name, Long id) {
        super(orderBy, limit, page, beginTime, endTime, offset);
        this.tagIds = tagIds;
        this.type = type;
        this.status = status;
        this.name = name;
        this.id = id;
    }
}
