package cn.idongjia.live.pojo.purelive.playback;

import cn.idongjia.common.base.Base;
import lombok.Getter;
import lombok.Setter;

/**
 * 直播回放详情
 * @author zhang created on 2018/2/26 上午10:55
 * @version 1.0
 */
@Getter
@Setter
public class PlayBackDetail extends Base{

    /**
     * 回放id
     */
    private Long id;
    /**
     * 回放地址
     */
    private String url;
    /**
     * 回放时长
     */
    private Long durationMillis;
    /**
     * 回放时长字符串
     */
    private String duration;

}
