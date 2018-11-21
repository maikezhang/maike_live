package cn.idongjia.live.restructure.domain.entity.live;

import cn.idongjia.live.db.mybatis.po.LiveShowPO;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.event.*;
import cn.idongjia.live.restructure.event.processor.*;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.restructure.rule.LiveStartTimeRule;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.live.support.enumeration.LiveOnlineStatus;
import cn.idongjia.live.support.enumeration.LivePlayType;
import cn.idongjia.live.support.enumeration.LiveStatus;
import cn.idongjia.live.support.spring.SpringUtils;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.idongjia.live.support.DateTimeUtil.millis2Timestamp;
import static cn.idongjia.util.Utils.getCurrentMillis;

/**
 * 直播模型
 *
 * @author lc
 * @create at 2018/6/6.
 */
@Getter
@Setter
public class LiveEntity {

    private final LiveStartedEventProcessor liveStartedEventProcessor = SpringUtils.getBean("liveStartedEventProcessor", LiveStartedEventProcessor.class).orElseThrow(() -> LiveException.failure
            ("获取liveStartedEventProcessor实例失败"));


    private final LiveStopEventProcessor liveStopEventProcessor = SpringUtils.getBean("liveStopEventProcessor", LiveStopEventProcessor.class).orElseThrow(() -> LiveException.failure
            ("获取liveStopEventProcessor实例失败"));

    private final LiveCreatedEventProsser liveCreatedEventProsser = SpringUtils.getBean("liveCreatedEventProsser", LiveCreatedEventProsser.class)
            .orElseThrow(() -> LiveException.failure("获取liveCreatedEventProsser实例失败"));
    private final LiveUpdatedEventProcessor liveUpdatedEventProcessor = SpringUtils.getBean("liveUpdatedEventProcessor", LiveUpdatedEventProcessor.class)
            .orElseThrow(() -> LiveException.failure("获取liveUpdatedEventProcessor实例失败"));

    private final LiveDeleteEventProcessor liveDeleteEventProcessor = SpringUtils.getBean("liveDeleteEventProcessor", LiveDeleteEventProcessor.class)
            .orElseThrow(() -> LiveException.failure("获取liveDeleteEventProcessor实例失败"));


    private final LiveStartTimeRule liveStartTimeRule = SpringUtils.takeBean("liveStartTimeRule", LiveStartTimeRule.class);

    private final ConfigManager configManager = SpringUtils.takeBean("configManager", ConfigManager.class);

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
    private LiveStatus status;

    /**
     * 上线状态
     */
    private LiveEnum.LiveOnline online;

    /**
     * 自动上线
     */
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
     * 直播的订阅
     */
    private List<LivePureBook> livePureBooks;


    /**
     * 小视频
     */
    private LiveVideoCover liveVideoCover;
    /**
     * 关联回放
     */
    private List<LiveRecord> records;

    /**
     * 回放数据
     */
    private List<PlayBack> playBacks;


    /**
     * 拉流地址
     */
    private LivePullUrlV livePullUrl;

    /**
     * 描述信息
     */
    private String showDesc;

    public LivePullUrlV getLivePullUrl() {
        return assembleRole().getLivePullUrl(roomId);
    }


    private LiveBaseRole assembleRole() {
        LiveBaseRole role;
        switch (liveType) {
            case PURE_LIVE:
                role = new PureLiveRole();
                break;
            case LIVE_AUCTION:
                role = new AuctionLiveRole();
                break;
            case CRAFTS_TALK_TYPE:
                role = new CraftsTalkLiveRole();
                break;
            case CRAFTS_PURCHASE_TYPE:
                role = new CraftsPurchaseLiveRole();
                break;
            case TREASURE_TYPE:
                role = new TreasureLiveRole();
                break;
            case ELSE_TYPE:
                role = new ElseLiveRole();
                break;
            case OPEN_MATERIAL_TYPE:
                role=new OpenMaterialLiveRole();
                break;
            default:
                throw LiveException.failure("直播类型错误");
        }
        if (Objects.isNull(role)) {
            throw LiveException.failure("分配角色失败");
        }
        return role;
    }


    public void start(Boolean validate) {
        if (validate && liveStartTimeRule.validate(this)) {
            throw LiveException.failure("预计开始时间前" + configManager.getMaxPreStartTime() + "分钟不允许开播");
        }
        LiveShowDTO liveShowDTO = new LiveShowDTO(new LiveShowPO());
        liveShowDTO.setStartTime(millis2Timestamp(getCurrentMillis()));
        liveShowDTO.setEndTime(null);
        liveShowDTO.setId(id);
        liveShowDTO.setModifiedTime(millis2Timestamp(getCurrentMillis()));
        liveShowDTO.setState(LiveConst.STATE_LIVE_IN_PROGRESS);
        liveShowDTO.setOnline(LiveOnlineStatus.ON_LINE.getCode());
        assembleRole().start(liveShowDTO);
        LiveStartedData liveStartedData = LiveStartedData.builder().id(id).uid(huid).showDesc(showDesc).online(LiveOnlineStatus.ON_LINE.getCode()).type(liveType.getCode()).livePullUrl
                (getLivePullUrl()).liveRoomId(roomId).title(title).zid(zid).build();
        liveStartedEventProcessor.publishEvent(liveStartedData);
    }

    public void create() {
        assembleRole().create(this);
        //发送创建直播的事件
        LiveCreatedData liveCreatedData = LiveCreatedData.builder()
                .id(id)
                .estimatedStartTime(estimatedStartTime)
                .estimatedEndTime(estimatedEndTime)
                .autoOnline(autoOnline)
                .online(online)
                .liveType(liveType)
                .createType(LiveEnum.LiveCreateType.LIVE_CREATE)
                .build();
        liveCreatedEventProsser.publishEvent(liveCreatedData);
    }

    /**
     * 更新直播
     *
     * @param newEntity 更新直播实体
     */
    public void update(LiveEntity newEntity) {

        assembleRole().update(this, newEntity);
        ;
        boolean isTaskAutoOnline = false;
        if (!Objects.equals(this.getEstimatedStartTime(), newEntity.getEstimatedStartTime())) {
            isTaskAutoOnline = true;
        }
        LiveUpdateData data = LiveUpdateData.builder()
                .id(id)
                .title(newEntity.getTitle()==null?title:newEntity.getTitle())
                .pic(newEntity.getPic()==null? pic:newEntity.getPic())
                .state(newEntity.getState()==null?state:newEntity.getState())
                .huid(newEntity.getHuid()==null?huid:newEntity.getHuid())
                .isTaskAutoOnline(isTaskAutoOnline)
                .estimatedStartTime(newEntity.getEstimatedStartTime()==null?estimatedStartTime:newEntity.getEstimatedStartTime())
                .estimatedEndTime(newEntity.getEstimatedEndTime()==null?estimatedEndTime:newEntity.getEstimatedEndTime())
                .createTime(this.createTime)
                .updateType(LiveEnum.LiveUpdateType.LIVE_UPDATE)
                .build();
        liveUpdatedEventProcessor.publishEvent(data);
    }


    public void delete() {
        assembleRole().delete(id);
        LiveDeletedData data=LiveDeletedData.builder().id(id).build();
        liveDeleteEventProcessor.publishEvent(data);

    }

    /**
     * 更新直播上下线
     *
     * @param online 上下线状态
     */
    public void updateLiveOnline(Integer online) {
        assembleRole().updateLiveOnline(this, online);
        //发送更新直播上线下线事件
        LiveUpdateData data = LiveUpdateData.builder()
                .id(id)
                .zid(zid)
                .desc(desc)
                .showDesc(showDesc)
                .huid(huid)
                .liveType(liveType)
                .online(BaseEnum.parseInt2Enum(online, LiveEnum.LiveOnline.values()).orElse(null))
                .updateType(LiveEnum.LiveUpdateType.LIVE_ONLINE_UPDATE)
                .build();
        liveUpdatedEventProcessor.publishEvent(data);
    }

    public void autoOnline(Integer onOffLine) {
        if (Objects.equals(onOffLine, BaseEnum.YesOrNo.YES.getCode())) {
            assembleRole().updateLiveAutoOnline(id, onOffLine);
        }
        //发送更新直播上线下线事件
        LiveUpdateData data = LiveUpdateData.builder()
                .id(id)
                .zid(zid)
                .desc(desc)
                .showDesc(showDesc)
                .huid(huid)
                .liveType(liveType)
                .estimatedStartTime(estimatedStartTime)
                .autoOnline(BaseEnum.parseInt2Enum(onOffLine, BaseEnum.YesOrNo.values()).orElse(BaseEnum.YesOrNo.NO))
                .updateType(LiveEnum.LiveUpdateType.LIVE_AUTOONLINE_UPDATE)
                .build();
        liveUpdatedEventProcessor.publishEvent(data);
    }


    public void modifyLivemodifyGeneralWeight(Integer weight) {
        assembleRole().modifyLivemodifyGeneralWeight(id, weight);
        LiveUpdateData data = LiveUpdateData.builder()
                .id(id)
                .updateType(LiveEnum.LiveUpdateType.LIVE_GENERALWEIGHT_UPDATE)
                .generalWeight(weight)
                .build();
        liveUpdatedEventProcessor.publishEvent(data);

    }

    public Long addPlayBack(PlayBack playBack) {
        assembleRole().addPlayBack(playBack);
        //发送创建直播的事件
        LiveCreatedData liveCreatedData = LiveCreatedData.builder()
                .id(id)
                .createType(LiveEnum.LiveCreateType.LIVE_PLAYBACK_CREATE)
                .build();
        liveCreatedEventProsser.publishEvent(liveCreatedData);

        return playBack.getId();
    }


    public void stop() {
        LiveShowDTO liveShowDTO = new LiveShowDTO(new LiveShowPO());
        liveShowDTO.setId(id);
        liveShowDTO.setEndTime(new Timestamp(getCurrentMillis()));
        liveShowDTO.setModifiedTime(new Timestamp(getCurrentMillis()));
        liveShowDTO.setState(LiveConst.STATE_LIVE_OVER);
        assembleRole().stop(liveShowDTO);
        LiveStopData liveStopData = LiveStopData.builder().id(id).uid(huid).type(liveType.getCode()).liveRoomId(roomId).title(title).zid(zid).build();
        liveStopEventProcessor.publishEvent(liveStopData);

    }

    public void resume() {
        assembleRole().resume(roomId);
    }

    public void resetStartTimeAndEndTime() {
        assembleRole().resetStartTimeAndEndTime(id);

    }

    public Long addLiveBook(LivePureBook livePureBook) {
        assembleRole().addLiveBook(livePureBook);
        return livePureBook.getId();
    }

    public boolean deleteLiveBook(LivePureBook livePureBook) {
        assembleRole().delLiveBook(livePureBook);
        return true;
    }

    public void addLiveTagRel(LiveTagRel rel) {
        Long id = null;
        assembleRole().addLiveTagRel(rel);

    }

    public boolean updateLiveTagRel(LiveTagRel rel) {
        assembleRole().updateLiveTagRel(rel);
        return true;
    }

    public boolean deleteLiveTagRel(LiveTagRel rel) {
        assembleRole().delLiveTagRel(rel);
        return true;
    }

    public Map<Long, String> addResource(List<LiveShowResource> liveResources) {
        return assembleRole().addLiveResource(this, liveResources);
    }

    /**
     * 删除直播资源
     *
     * @param liveResources
     */
    public void deleteResource(List<LiveShowResource> liveResources) {
        assembleRole().deleteLiveResource(this, liveResources);

    }

    public Map<Long, String> updateLiveResource(List<LiveShowResource> liveResource) {
        return assembleRole().updateResource(this, liveResource);
    }
}
