package cn.idongjia.live.restructure.event;

import cn.idongjia.live.restructure.domain.entity.live.*;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.enumeration.LivePlayType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/13
 * Time: 上午11:20
 */
@Getter
@Setter
@ToString
@Builder
public class LiveCreatedData {


    /**
     * 直播创建类型
     */
    private LiveEnum.LiveCreateType createType;
    /**
     * 纯直播ID
     */
    private Long id;

    /**
     * 纯直播标题
     */
    private String title;

    /**
     * 纯直播图片
     */
    private String pic;


    /**
     * 纯直播创建时间
     */
    private Long createTime;

    /**
     * 直播修改(更新)时间
     */
    private Long modifiedTime;


    private Long roomId;

    /**
     * 纯直播预展时间
     */
    private Long previewTime;

    /**
     * 纯直播预计开始时间
     */
    private Long estimatedStartTime;

    /**
     * 纯直播预计结束时间
     */
    private Long estimatedEndTime;

    /**
     * 纯直播实际开始时间
     */
    private Long startTime;

    /**
     * 纯直播实际结束时间
     */
    private Long endTime;

    /**
     * 纯直播权重
     */
    private Integer weight;

    /**
     * 直播通用权重
     */
    private Integer generalWeight;

    /**
     * 纯直播云类型
     */
    private Integer cloudType;

    /**
     * 纯直播描述
     */
    private String desc;

    /**
     * 聊天室ID
     */
    private Long zid;

    /**
     * 直播播出类型
     */
    private LivePlayType livePlayType;


    /**
     * 主播ID
     */
    private Long huid;


    /**
     * 直播类型
     */
    private LiveEnum.LiveType liveType;

    /**
     * 直播范围开始时间
     */
    private Long periodStartTm;

    /**
     * 直播范围结束时间
     */
    private Long periodEndTm;

    /**
     * 纯直播状态
     */
    private BaseEnum.DataStatus status;

    private LiveEnum.LiveOnline online;

    private BaseEnum.YesOrNo autoOnline;

    /**
     * 直播进程
     */
    private LiveEnum.LiveState state;

    /**
     * 直播分类tag ID
     */
    private List<Tag> tags;

    /**
     * 匠人直播图文详情
     */
    private String detail;

    /**
     * 是否为免审0、否1、是
     */
    private Integer exemption;

    /**
     * 录制地址
     */
    private String url;

    /**
     * 首页热门权重
     */
    private Integer recommendWeight;


    /**
     * 直播屏幕方向
     */
    private Integer screenDirection;

    /**
     * 纯直播资源
     */
    private List<LiveShowResource> resource;


    /**
     * 小视频
     */
    private LiveVideoCover liveVideoCover;
    /**
     * 关联回放
     */
    private List<LiveRecord> records;

    /**
     * 拉流地址
     */
    private LivePullUrlV livePullUrl;

    private String showDesc;


    /**
     * 角色
     */
    private LiveBaseRole role;

}
