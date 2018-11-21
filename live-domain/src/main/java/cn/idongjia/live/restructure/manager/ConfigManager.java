package cn.idongjia.live.restructure.manager;

import lombok.Getter;
import lombok.Setter;

/**
 * 配置信息类
 * @author zhang
 */
@Setter
@Getter
public class ConfigManager {

    //代理前缀
    private String proxyPrefix;
    //直播持续时间长度
    private Long liveDuration;
    //直播录制格式
    private Integer recordFormat;
    //直播云类型
    private String cloudType;
    //H5前缀
    private String H5Prefix;
    //H5后缀
    private String H5Suffix;
    //直播分享模版
    private String shareDescTemplate;
    //直播推送内容
    private String pushContent;
    //直播消息中心内容
    private String infoContent;
    //直播默认管理员ID
    private Long liveSuid;
    //直播默认基准随机数
    private Integer zooRandomCount;
    //直播审核默认通过信息
    private String auditMsgPassed;
    //disconf地址
    private String disconfAddr;
    //disconf app名字
    private String disconfApp;
    //disconf env名字
    private String disconfEnv;
    //disconf version
    private String disconfVersion;
    //disconf用户
    private String disconfUser;
    //disconf密码
    private String disconfPassWord;
    //disconf白名单配置ID
    private String whiteListConfName;
    //disconf热门主播配置ID
    private String hotAnchorsConfName;
    //disconf白名单
    private String whiteListUids;
    //disconf热门主播
    private String hotAnchors;
    //热门主播推荐数量
    private Integer recommendNum;
    //直播预告数量
    private Integer foreShowNum;
    //热门栏目位置
    private Integer columnPos;
    //热门主播位置
    private Integer hotAnchorsPos;
    //热门直播块标题
    private String hotLiveTitle;
    //热门栏目块标题
    private String hotColumnTitle;
    //热门主播块标题
    private String hotAnchorTitle;
    //预告天数
    private Integer foreShowDays;
    //最大拉取预告天数
    private Integer maxForeShowDays;
    //最大单页拉取数量
    private Integer maxForeShowLimit;
    //用户中心直播提醒
    private String userLiveRemind;
    //用户中心回放提醒
    private String userPlayBackRemind;
    //用户中心没东西提醒
    private String userNothingRemind;
    //最小回放时长
    private Long leastDuration;
    //日播标题后缀
    private String titleSuffix;
    //redis过期时间
    private Integer redisExpireTime;

    //腾讯录制时长
    private Integer qCloudRecordDuration;
    //腾讯录制AppID
    private String qCloudAppId;
    //腾讯录制密钥
    private String qCloudBizidKey;
    //腾讯录制密钥
    private String qCloudRecordKey;
    //腾讯API密钥
    private String qCloudAPIKey;
    //腾讯通用功能访问地址
    private String qCloudFcgiUrl;
    //腾讯统计功能访问地址
    private String qCloudStatcgiUrl;

    //以下是网易直播配置参数，现在暂时不再使用
    private Integer vCloudRecordDuration;
    private String vCloudAppKey;
    private String vCloudAppSecret;
    private String vCloudCreateUrl;
    private String vCloudAddressUrl;
    private String vCloudPauseUrl;
    private String vCloudResumeUrl;
    private String vCloudRecordUrl;
    private String vCloudVideoListUrl;
    private String vCloudVideoDetailUrl;

    private String liveStartTips;
    private String charsToRemove;

    //直播列表h5地址
    private String liveListUrl;
    //直播列表分享文案
    private String liveListShareDesc;
    //直播列表分享标题
    private String liveListShareTitle;
    //直播列表分享图片 
    private String liveListSharePic;


    /**
     * 首页缓存失效时间 秒
     */
    private Integer cacheExpireTime;

    /**
     * 直播标题最大字数
     */
    private Integer maxTitleNum;
    /**
     * 直播简介最大字数
     */
    private Integer maxShowDescNum;
    /**
     * 直播开始间隔时间 小时
     */
    private Long liveStartInterval;
    /**
     * 直播预计结束时间提前时间
     */
    private Long liveEndInterval;
    /**
     * 直播自动上线时间 小时
     */
    private Integer liveAutoOnlineInterval;
    /**
     * 直播资源最大数量
     */
    private Integer liveResourceMaxNum;
    /**
     * 判断是否展示调试的时间间隔，分钟
     */
    private Integer isDebugMinutes;
    /**
     * 服务电话
     */
    private String servicePhoneNumber;
    /**
     * 最大提前开播时间 分钟
     */
    private Integer maxPreStartTime;
    /**
     * 默认H5 url
     */
    private String defaultH5Url;
    /**
     * 列表展示最小回放的时长，分钟
     */
    private Integer playBackDurationLimitMinutes;
    /**
     * 默认超级模版id
     */
    private Long defaultTemplateId;

    /**
     * 匠人创建直播保存超级模办的url
     */
    private String saveTemplateUrl;

    /**
     * 获取模板的data数据
     */
    private String getTemplateDataUrl;

    private String liveMixTabContent;

    /**
     * 推流端添加图文详情，调用前端超级模板
     */
    private String appInvokeTemplateUrl;

    /**
     * 匠人推流端，获取模板返回的图文详情列表数据
     */
    private String getAppTemplateUrl;
    /**
     * 玩法地址
     */
    private String playAddr;
    /**
     * 投诉地址
     */
    private String reportAddr;

    private String LiveTypeConfig;

    private Integer liveHomePageTabSize;

    private String wxLiveMpAppId;

    private String wxLiveMpTemplateId;

    private String wxLiveMpPushTips;

    private String wxLiveMpPushPage;

    /**
     * 直播开播推送的关注主播匠人的用户开关
     */
    private Integer liveStartPushFollowUserOnOff;

    /**
     * 直播开播批量的数量
     */
    private Integer liveStartPushBatchCount;

    /**
     * 推送每批的间隔时间  单位 ms
     */
    private Long liveStartPushRepeatInterval;

    /**
     * 直播推流地址
     */
    private String livePushUrl;

    /**
     * 直播拉流地址
     */
    private String livePullUrl;

    /**
     * 纯直播标题字数限制
     */
    private Integer liveTileLength;

    /**
     * 出直播标题超出限制提示文案
     */
    private String liveTitleLengthText;
}
