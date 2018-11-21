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
public class LiveResp extends Base{
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
    private Integer status;

    //开始时间
    private Long startTime;

    //结束时间
    private Long endTime;

    //uv数据
    private Integer uv;

    //权重
    private Integer generalWeight;

    //封面图地址
    private String pic;

    //短视频id
    private Long videoCoverId;
    //短视频地址
    private String videoCoverUrl;
    //短视频时长
    private Integer videoCoverDuration;
    //短视频默认图片
    private String videoCoverPic;
    //直播类型

    private Integer type;

    //更新时间
    private Long updateTime;
    //直播进程1未开始2已开始3已结束
    private Integer  state;
    //主播id
    private Long uid;
    //直播预计开始时间
    private Long preStartTime;
    //直播预计结束时间
    private Long preEndTime;
    //直播创建时间
    private Long createTime;
    //上下线
    private Integer online;
    //首页热门权重
    private Integer recommendWeight;
    // 聊天室基准随机数
    private Integer zrc;

    private List<LiveItemResp> items;
    //关联资源数量
    private Integer resourceCount;
    //聊天室id
    private Long zid;

    /**
     * 是否置顶
     */
    private Boolean isTop;
}
