package cn.idongjia.live.restructure.pojo.co.tab;

import cn.idongjia.live.restructure.pojo.ClientObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author lc
 * @create at 2018/7/8.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@ToString
public class PageTabLiveCO extends ClientObject {
    /**
     * id
     */
    private Long id;

    /**
     * 直播id
     */
    private Long liveId;
    /**
     * 专场id
     */
    private Long sessionId;

    /**
     * title
     */
    private String liveTitle;
    /**
     * 首图
     */
    private String livePic;
    /**
     * 状态
     */
    private int liveStatus;
    /**
     * 进程
     */
    private int liveState;
    /**
     * 直播类型
     */
    private int liveType;
    /**
     * 权重
     */
    private int weight;


}
