package cn.idongjia.live.api.live.pojo;

import cn.idongjia.common.base.Base;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 直播列表索引返回
 *
 * @author lc
 * @create at 2017/12/18.
 */
@Getter
@Setter
public class LiveApiResp extends Base {
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

    //纯直播状态(-5修改中-4待审核-3审核中-2审核结束-1删除0未上线1已上线)
    private int status;

    //开始时间
    private Long startTime;

    //结束时间
    private Long endTime;

    //权重
    private int generalWeight;

    //封面图地址
    private String pic;

    //短视频地址
    private String videoCover;
    //直播类型
    private int    liveType;
    //数据类型
    private int    type;

    //直播进程1未开始2已开始3已结束
    private int state;

    //直播预计开始时间
    private Long           preStartTime;
    //直播预计结束时间
    private Long           preEndTime;
    //直播创建时间
    private Long           createTime;
    //回放
    private List<PlayBack> playBacks;
    //回放时长
    private String         duration;

    private List<LiveItemResp> items;
    //主播id
    private long uid;
    //主播类型 1匠人0 用户
    private Integer utype;
    //直播屏幕方向
    private Integer screenDirection;
   
}
