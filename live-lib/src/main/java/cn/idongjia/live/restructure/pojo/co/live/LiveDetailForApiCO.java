package cn.idongjia.live.restructure.pojo.co.live;

import cn.idongjia.live.api.live.pojo.PlayBack;
import cn.idongjia.live.pojo.purelive.PureLiveDetailDO;
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
 * Date: 2018/9/21
 * Time: 下午3:06
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class LiveDetailForApiCO extends ClientObject {


    /**
     * 直播id
     */
    private Long               id;
    /**
     * 纯直播标题
     */
    private String             title;
    /**
     * 纯直播图片
     */
    private String             pic;
    /**
     * 聊天室ID
     */
    private Long               zid;
    /**
     * rtmp拉流地址
     */
    private String             rtmpUrl;
    /**
     * hls拉流地址
     */
    private String             hlsUrl;
    /**
     * flv拉流地址
     */
    private String             flvUrl;
    /**
     * 纯直播类型
     */
    private Integer            type;
    /**
     * 纯直播人数
     */
    private Integer            userCount;
    /**
     * 纯直播开始时间
     */
    private Long               starttm;
    /**
     * 主播ID
     */
    private Long               huid;
    /**
     * 主播名字
     */
    private String             husername;
    /**
     * 主播头像
     */
    private String             havatar;
    /**
     * 主播头衔
     */
    private String             htitle;
    /**
     * 分享图片
     */
    private String             sharepic;
    /**
     * 直播进程
     */
    private Integer            state;

    /**
     * 预计开始时间字符串
     */
    private String             starttmStr;
    /**
     * 是否被订阅
     */
    private Integer            isBooked;
    /**
     * 直播屏幕方向 0 竖屏 1 横屏
     */
    private Integer            screenDirection;
    /**
     * 主推资源
     */
    private LiveResourceDetail main;
    /**
     * 关联资源数量
     */
    private Integer            resourceCount;
    /**
     * 直播图文详情的url
     */
    private String             picConUrl;

    /**
     * 分享名字
     */
    private String             sharetitle;
    /**
     * 分享描述
     */
    private String             sharedesc;

    /**
     * 纯直播资源
     */
    private List<PureLiveDetailDO> details;

    /**
     * 直播预计开始时间
     */
    private Long prestarttm;

    /**
     * 回放列表
     *
     */
    private List<PlayBack> playBacks;

    /**
     * 直播简介
     */
    private String showDesc;
}
