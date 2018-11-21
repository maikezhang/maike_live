package cn.idongjia.live.db.mybatis.query;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author lc
 * @create at 2018/6/7.
 */
@Getter
@Setter
public class DBLiveBookQuery extends BaseQuery {

    private List<Long> liveIds;

    private Long userId;
    private Integer status;

    private Long id;

    @Builder
    public DBLiveBookQuery(String orderBy, Integer limit, Integer page, Long beginTime, Long endTime, Integer offset, List<Long> liveIds, Long userId, Integer status, Long id) {
        super(orderBy, limit, page, beginTime, endTime, offset);
        this.liveIds = liveIds;
        this.id = id;
        this.userId = userId;
        this.status = status;
    }
}
