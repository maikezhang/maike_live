package cn.idongjia.live.pojo.purelive;

import cn.idongjia.common.base.Base;
import cn.idongjia.live.api.live.pojo.PlayBack;
import cn.idongjia.live.v2.pojo.LiveResourceDetail;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
@ToString
public class PureLive extends Base{
    private static final long serialVersionUID = 1509828825435123585L;

    //纯直播ID
    private Long id;
    //纯直播ID
    private Long plid;
    //纯直播标题
    private String title;
    //纯直播图片
    private String pic;
    //纯直播创建时间
    private Long createtm;
    //纯直播预展时间
    private Long previewtm;
    //纯直播预计开始时间
    private Long prestarttm;
    //纯直播预计结束时间
    private Long preendtm;
    //纯直播实际开始时间
    private Long starttime;
    //纯直播实际结束时间
    private Long endtime;
    //纯直播权重
    private Integer weight;
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
    //纯直播类型
    private Integer type;
    //纯直播人数
    private Integer userCount;
    //纯直播开始时间
    private Long starttm;
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
    //直播分类tag ID
    private Long tagId;
    //直播分类tag名字
    private String tagName;
    //匠人直播图文详情
    private String detail;
    //是否为免审0、否1、是
    private Integer exemption;
    //录制地址
    private String url;
    //录制时长
    private String duration;
    //预计开始时间字符串
    private String starttmStr;
    //录制时间
    private Long durationLong;
    //是否被订阅
    private Integer isBooked;

    //纯直播资源
    private List<PureLiveDetailDO> details;
    
    //短视频id
    private Long videoCoverId;
    //短视频地址
    private String videoCoverUrl;
    //短视频时长
    private Integer videoCoverDuration;
    //短视频默认图片
    private String videoCoverPic;
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
     * 直播是否自动上线 0 否 1 是
     */
    private Integer autoOnline;

    /**
     * 主推资源
     */
    private LiveResourceDetail main;
    /**
     * 关联资源数量
     */
    private Integer resourceCount;

    /**
     * 回放列表
     *
     */
    private List<PlayBack> playBacks;
    /**
     * 直播图文详情的url
     */
    private String picConUrl;
    public PureLive(){}
}
