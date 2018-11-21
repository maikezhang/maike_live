package cn.idongjia.live.db.mybatis.po;

import lombok.Getter;
import lombok.Setter;

/**
 * 封面
 *
 * @author lc
 * @create at 2018/6/11.
 */
@Getter
@Setter
public class VideoCoverPO extends BasePO {
    /**
     * 小视频id
     */
    private Long id;

    /**
     * 小视频时长
     */
    private Integer duration;
    /**
     * 小视频地址
     */
    private String url;
    /**
     * 小视频图片
     */
    private String pic;

    /**
     * 直播id
     */
    private Long liveId;
    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;
}
