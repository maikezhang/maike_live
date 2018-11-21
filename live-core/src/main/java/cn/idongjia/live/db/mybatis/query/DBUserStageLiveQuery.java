package cn.idongjia.live.db.mybatis.query;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 直播新老用户强运营数据查询
 *
 * @author lc
 * @create at 2018/7/7.
 */
@Getter
@Setter
public class DBUserStageLiveQuery extends BaseQuery {

    private List<Integer> stages;

    private List<Long> liveIds;

    private Integer showStatus;


    private Integer status;

    @Builder
    public DBUserStageLiveQuery(String orderBy, Integer limit, Integer page, Long beginTime, Long endTime, Integer
            offset, List<Integer> stages, List<Long> liveIds, Integer showStatus,Integer status) {
        super(orderBy, limit, page, beginTime, endTime, offset);
        this.stages = stages;
        this.liveIds = liveIds;
        this.showStatus = showStatus;
        this.status=status;
    }
}
