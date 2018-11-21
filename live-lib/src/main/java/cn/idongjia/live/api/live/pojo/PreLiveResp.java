package cn.idongjia.live.api.live.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * 直播列表索引返回
 *
 * @author lc
 * @create at 2017/12/18.
 */
@Getter
@Setter
public class PreLiveResp {
    /**
     * ﻿id,匠人头像,匠人名称,直播标题,直播状态,预计开始时间,封面图,短视频,tabs
     */

    //id
    private Long id;

    //专场id
    private Long asid;

    //头像
    private String avatar;

    //名称
    private String name;

    //标题
    private String title;

    /**
     * 预计开始时间
     */
    private Long preStartTime;

    /**
     * 是否通知
     */
    private boolean isNotify;
}
