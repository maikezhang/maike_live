package cn.idongjia.live.restructure.pojo.co.live;

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
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor

public class LiveResourceCO extends ClientObject {
    /**
     * 封面图
     */
    private String cover;

    /**
     * id
     */
    private Long id;
    /**
     * 价格
     */
    private long price;

    /**
     * 直播进程
     */
    private Integer state;
}
