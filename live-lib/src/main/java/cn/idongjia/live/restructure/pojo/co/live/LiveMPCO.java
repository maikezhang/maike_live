package cn.idongjia.live.restructure.pojo.co.live;

import cn.idongjia.live.restructure.pojo.ClientObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import jdk.nashorn.internal.ir.IdentNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/4
 * Time: 上午11:03
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class LiveMPCO extends ClientObject {


    private Long id;

    private String title;

    private String cover;

    private Long createTime;


    private Long preViewTime;

    private Long preStartTime;

    private Long preEndTime;

    private Long startTime;

    private Long endTime;

    private Integer weight;

    private String desc;

    private Long craftsmanUserId;

    private String craftsmanName;

    private String craftsmanAvatar;

    private String craftsmanTitle;

    private Integer type;

    private Integer status;

    private Integer state;

    private Integer hot;

    private String craftsmanCity;

    private Integer online;

    private Long zid;
    /**
     * 直播屏幕方向 0 竖屏 1 横屏
     */
    private Integer screenDirection;


}
