package cn.idongjia.live.restructure.pojo.co.live;

import cn.idongjia.live.api.live.pojo.PlayBack;
import cn.idongjia.live.restructure.pojo.ClientObject;
import cn.idongjia.live.v2.pojo.LiveResourceDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/4
 * Time: 下午3:23
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class LiveMPDetailCO extends ClientObject{

    //纯直播ID
    private Long id;
    //纯直播标题
    private String title;
    //纯直播图片
    private String pic;
    //纯直播创建时间
    private Long createTime;
    //纯直播预展时间
    private Long preViewTime ;
    //纯直播预计开始时间
    private Long preStartTime;
    //纯直播预计结束时间
    private Long preEndTime;
    //纯直播实际开始时间
    private Long startTime;
    //纯直播实际结束时间
    private Long endTime;

    //纯直播云类型
    private Integer cloudType;
    //纯直播描述
    private String desc;
    //聊天室ID
    private Long zid;
    //聊天室管理员ID
    private Long muid;
    //聊天室基准随机数
    private Integer zrc;
    //rtmp拉流地址
    private String rtmpUrl;
    //hls拉流地址
    private String hlsUrl;
    //flv拉流地址
    private String flvUrl;
    //纯直播人数
    private Integer userCount;
    //主播ID
    private Long huid;
    //主播名字
    private String husername;
    //主播头像
    private String havatar;
    //主播头衔
    private String htitle;
    //分享名字
    private String sharetitle;
    //分享图片
    private String sharepic;
    //分享描述
    private String sharedesc;
    /**
     *直播类型  1：其他 2：直播拍 3：匠购 4：探宝 5：匠说
     */
    private Integer liveType;
    //直播范围开始时间
    private Long periodStartTm;
    //直播范围结束时间
    private Long periodEndTm;
    //纯直播状态
    private Integer status;
    //直播进程
    private Integer state;
    //录制时长
    private String duration;
    //预计开始时间字符串
    private String starttmStr;
    //录制时间
    private Long durationLong;
    //是否被订阅
    private Integer isBooked;


    //上下线
    private Integer online;
    /**
     * 直播简介
     */
    private String showDesc;
    /**
     * 直播屏幕方向 0 竖屏 1 横屏
     */
    private Integer screenDirection;

    /**
     * 关联资源数量
     */
    private Integer resourceCount;

    /**
     * 超级模版json字符串
     */
    private String templateJsonStr;

    private Boolean isLike;

}
