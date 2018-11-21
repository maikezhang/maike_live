package cn.idongjia.live.restructure.domain.entity.room;

import lombok.Getter;
import lombok.Setter;

/**
 * 直播间模型
 *
 * @author lc
 * @create at 2018/6/6.
 */
@Getter
@Setter
public class LiveRoom {


    /**
     * 直播间ID
     */
    private Long    id;
    /**
     * 直播间云类型
     */
    private Integer cloudType;
    /**
     * 直播间云ID
     */
    private String  cloudId;
    /**
     * 直播间状态
     */
    private Integer status;
    /**
     * 直播间直播ID
     */
    private Long    uid;
    /**
     * 直播间直播ID
     */
    private Long    createTime;
    /**
     * 直播间更新时间
     */
    private Long    modifiedTime;



}
