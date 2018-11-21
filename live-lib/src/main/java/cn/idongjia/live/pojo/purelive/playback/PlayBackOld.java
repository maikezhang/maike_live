package cn.idongjia.live.pojo.purelive.playback;

import cn.idongjia.common.base.Base;
import lombok.Getter;
import lombok.Setter;

/**
 * 旧版回放
 * @author zhang created on 2018/2/28 下午2:15
 * @version 1.0
 */
@Setter
@Getter
public class PlayBackOld extends Base{

    /**
     * 回放ID
     */
    private Long id;
    /**
     * 回放地址
     */
    private String url;
    /**
     * 回放时长字符串
     */
    private String duration;
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
}
