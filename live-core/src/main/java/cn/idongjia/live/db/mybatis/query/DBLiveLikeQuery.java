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
public class DBLiveLikeQuery extends BaseQuery {
    private Long userId;

    private Long liveId;

    private Integer status;


    @Builder
    public DBLiveLikeQuery(String orderBy, Integer limit, Integer page, Long beginTime, Long endTime, Integer offset, Long userId, Long liveId, Integer status) {
        super(orderBy, limit, page, beginTime, endTime, offset);
        this.userId = userId;
        this.liveId=liveId;
        this.status = status;
    }
}
