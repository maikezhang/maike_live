package cn.idongjia.live.db.mybatis.po;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class DLiveCloudPO extends BasePO {

    /**
     * ID
     */
    private Long id;
    /**
     * 主播uid
     */
    private Long userId;
    /**
     * 频道rtmp拉流地址
     */
    private String rtmpUrl;
    /**
     * 频道hls拉流地址
     */
    private String hlsUrl;
    /**
     * 频道flv拉流地址
     */
    private String flvUrl;
    /**
     * 频道flv拉流地址
     */
    private Timestamp createTime;
    /**
     * 频道flv拉流地址
     */
    private Timestamp modifiedTime;


}
