package cn.idongjia.live.restructure.pojo.co;

import cn.idongjia.live.restructure.pojo.ClientObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author lc
 * @create at 2018/7/7.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@NoArgsConstructor

public class CategoryCO extends ClientObject {
    /**
     * 类目id
     */
    private Long id;
    /**
     * 类目名
     */
    private String name;

}
