package cn.idongjia.live.v2.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 直播商品资源
 * @author zhang created on 2018/1/22 下午2:35
 * @version 1.0
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemResource extends LiveResource{

    /**
     * 商品图片
     */
    private String pic;
    /**
     * 商品价格
     */
    private String price;
    /**
     * 是否为主推
     */
    private Boolean isMain;
    /**
     * 商品库存
     */
    private Integer stock;
    /**
     * 匠人名字
     */
    private String craftsName;
    /**
     * 商品售卖类型
     */
    private Integer saleType;
}
