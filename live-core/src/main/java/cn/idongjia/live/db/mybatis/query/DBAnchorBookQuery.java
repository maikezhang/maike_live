package cn.idongjia.live.db.mybatis.query;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 主播查询
 *
 * @author lc
 * @create at 2018/6/7.
 */
@Getter
@Setter
public class DBAnchorBookQuery extends BaseQuery {
    private List<Long> userIds;

    private List<Long> anchorIds;

    private List<Integer> status;

    private Long id;

    @Builder
    public DBAnchorBookQuery(String orderBy, Integer limit, Integer page, Long beginTime, Long endTime, Integer offset, List<Long> userIds, List<Long> anchorIds, List<Integer> status, Long id) {
        super(orderBy, limit, page, beginTime, endTime, offset);
        this.userIds = userIds;
        this.anchorIds = anchorIds;
        this.status = status;
        this.id = id;
    }
}
