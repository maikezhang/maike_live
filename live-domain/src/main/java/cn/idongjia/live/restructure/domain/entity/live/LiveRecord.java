package cn.idongjia.live.restructure.domain.entity.live;

import lombok.Getter;
import lombok.Setter;

/**
 * 直播云录制视频信息
 */
@Getter
@Setter
public class LiveRecord {

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

}
