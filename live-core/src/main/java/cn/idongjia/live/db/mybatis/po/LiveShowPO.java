package cn.idongjia.live.db.mybatis.po;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author lc
 * @create at 2018/6/7.
 */
@Getter
@Setter
public class LiveShowPO extends BasePO {
    /**
     * 直播ID
     */
    private Long id;
    /**
     * 直播名字
     */
    private String title;
    /**
     * 直播类型1、纯直播2、拍卖直播
     */
    private Integer type;
    /**
     * 直播进程直播进程1未开始2已开始3已结束
     */
    private Integer state;
    /**
     * 直播预展时间
     */
    private Timestamp previewTime;
    /**
     * 直播预计开始时间
     */
    private Timestamp estimatedStartTime;
    /**
     * 直播预计结束时间
     */
    private Timestamp estimatedEndTime;
    /**
     * 直播开始时间
     */
    private Timestamp startTime;
    /**
     * 直播结束时间
     */
    private Timestamp endTime;
    /**
     * 直播创建时间
     */
    private Timestamp createTime;
    /**
     * 直播修改时间
     */
    private Timestamp modifiedTime;
    /**
     * 主播ID
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
    private Integer status;
    /**
     * 小视频id
     */
    private Long videoCoverId;
    /**
     * 所有直播的通用权重
     */
    private Integer generalWeight;

    /**
     * 上线状态
     */
    private Integer online;

    /**
     * 直播简介
     */
    private String showDesc;
    /**
     * 直播屏幕方向
     */
    private Integer screenDirection;
    /**
     * 直播是否自动上线
     */
    private Integer autoOnline;
}
