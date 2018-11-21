package cn.idongjia.live.db.mybatis.query;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 页面tab 关联直播查询条件
 *
 * @author lc
 * @create at 2018/7/6.
 */
@Getter
public class DBPageTabLiveQuery extends BaseQuery {
    /**
     * tab id
     */
    private Long tabId;


    /**
     * id
     */
    private List<Long> ids;


    /**
     * 显示状态
     */
    private Integer showStatus;

    /**
     * 关联id
     */
    private List<Long> liveIds;

    /**
     * 直播标题
     */
    private String liveTitle;


    @Builder
    public DBPageTabLiveQuery(String orderBy, Integer limit, Integer page, Long beginTime, Long endTime, Integer
            offset, Long tabId, List<Long> ids, List<Long> liveIds, Integer showStatus,String liveTitle) {
        super(orderBy, limit, page, beginTime, endTime, offset);
        this.tabId = tabId;
        this.ids = ids;
        this.liveIds = liveIds;
        this.showStatus = showStatus;
        this.liveTitle=liveTitle;
    }
}
