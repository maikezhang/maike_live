package cn.idongjia.live.v2.pojo;

import cn.idongjia.common.base.Base;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * 匠人商品包装
 * Created by YueXiaodong on 2018/01/23.
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemResourcePackage extends Base {
    //商品列表
    private List<ItemResource> all;
    //已关联到直播的商品id
    private List<Long>         related;
}
