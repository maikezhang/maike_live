package cn.idongjia.live.api.live.pojo;

import cn.idongjia.common.base.Base;
import lombok.Getter;
import lombok.Setter;

/**
 * 直播关联商品
 *
 * @author lc
 * @create at 2017/12/18.
 */
@Getter
@Setter
public class LiveItemResp extends Base {

    //id
    private Long id;

    //价格
    private Long price;

    //状态
    private Integer state;

    //封面图地址
    private String pic;

    //排序
    private Integer idx;
}
