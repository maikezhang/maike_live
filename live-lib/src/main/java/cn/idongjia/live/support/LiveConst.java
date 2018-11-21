package cn.idongjia.live.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class LiveConst {

    public LiveConst() {
    }

    public static final int STATUS_LIVE_NORMAL = 0;


    public static final List<Integer> STATUS_LIVE_AVALIABLE = Arrays.asList(0, 1, 2);


    //纯直播上线状态
    public static final int STATUS_LIVE_ONLINE     = 1;
    //纯直播下线状态
    public static final int STATUS_LIVE_OFFLINE    = 0;
    //纯直播删除状态
    public static final int STATUS_LIVE_DEL        = -1;
    //纯直播审核失败
    public static final int STATUS_LIVE_AUDIT_FAIL = -2;
    //纯直播审核中
    public static final int STATUS_LIVE_AUDITING   = -3;
    //纯直播待审核
    public static final int STATUS_LIVE_TO_AUDIT   = -4;
    //纯直播修改中
    public static final int STATUS_LIVE_MODIFYING  = -5;

    //预展中、APP中未开始
    public static final int STATE_LIVE_NOT_BEGIN   = 1;
    //直播中
    public static final int STATE_LIVE_IN_PROGRESS = 2;
    //已结束
    public static final int STATE_LIVE_OVER        = 3;
    //进行中
    public static final int STATE_LIVE_PROGRESSING = 4;
    //未预展
    public static final int STATE_LIVE_NOT_PREON   = 5;

    //纯直播类型
    public static final int TYPE_LIVE_NORMAL  = 1;
    //拍卖直播类型
    public static final int TYPE_LIVE_AUCTION = 2;

    //纯直播资源状态删除
    public static final int STATUS_DETAIL_DEL    = -1;
    //纯直播资源状态正常
    public static final int STATUS_DETAIL_NORMAL = 0;

    //推流FLV位置
    public static final int FLV_POS  = 0;
    //推流RTMP位置
    public static final int RTMP_POS = 1;
    //推流HLS位置
    public static final int HLS_POS  = 2;

    //直播间状态正常
    public static final int STATUS_ROOM_AVAILABLE = 0;
    //直播间状态删除
    public static final int STATUS_ROOM_DEL       = -1;

    //纯直播超级模版资源类型
    public static final int TYPE_DETAIL_TEMPLATE = 0;
    //纯直播商品资源类型
    public static final int TYPE_DETAIL_ITEM     = 1;

    //订阅状态删除
    public static final int STATUS_BOOK_DEL    = -1;
    //订阅状态正常
    public static final int STATUS_BOOK_NORMAL = 0;

    //标签状态删除
    public static final int STATUS_TAG_DEL    = -1;
    //标签状态正常
    public static final int STATUS_TAG_NORMAL = 0;

    //标签类型热门专栏
    public static final int TYPE_TAG_COLUMN     = 0;
    //标签类型热门专题
    public static final int TYPE_TAG_TOPIC      = 1;
    //标签类
    public static final int TYPE_TAG_CLASSIFIED = 2;

    //标签关联关系删除
    public static final int STATUS_TAG_REL_DEL    = -1;
    //标签关联关系正常
    public static final int STATUS_TAG_REL_NORMAL = 0;

    //直播审核状态删除
    public static final int STATUS_AUDIT_DEL    = -1;
    //直播审核状态正常
    public static final int STATUS_AUDIT_NORMAL = 0;
    //审核通过
    public static final int RESULT_AUDIT_PASS   = 1;
    //审核不通过
    public static final int RESULT_AUDIT_FAIL   = 0;

    //banner删除状态
    public static final int STATUS_BANNER_DEL    = -1;
    //banner正常状态
    public static final int STATUS_BANNER_NORMAL = 0;

    //策略删除
    public static final int STATUS_STRATEGY_DEL    = -1;
    //策略正常
    public static final int STATUS_STRATEGY_NORMAL = 0;
    //策略类型单播
    public static final int TYPE_STRATEGY_ONCE     = 0;
    //策略类型周播
    public static final int TYPE_STRATEGY_WEEKLY   = 1;
    //策略类型日播
    public static final int TYPE_STRATEGY_DAILY    = 2;

    //订阅
    public static final int STATUS_ANCHOR_BOOKED   = 1;
    //未订阅
    public static final int STATUS_ANCHOR_UNBOOKED = 0;

    //免审
    public static final int IS_EXEMPTION        = 0;
    //不是免审
    public static final int NOT_EXEMPTION       = 1;
    //分页第一页
    public static final int FIRST_PAGE          = 1;
    //feeds流直播类型
    public static final int TYPE_FEEDS_LIVE     = 0;
    //feeds流热门栏目类型
    public static final int TYPE_FEEDS_COLUMN   = 1;
    //feeds流热门匠人类型
    public static final int TYPE_FEEDS_ANCHOR   = 2;
    //feeds流热门回放类型
    public static final int TYPE_FEEDS_PLAYBACK = 3;
    //feeds流热门专题类
    public static final int TYPE_FEEDS_TOPIC    = 4;

    //一天的秒数
    public static final long ONE_DAY_SECONDS  = 24 * 3600L;
    //一周的秒数
    public static final long ONE_WEEK_SECONDS = 7 * ONE_DAY_SECONDS;

    //跳转超级模版类型
    public static final int TYPE_JUMP_TEMPLATE = 7;
    //跳转纯直播类型
    public static final int TYPE_JUMP_LIVE     = 31;


    public static final int TYPE_JUMP_LIVE_AUCTION = 19;

    //所有直播通用的上下线状态
    public static final int GENERAL_LIVE_ONLINE  = 1;
    public static final int GENERAL_LIVE_OFFLINE = 0;

    //创建直播的入口类型
    public static final int PUSH_LIVE_CREATE  = 0;
    public static final int CRAFT_LIVE_CREATE = 1;
    public static final int BACK_LIVE_CREATE  = 2;

    public static final String CRAFTS_LIVE_SQL = "CASE WHEN state = 2 THEN 0 WHEN state = 1 THEN 1 WHEN state = 3 THEN 2 END  " +
            ", CASE WHEN state = 2 THEN starttm END DESC" +
            ", CASE WHEN state = 1 THEN prestarttm END" +
            ", CASE WHEN state = 3 THEN endtm END DESC, general_weight DESC";

    public static final String LIVE_TYPE_ELSE            = "【其他】";
    public static final String LIVE_TYPE_AUCTION         = "【拍卖】";
    public static final String LIVE_TYPE_CRAFTS_PURCHASE = "【匠购】";
    public static final String LIVE_TYPE_TREASURE        = "【探宝】";
    public static final String LIVE_TYPE_CRAFTS_TALK     = "【匠说】";
    public static final String LIVE_OPEN_MATERIAL        = "【开料】";


}
