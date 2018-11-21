package cn.idongjia.live.restructure.domain.entity.live;

import lombok.Getter;
import lombok.Setter;

/**
 * 直播分享数据
 *
 * @author lc
 * @create at 2018/6/6.
 */
@Getter
@Setter
public class LiveShare {
    // 分享名字
    private String sharetitle;
    // 分享图片
    private String sharepic;
    // 分享描述
    private String sharedesc;
}
