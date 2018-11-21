package cn.idongjia.live.restructure.domain.entity.live;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lc
 * @create at 2018/6/6.
 */
@Getter
@Setter
public class LivePullUrlV {
    // rtmp拉流地址
    private String              rtmpUrl;
    // hls拉流地址
    private String              hlsUrl;
    // flv拉流地址
    private String              flvUrl;
}
