package cn.idongjia.live.db.mybatis.query;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author lc
 * @create at 2018/6/11.
 */
@Getter
@Setter
public class DBLiveResourceQuery extends BaseQuery {
    private List<Long> liveIds;
    private Integer resourceType;
    private Integer status;
    private Long weight;
    private Long resourceId;
    private Long id;

    @Builder
    public DBLiveResourceQuery(String orderBy, Integer limit, Integer page, Long beginTime, Long endTime, Integer offset, List<Long> liveIds, Integer resourceType, Integer status, Long weight, Long resourceId,Long id) {
        super(orderBy, limit, page, beginTime, endTime, offset);
        this.liveIds = liveIds;
        this.resourceType = resourceType;
        this.status = status;
        this.weight = weight;
        this.resourceId = resourceId;
        this.id=id;
    }
}
