package cn.idongjia.live.pojo.live;

import cn.idongjia.common.base.Base;
import cn.idongjia.live.api.live.pojo.PlayBack;
import cn.idongjia.live.restructure.pojo.co.live.LiveResourceCO;
import cn.idongjia.live.restructure.pojo.co.tab.LivePullUrlCO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/7/16
 * Time: 下午5:11
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LiveListCO extends Base {

    /**
     * 订阅状态
     */
    private int bookState;

    /**
     * 封面图
     */
    private String cover;
    /**
     * 头像
     */
    private String craftsmanAvatar;
    /**
     * 匠人所在地
     */
    private String craftsmanCity;

    /**
     * 匠人名
     */
    private String craftsmanName;

    /**
     * 匠人头衔
     */
    private String craftsmanTitle;

    /**
     * 匠人uid
     */
    private Long craftsmanUserId;

    /**
     * 热度
     */
    private Long hot;

    /**
     * 直播id
     */
    private Long id;

    /**
     * 预计开播时间
     */
    private Long preStartTime;


    /**
     * 拉流地址
     */
    private LivePullUrlCO pullURL;

    /**
     * 关联作品
     */
    private List<LiveResourceCO> resources;

    /**
     * 专场id
     */
    private Long sessionId;

    /**
     * 直播进度
     */
    private int state;

    /**
     * 标题
     */
    private String title;
    /**
     * 类型
     */
    private int  type;

    /**
     * 视频封面
     */
    private String videoCoverUrl;

    /**
     * 回放时长
     */
    private String         duration;



    /**
     * 直播回放
     */
    private List<PlayBack> playBacks;
}
