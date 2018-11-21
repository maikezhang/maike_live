package cn.idongjia.live.pojo.live;

import cn.idongjia.common.base.Base;

/**
 * 直播云录制视频信息
 */
public class LiveRecord extends Base {

    private static final long serialVersionUID = -2027239599848862628L;
    //录制文件内容
    private String videoName;
    //录制文件点播地址
    private String vodUrl;
    //录制文件下载地址
    private String downloadUrl;
    //录制文件开始时间
    private String starttm;
    //录制文件结束时间
    private String endtm;

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVodUrl() {
        return vodUrl;
    }

    public void setVodUrl(String vodUrl) {
        this.vodUrl = vodUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getStarttm() {
        return starttm;
    }

    public void setStarttm(String starttm) {
        this.starttm = starttm;
    }

    public String getEndtm() {
        return endtm;
    }

    public void setEndtm(String endtm) {
        this.endtm = endtm;
    }
}
