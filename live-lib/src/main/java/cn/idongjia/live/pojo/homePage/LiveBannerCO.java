package cn.idongjia.live.pojo.homePage;

import cn.idongjia.common.base.Base;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/7/20
 * Time: 上午10:41
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LiveBannerCO extends Base {

    //主键
    private Long bid;
    //封面
    private String cover;
    //跳转类型
    private Integer type;
    //跳转地址
    private String addr;
    //权重
    private Integer weight;
    //状态
    private Integer status;
    //分类id
    private Long classificationId;

}
