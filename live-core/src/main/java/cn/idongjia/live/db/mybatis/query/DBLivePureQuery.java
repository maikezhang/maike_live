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
public class DBLivePureQuery extends BaseQuery {

    private List<Long> liveIds;

    private List<Integer> status;

    @Builder
    public DBLivePureQuery(String orderBy, Integer limit, Integer page, Long beginTime, Long endTime, Integer offset, List<Long> liveIds,List<Integer> status) {
        super(orderBy, limit, page, beginTime, endTime, offset);
        this.liveIds = liveIds;
        this.status=status;
    }
}
