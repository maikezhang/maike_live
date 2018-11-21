package cn.idongjia.live.pojo.purelive.playback;

import cn.idongjia.common.base.Base;

import java.sql.Timestamp;

public class PlayBackDO extends Base {

    //唯一ID
    private Long id;
    //直播ID
    private Long lid;
    //回放地址
    private String url;
    //回放时长单位毫秒
    private Long duration;
    //腾讯文件ID
    private Integer fileId;
    //回放状态-1删除0正常
    private Integer status;
    //创建时间
    private Timestamp createTm;
    //修改时间
    private Timestamp modifiedTm;

    public PlayBackDO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLid() {
        return lid;
    }

    public void setLid(Long lid) {
        this.lid = lid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }
}
