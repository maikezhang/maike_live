package cn.idongjia.live.restructure.event.handler;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.live.db.mybatis.po.LivePurePO;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.cache.liveMP.LiveMPPushCache;
import cn.idongjia.live.restructure.domain.entity.live.LivePullUrlV;
import cn.idongjia.live.restructure.dto.LivePureDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.event.LiveStartedData;
import cn.idongjia.live.restructure.event.LiveStartedEvent;
import cn.idongjia.live.restructure.manager.*;
import cn.idongjia.live.restructure.redis.ShowDescSendRedis;
import cn.idongjia.live.restructure.repo.LivePureRepo;
import cn.idongjia.live.restructure.repo.LiveRoomRepo;
import cn.idongjia.live.restructure.repo.LiveShowRepo;
import cn.idongjia.live.support.AssembleProto;
import cn.idongjia.live.support.DateTimeUtil;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.mq.message.body.PureLiveMessage;
import cn.idongjia.mq.topic.LiveTopic;
import cn.idongjia.mq.topic.PureLiveTopic;
import cn.idongjia.push.pojo.wx.MPRequest;
import cn.idongjia.zoo.proto.ZooProto;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/6/8.
 */
@Component
public class LiveStartedStepOneHandler implements EventHandler<LiveStartedEvent>, WorkHandler<LiveStartedEvent> {

    @Resource
    private LiveRoomRepo liveRoomRepo;

    @Resource
    private MqProducerManager mqProducerManager;

    @Resource
    private ZooManager zooManager;

    @Resource
    private ShowDescSendRedis showDescSendRedis;

    @Resource
    private SearchManager searchManager;

    @Resource
    private LivePureRepo livePureRepo;

    @Resource
    private LiveMPPushCache liveMPPushCache;

    @Resource
    private ConfigManager configManager;

    @Resource
    private WxNotifyPushManager wxNotifyPushManager;

    @Resource
    private LiveShowRepo liveShowRepo;

    @Resource
    private UserManager userManager;

    private static final Log LOGGER = LogFactory.getLog(LiveStartedStepOneHandler.class);


    @Override
    public void onEvent(LiveStartedEvent event, long sequence, boolean endOfBatch) throws Exception {
        onEvent(event);
    }


    @Override
    public void onEvent(LiveStartedEvent event) throws Exception {
        // 调用直播间开始直播
        LiveStartedData liveStartedData = event.getValue();
        liveRoomRepo.startLiveShow(liveStartedData.getLiveRoomId());
        liveRoomRepo.startLiveRecord(liveStartedData.getLiveRoomId(), liveStartedData.getTitle());
        // 根据直播主播ID获取拉流地址
        LivePullUrlV livePullUrl = liveStartedData.getLivePullUrl();
        Long         zid         = liveStartedData.getZid();
        // 组装聊天室Proto
        ZooProto.ZooPack zooPack = AssembleProto.assemblePushUrls(livePullUrl);
        // mq发送到聊天室信息
        mqProducerManager.broadCastMessage2Zoo(zid, zooPack);
        // 调用聊天室接口开始记录历史人数
        zooManager.turnOnZooRoomRecord(zid);
        // 调用聊天室接口开始随机数增长
        zooManager.turnZooRoomUserCountToZrc(zid);
        // 通过mq推送直播状态改变
        byte[] message = new byte[2];
        message[0] = liveStartedData.getType().byteValue();
        message[1] = new Integer(LiveConst.STATE_LIVE_IN_PROGRESS).byteValue();
        mqProducerManager.pushMessageWithByte(LiveTopic.LIVE_STATE_CHANGE, liveStartedData.getId().toString(), message);
        // 补全直播时间相关信息，并更新数据库

        if (!liveStartedData.getType().equals(LiveConst.TYPE_LIVE_AUCTION)) {
            LivePureDTO livePureDTO = new LivePureDTO(new LivePurePO());
            livePureDTO.setStatus(LiveConst.STATUS_LIVE_ONLINE);
            livePureDTO.setId(liveStartedData.getId());
            livePureRepo.update(livePureDTO);
            //发送mq消息给首页，以更新状态
            PureLiveMessage pureLiveMessage = new PureLiveMessage();
            pureLiveMessage.setPlid(liveStartedData.getId());
            mqProducerManager.pushMessageWithMessage(PureLiveTopic.PURE_LIVE_UPDATE, liveStartedData.getId().toString(), pureLiveMessage);
        }
        if (Objects.nonNull(zid) && !Strings.isNullOrEmpty(liveStartedData.getShowDesc())) {
            //不能重复发送
            Optional<Integer> optional = showDescSendRedis.takeRedis(liveStartedData.getId());
            if (!optional.isPresent()) {
                zooManager.sendChatMessage(zid, liveStartedData.getShowDesc(), liveStartedData.getUid());
                showDescSendRedis.putRedis(liveStartedData.getId(), 1);
            }
        }
        mqProducerManager.pushLiveModify(liveStartedData.getId());


        try {

            sendWxMpNotify(liveStartedData.getId());

        } catch (Exception e) {
            LOGGER.error("发送直播小程序失败，{}", e);
        }

    }

    public void sendWxMpNotify(Long liveId) {


        LiveShowDTO byId               = liveShowRepo.getById(liveId);
        User        user               = userManager.getUser(byId.getUserId());
        String      wxLiveMpAppId      = configManager.getWxLiveMpAppId();
        String      wxLiveMpTemplateId = configManager.getWxLiveMpTemplateId();
        String      wxLiveMpPushTips   = configManager.getWxLiveMpPushTips();
        String      wxLiveMpPushPage   = configManager.getWxLiveMpPushPage();
        Set<String> liveUserRedis      = liveMPPushCache.getLiveUserRedis(liveId);
        if (CollectionUtils.isEmpty(liveUserRedis)) {
            return;
        }
        List<String> liveUserlist = new ArrayList<>(liveUserRedis);


        List<Long> userIds = liveUserlist.stream().map(liveUser -> {
            String[] split = liveUser.split("<==>");
            return Long.parseLong(split[0]);
        }).collect(Collectors.toList());

        Map<Long, String> userFormIdMap = liveMPPushCache.batchGetUserFormIdRedis(userIds, liveId);
        LOGGER.info("查询userFormid数据：{}", JSON.toJSONString(userFormIdMap));
        if (Objects.isNull(userFormIdMap)) {
            return;
        }


        liveUserlist.forEach(liveUser -> {
            String[]  split   = liveUser.split("<==>");
            Long      userId  = Long.parseLong(split[0]);
            String    openId  = split[1];
            String    formId  = userFormIdMap.get(userId);
            MPRequest request = new MPRequest();
            request.setFormId(formId);
            request.setTemplateId(wxLiveMpTemplateId);
            request.setToUser(openId);
            request.setPage(String.format(wxLiveMpPushPage, liveId, byId.getZooId()));
            request.addRequestParam("keyword1", byId.getTitle());
            request.addRequestParam("keyword2", DateTimeUtil.getDateTime(byId.getEstimatedStartTime()));
            request.addRequestParam("keyword3", user.getUsername());
            request.addRequestParam("keyword4", String.format(wxLiveMpPushTips, byId.getTitle()));

            wxNotifyPushManager.sendWxMpNotify(wxLiveMpAppId, request);

        });
        try {
            liveMPPushCache.vanishLiveUserRedis(liveId);
            liveMPPushCache.batchDelRedis(userIds, liveId);

        } catch (Exception e) {
            LOGGER.warn("批量删除用户缓存formid数据失败，userIds:{} liveId{} ,exception :{}", userIds, liveId, e);
        }

    }
}
