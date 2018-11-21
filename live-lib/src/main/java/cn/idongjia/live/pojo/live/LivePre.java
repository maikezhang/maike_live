package cn.idongjia.live.pojo.live;

import cn.idongjia.common.base.Base;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/7/13
 * Time: 上午11:14
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LivePre  extends Base{
    /**
     *日期
     */
    private String date;
    /**
     * 时间
     */
    private String time;
    /**
     * 直播id
     */
    private Long liveId;
    /**
     * 专场id  当直播为直播拍时有值
     */
    private Long asid;
    /**
     * 直播标题
     */
    private String title;

    /**
     * 匠人头衔
     */
    private String htitle;
    /**
     * 匠人头像
     */
    private String havatar;
    /**
     * 直播预开始时间
     */
    private Long preStartTime;
    /**
     * 是否订阅
     */
    private Boolean subscribed;
}
