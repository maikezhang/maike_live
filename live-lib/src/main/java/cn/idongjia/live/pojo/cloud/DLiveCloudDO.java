package cn.idongjia.live.pojo.cloud;

import cn.idongjia.common.base.Base;

import java.sql.Timestamp;

public class DLiveCloudDO extends Base {

    //ID
    private Long id;
    //主播uid
    private Long uid;
    //频道rtmp拉流地址
    private String rtmpUrl;
    //频道hls拉流地址
    private String hlsUrl;
    //频道flv拉流地址
    private String flvUrl;
    //频道创建时间
    private Timestamp createTm;
    //频道修改时间
    private Timestamp modifiedTm;

    public DLiveCloudDO(){}

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreateTm() {
        return createTm;
    }

    public void setCreateTm(Timestamp createTm) {
        this.createTm = createTm;
    }

    public Timestamp getModifiedTm() {
        return modifiedTm;
    }

    public void setModifiedTm(Timestamp modifiedTm) {
        this.modifiedTm = modifiedTm;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
