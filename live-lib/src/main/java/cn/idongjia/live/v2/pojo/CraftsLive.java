package cn.idongjia.live.v2.pojo;

import cn.idongjia.common.base.Base;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * 匠人端直播数据
 *
 * @author zhang created on 2018/1/17 下午12:53
 * @version 1.0
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CraftsLive extends Base {

    /**
     * 直播id
     */
    private Long lid;
    /**
     * 直播主播id
     */

    private Long anchorId;
    /**
     * 直播图片
     */
    private String pic;
    /**
     * 直播标题
     */
    private String title;
    /**
     * 直播介绍
     */
    private String showDesc;
    /**
     * 直播预计开始时间
     */
    private Long preStartTime;
    /**
     * 直播预计结束时间
     */
    private Long preEndTime;
    /**
     * 直播是否自动上线
     */
    private Integer autoOnline;
    /**
     * 直播屏幕方向
     */

    private Integer screenDirection;
    /**
     * 小视频id
     */

    private Long videoCoverId;
    /**
     * 小视频时长
     */

    private Integer videoCoverDuration;
    /**
     * 小视频封面图
     */

    private String videoCoverPic;
    /**
     * 小视频地址
     */

    private String videoCoverUrl;
    /**
     * 直播关联资源
     */
    private List<LiveResource> resources;
    /**
     * 是否上线 1=上线，0=下线
     */
    private Integer online;
    /**
     * 直播拍的专场id
     */
    private Long asid;
    /**
     * 是否为直播拍
     */

    private Integer isAuction;
    /**
     * 直播开始时间
     */

    private Long startTime;
    /**
     * 聊天室id
     */

    private Long zid;
    /**
     * 直播进程
     */
    private Integer state;
    //直播状态
    private Integer status;

    /**
     * 超级模版json字符串
     */
    private String templateJsonStr;

    /**
     * 直播类型   2：直播拍 3：匠购 4：探宝 5：匠说 6：其他
     */
    private Integer liveType;

    /**
     * 直播类型对应的name 2：直播拍 3：匠购 4：探宝 5：匠说 6：其他
     */
    private String liveTypeName;

}
