package cn.idongjia.live.restructure.pojo.co.tab;

import cn.idongjia.live.restructure.pojo.ClientObject;
import cn.idongjia.live.restructure.pojo.co.live.LiveResourceCO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * 直播列表返回
 *
 * @author lc
 * @create at 2018/7/8.
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor

public class LiveCO extends ClientObject {
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

}
