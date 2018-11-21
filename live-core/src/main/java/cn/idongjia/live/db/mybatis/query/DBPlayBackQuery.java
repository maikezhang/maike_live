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
public class DBPlayBackQuery extends BaseQuery {

    private List<Long> liveIds;
    private Long id;
    /**
     * 回放时长
     */
    private Long duration;
    /**
     * 回放状态
     */
    private Integer status;

    @Builder
    public DBPlayBackQuery(String orderBy, Integer limit, Integer page, Long beginTime, Long endTime, Integer offset, List<Long> liveIds, Long id, Long duration, Integer status) {
        super(orderBy, limit, page, beginTime, endTime, offset);
        this.liveIds = liveIds;
        this.id = id;
        this.duration = duration;
        this.status = status;
    }
}
