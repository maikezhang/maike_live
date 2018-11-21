package cn.idongjia.live.v2.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 资源的详细信息
 * Created by YueXiaodong on 2018/01/19.
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LiveResourceDetail extends LiveResource {

    //拍品最新出价人
    private String username;
    //商品价格or拍品最新出价
    private String price;
    //图片
    private String pic;

}
