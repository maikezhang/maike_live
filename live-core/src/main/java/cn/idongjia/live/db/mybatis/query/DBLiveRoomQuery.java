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

public class DBLiveRoomQuery extends BaseQuery {
    private List<Long> ids;
    private List<Long> userIds;
    private Integer cloudType;
    private Integer status;

    @Builder
    public DBLiveRoomQuery(String orderBy, Integer limit, Integer page, Long beginTime, Long endTime, Integer offset, List<Long> ids, List<Long> userIds, Integer cloudType, Integer status) {
        super(orderBy, limit, page, beginTime, endTime, offset);
        this.ids = ids;
        this.userIds = userIds;
        this.cloudType = cloudType;
        this.status = status;
    }
}
