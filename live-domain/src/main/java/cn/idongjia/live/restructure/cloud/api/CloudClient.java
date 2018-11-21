package cn.idongjia.live.restructure.cloud.api;


import cn.idongjia.live.pojo.live.LivePullUrl;
import cn.idongjia.live.restructure.domain.entity.live.LiveRecord;

import java.util.List;


public interface CloudClient {

    /**
     * 根据云ID和视频名字获取录制地址
     * @param cloudId 云ID
     * @param videoName 视频名字
     * @return 录制地址
     */
    String getRecordUrl(String cloudId, String videoName);

    /**
     * 根据云ID获取播放流地址
     * @param cloudId 云ID
     * @return 播放流地址(0.flv_url 1.rtmp_url 2.hls_url)
     */
    LivePullUrl getPlayUrl(String cloudId);

    /**
     * 根据种子获取云ID
     * @param seed 种子
     * @return 云ID
     */
    String getCloudId(String seed);

    /**
     * 开启云直播流
     * @param cloudId 直播云ID
     * @return 是否成功
     */
    boolean startLiveShow(String cloudId);

    /**
     * 结束云直播流
     * @param cloudId 直播云ID
     * @return 是否成功
     */
    boolean stopLiveShow(String cloudId);

    /**
     * 开始云直播录制
     * @param cloudId 直播云ID
     * @param videoName 视频名字
     * @return 是否成功
     */
    boolean startRecord(String cloudId, String videoName);

    /**
     * 结束云直播录制
     * @param cloudId 直播云ID
     * @param videoName 视频名字
     * @return 是否成功
     */
    boolean stopRecord(String cloudId, String videoName);

    /**
     * 获取云直播下所有的录制视频
     * @param cloudId 直播云ID
     * @return 录制视频文件列表
     */
    List<LiveRecord> listRecords(String cloudId);
}
