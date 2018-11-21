package cn.idongjia.live.api.live.pojo;

import cn.idongjia.common.base.Base;
import lombok.Getter;
import lombok.Setter;

/**
 * 回放
 *
 * @author lc
 * @create at 2017/12/22.
 */
@Getter
@Setter
public class PlayBack extends Base {
    //回放ID
    private Long id;
    //回放地址
    private String url;
    //回放时长字符串
    private String duration;
    //回放毫秒
    private Long durationMillis;
}
