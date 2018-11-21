package cn.idongjia.live.pojo.purelive.playback;

import cn.idongjia.common.base.Base;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zhang
 */
@Setter
@Getter
public class PlayBack extends Base{

    /**
     * 回放标题
     */
    private String title;
    /**
     * 回放状态
     */
    private Integer state;
    /**
     * 主播名字
     */
    private String husername;
    /**
     * 主播头像
     */
    private String havatar;
    /**
     * 回放图片
     */
    private String pic;
    /**
     * 回放简介
     */
    private String desc;
    /**
     * 直播ID
     */
    private Long lid;
    /**
     * 回放详细
     */
    private List<PlayBackDetail> playBacks;
    /**
     * 屏幕方向
     */
    private Integer screenDirection;
    /**
     * 回放总时长
     */
    private Long duration;
}
