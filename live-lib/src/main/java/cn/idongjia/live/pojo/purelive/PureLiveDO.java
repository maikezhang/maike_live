package cn.idongjia.live.pojo.purelive;

import cn.idongjia.live.pojo.live.LiveShowDO;

/**
 * Created by zhang on 2017/2/19.
 * 纯直播数据表对应DO
 */
public class PureLiveDO extends LiveShowDO {

    private static final long serialVersionUID = -2082339519490913694L;

    //纯直播图片
    private String pic;
    //纯直播权重
    private Integer weight;
    //纯直播描述
    private String desc;
    //纯直播状态(-4修改中-3待审核-2审核中-1删除0未上线1上线)
    private Integer status;
    //直播播出时间策略ID
    private Long timeStrategy;
    //录制地址
    private String videoUrl;
    //录制视频时长
    private Long duration;
    //纯直播是否免审
    private Integer exemption;

    public PureLiveDO() {
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getTimeStrategy() {
        return timeStrategy;
    }

    public void setTimeStrategy(Long timeStrategy) {
        this.timeStrategy = timeStrategy;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Integer getExemption() {
        return exemption;
    }

    public void setExemption(Integer exemption) {
        this.exemption = exemption;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}