package cn.idongjia.live.restructure.pojo.co;

import cn.idongjia.live.restructure.pojo.ClientObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class UserStageLiveDetailCO extends ClientObject {

    /**
     * id
     */
    private Long id;
    /**
     * 直播id
     */
    private Long liveId;

    /**
     * 权重
     */
    private Integer weight;

    /**
     * 用户阶段
     */
    private int stage;

    /**
     * 专场id
     */
    private Long sessionId;

    /**
     * 首图
     */
    private String pic;

    /**
     * 匠人名称
     */
    private String craftman;

    /**
     * 直播进程
     */
    private Integer state;

    /**
     * 标题
     */
    private String title;

}
