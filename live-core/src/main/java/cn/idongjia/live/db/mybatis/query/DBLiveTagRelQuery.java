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
public class DBLiveTagRelQuery extends BaseQuery {
    private List<Long> liveIds;
    private Integer type;
    private Integer status;
    private List<Long> tagIds;
    private String name;
    private List<Long> relIds;
    private Long id;

    @Builder
    public DBLiveTagRelQuery(String orderBy, Integer limit, Integer page, Long beginTime, Long endTime, Integer offset, List<Long> tagIds, List<Long> liveIds, Integer type, Integer status, String name, List<Long> relIds, Long id) {
        super(orderBy, limit, page, beginTime, endTime, offset);
        this.liveIds = liveIds;
        this.tagIds = tagIds;
        this.type = type;
        this.status = status;
        this.name = name;
        this.relIds = relIds;
        this.id = id;
    }
}
