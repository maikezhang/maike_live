package cn.idongjia.live.v2.domain.show;

import cn.idongjia.live.v2.support.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 直播小视频
 * @author zhang created on 2018/1/17 下午2:04
 * @version 1.0
 */
@Setter
@Getter
@ToString
public class VideoCover extends Entity{

    /**
     * 小视频id
     */
    private Long id;
    /**
     * 小视频时长
     */
    private Integer duration;
    /**
     * 小视频地址
     */
    private String url;
    /**
     * 小视频图片
     */
    private String pic;
}
