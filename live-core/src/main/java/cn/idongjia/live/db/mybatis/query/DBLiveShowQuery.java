package cn.idongjia.live.db.mybatis.query;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author lc
 * @create at 2018/6/7.
 */
@Getter
@Setter
public class DBLiveShowQuery extends BaseQuery {

    /**
     * 批量直播ID
     */
    private List<Long> ids;

    /**
     * 直播id
     */
    private Long liveId;

    /**
     * 直播名字
     */
    private String title;

    /**
     * 直播类型1、纯直播2、拍卖直播
     */
    private List<Integer> types;
    /**
     * 直播进程
     */
    private List<Integer> states;
    /**
     * 直播预展时间 最小值
     */
    private Timestamp minPreviewTime;


    /**
     * 直播预展时间最大值
     */
    private Timestamp maxPreviewTime;
    /**
     * 直播预计开始时间最小值
     */
    private Timestamp minEstimatedStartTime;

    /**
     * 直播预计开始时间最大值
     */
    private Timestamp maxEstimatedStartTime;
    /**
     * 直播预计结束时间最小值
     */
    private Timestamp minEstimatedEndTime;

    /**
     * 直播预计结束时间最大值
     */
    private Timestamp maxEstimatedEndTime;

    /**
     * 直播开始时间最小值
     */
    private Timestamp minStartTime;

    /**
     * 直播开始时间最大值
     */
    private Timestamp maxStartTime;
    /**
     * 直播结束时间最小值
     */
    private Timestamp minEndTime;

    /**
     * 直播结束时间最大值
     */
    private Timestamp maxEndTime;
    /**
     * 直播创建时间最小值
     */
    private Timestamp minCreateTime;

    /**
     * 直播创建时间最大值
     */
    private Timestamp maxCreateTime;

    /**
     * 批量主播ID
     */
    private List<Long> userIds;

    /**
     * 主播id
     */
    private Long userId;

    /**
     * 直播间ID
     */
    private Long roomId;
    /**
     * 聊天室ID
     */
    private Long zooId;
    /**
     * 直播状态 -1删除0正常
     */
    private List<Integer> status;

    /**
     * 所有直播的通用权重
     */
    private Integer generalWeight;

    /**
     * 上下线
     */
    private Integer online;


    /**
     * 直播屏幕方向
     */
    private Integer screenDirection;
    /**
     * 直播是否自动上线
     */
    private Integer autoOnline;

    @Builder
    public DBLiveShowQuery(Long userId,Long liveId,String orderBy, Integer limit, Integer page, Long beginTime,
            Long endTime, Integer offset, List<Long> ids, String title, List<Integer> types, List<Integer> states,
            Timestamp minPreviewTime, Timestamp maxPreviewTime, Timestamp minEstimatedStartTime, Timestamp maxEstimatedStartTime, Timestamp minEstimatedEndTime, Timestamp maxEstimatedEndTime,
            Timestamp minStartTime, Timestamp maxStartTime, Timestamp minEndTime, Timestamp maxEndTime, Timestamp minCreateTime, Timestamp maxCreateTime, List<Long> userIds, Long roomId, Long zooId, List<Integer> status,  Integer generalWeight, Integer online,  Integer screenDirection, Integer autoOnline) {
        super(orderBy, limit, page, beginTime, endTime, offset);
        this.liveId=liveId;
        this.userId=userId;
        this.ids = ids;
        this.title = title;
        this.types = types;
        this.states = states;
        this.minPreviewTime = minPreviewTime;
        this.maxPreviewTime = maxPreviewTime;
        this.minEstimatedStartTime = minEstimatedStartTime;
        this.maxEstimatedStartTime = maxEstimatedStartTime;
        this.minEstimatedEndTime = minEstimatedEndTime;
        this.maxEstimatedEndTime = maxEstimatedEndTime;
        this.minStartTime = minStartTime;
        this.maxStartTime = maxStartTime;
        this.minEndTime = minEndTime;
        this.maxEndTime = maxEndTime;
        this.minCreateTime = minCreateTime;
        this.maxCreateTime = maxCreateTime;
        this.userIds = userIds;
        this.roomId = roomId;
        this.zooId = zooId;
        this.status = status;
        this.generalWeight = generalWeight;
        this.online = online;
        this.screenDirection = screenDirection;
        this.autoOnline = autoOnline;
    }
}
