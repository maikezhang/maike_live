package cn.idongjia.live.pojo.live;

import cn.idongjia.common.base.Base;

public class LivePullUrl extends Base {
    private static final long serialVersionUID = -8540568406502369422L;

    //拉流地址
    private String rtmpUrl;
    private String hlsUrl;
    private String flvUrl;

    public String getRtmpUrl() {
        return rtmpUrl;
    }

    public void setRtmpUrl(String rtmpUrl) {
        this.rtmpUrl = rtmpUrl;
    }

    public String getHlsUrl() {
        return hlsUrl;
    }

    public void setHlsUrl(String hlsUrl) {
        this.hlsUrl = hlsUrl;
    }

    public String getFlvUrl() {
        return flvUrl;
    }

    public void setFlvUrl(String flvUrl) {
        this.flvUrl = flvUrl;
    }
}
