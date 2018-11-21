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
 * Date: 2018/9/2
 * Time: 下午12:13
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class AnchorBlackWhiteCO extends ClientObject {


    /**
     * 主播id
     */
    private Long craftsmanUserId;


    /**
     * 创建时间 13位
     */
    private Long createTime;


    /**
     * 主播头像
     */
    private String craftsmanAvatar;

    /**
     * 主播名字
     */
    private String craftsmanName;

}
