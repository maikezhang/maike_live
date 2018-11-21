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
public class DBLiveVideoCoverQuery extends BaseQuery {

    private List<Long> liveIds;
    @Builder
    public DBLiveVideoCoverQuery(String orderBy, Integer limit, Integer page, Long beginTime, Long endTime, Integer offset, List<Long> liveIds) {
        super(orderBy, limit, page, beginTime, endTime, offset);
        this.liveIds = liveIds;
    }
}
