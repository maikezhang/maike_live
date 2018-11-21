package cn.idongjia.live.db.mybatis.po;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Created by zhang on 2017/2/19.
 * 直播关联专场查询结果
 */
@Setter
@Getter
public class LiveShow4IndexPO extends BasePO {

    private static final long serialVersionUID = 5911098839313781581L;
    //直播ID
    private Long id;
    //专场id
    private Long asid;
    //直播名字
    private String title;
    //直播类型1、纯直播2、拍卖直播
    private Integer type;
    //直播进程
    private Integer state;
    //直播预展时间
    private Timestamp preViewTm;
    //直播预计开始时间
    private Timestamp preStartTm;
    //直播预计结束时间
    private Timestamp preEndTm;
    //直播开始时间
    private Timestamp startTm;
    //直播结束时间
    private Timestamp endTm;
    //直播创建时间
    private Timestamp createTm;
    //直播修改时间
    private Timestamp modifiedTm;

    private Integer zrc;
    //主播ID
    private Long userId;
    //直播间ID
    private Long roomId;
    //聊天室ID
    private Long zid;
    //直播状态 -1删除0正常
    private Integer status;
    //所有直播的通用权重
    private Integer generalWeight;
    //头像
    private String avatar;
    //首图
    private String pic;
    //上下线
    private Integer online;
    //小视频
    private Long videoCoverId;
    //小视频地址
    private String videoCoverUrl;
    //云视频类型
    private Integer cloudType;
    //回放id
    private int hasPlayback;

    //直播屏幕方向
    private Integer screenDirection;

    public LiveShow4IndexPO() {
    }
}
