package cn.idongjia.live.pojo.live;

import cn.idongjia.common.base.Base;

public class LiveCloudStat extends Base {
    private static final long serialVersionUID = 4491553191808513660L;

    private Integer streamCount;
    private Double totalBandWidth;
    private Integer totalOnline;

    private String cloudId;
    private Double bandWidth;
    private Integer onlineCount;
    private String clientIP;
    private String serverIP;
    private Integer pushFps;
    private Integer pushSpeed;

    public Integer getStreamCount() {
        return streamCount;
    }

    public void setStreamCount(Integer streamCount) {
        this.streamCount = streamCount;
    }

    public Double getTotalBandWidth() {
        return totalBandWidth;
    }

    public void setTotalBandWidth(Double totalBandWidth) {
        this.totalBandWidth = totalBandWidth;
    }

    public Integer getTotalOnline() {
        return totalOnline;
    }

    public void setTotalOnline(Integer totalOnline) {
        this.totalOnline = totalOnline;
    }

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId;
    }

    public Double getBandWidth() {
        return bandWidth;
    }

    public void setBandWidth(Double bandWidth) {
        this.bandWidth = bandWidth;
    }

    public Integer getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(Integer onlineCount) {
        this.onlineCount = onlineCount;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public Integer getPushFps() {
        return pushFps;
    }

    public void setPushFps(Integer pushFps) {
        this.pushFps = pushFps;
    }

    public Integer getPushSpeed() {
        return pushSpeed;
    }

    public void setPushSpeed(Integer pushSpeed) {
        this.pushSpeed = pushSpeed;
    }
}
