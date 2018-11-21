package cn.idongjia.live.restructure.pojo.co.live;

import cn.idongjia.live.restructure.pojo.ClientObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/8
 * Time: 下午1:29
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class LiveMPFormIdCO extends ClientObject {

    private Long liveId;

    private String formId;

    private Long userId;
}
