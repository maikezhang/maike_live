package cn.idongjia.live.restructure.manager;

import cn.idongjia.live.db.mybatis.query.DBPageTabLiveQuery;
import cn.idongjia.live.db.mybatis.query.DBUserStageLiveQuery;
import cn.idongjia.live.pojo.purelive.playback.PlayBackDO;
import cn.idongjia.live.proto.LiveProto;
import cn.idongjia.live.restructure.biz.LiveShowBO;
import cn.idongjia.live.restructure.biz.PlayBackBO;
import cn.idongjia.live.restructure.domain.entity.live.UserStageLiveE;
import cn.idongjia.live.restructure.domain.entity.tab.PageTabLiveE;
import cn.idongjia.live.restructure.query.LiveShowQueryHandler;
import cn.idongjia.live.restructure.query.UserStageLiveQueryHandler;
import cn.idongjia.live.restructure.repo.PageTabLiveRepo;
import cn.idongjia.live.restructure.repo.UserStageLiveRepo;
import cn.idongjia.live.restructure.rule.TabLiveShowRule;
import cn.idongjia.live.support.AssembleProto;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.mq.annocation.MqListener;
import cn.idongjia.mq.annocation.TopicHandler;
import cn.idongjia.mq.message.Message;
import cn.idongjia.mq.message.body.LiveChangeMessage;
import cn.idongjia.mq.message.body.OrderV3Message;
import cn.idongjia.mq.message.body.PureLivePlayBackMessage;
import cn.idongjia.mq.topic.LiveTopic;
import cn.idongjia.mq.topic.OrderV3Topic;
import cn.idongjia.mq.topic.ZooTopic;
import cn.idongjia.util.Utils;
import cn.idongjia.zoo.pojo.ZooPack;
import cn.idongjia.zoo.proto.ZooProto;
import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@MqListener
public class MqManager {

    private static final Log LOGGER = LogFactory.getLog(MqProducerManager.class);

    @Resource
    private ItemOperationPushManager itemOperationPushManager;

    @Resource
    private LiveShowBO liveShowBO;

    @Resource
    private PlayBackBO playBackBO;

    @Resource
    private MqProducerManager mqProducerManager;


    private static final int LIVE_START = 1;
    private static final int LIVE_STOP  = 0;


    @Resource
    private PageTabLiveRepo pageTabLiveRepo;

    @Resource
    private UserStageLiveQueryHandler userStageLiveQueryHandler;

    @Resource
    private TabLiveShowRule tabLiveShowRule;

    /**
     * 监听从聊天室中发来的mq消息
     *
     * @param key     mq key
     * @param message mq 消息体
     */
    @TopicHandler(topic = ZooTopic.LIVE_TOPIC)
    @SuppressWarnings("unused")
    public void handleLiveReq(String key, byte[] message) {
        ZooPack zooPack = new ZooPack();
        ZooPack.SERIALIZE.decoder(message, zooPack);
        handleProto(key, zooPack.getService(), zooPack);
    }

    /**
     * 根据服务类型处理websocket信息
     *
     * @param key         zoo连接唯一key
     * @param serviceType 服务类型
     * @param zooPack     websocket信息
     */
    public void handleProto(String key, int serviceType, ZooPack zooPack) {
        switch (serviceType) {
            case LiveProto.LiveServiceType.LIVE_STATE_VALUE: {
                //直播状态变化类型
                try {
                    LiveProto.LiveStateReq liveStateReq =
                            LiveProto.LiveStateReq.parseFrom(zooPack.getPack().getSerialized());
                    //TODO zooPack.getZooUser().getConnectionId()需要zoo提供完整的key
                    pushLiveState(liveStateReq, zooPack.getZooUser().getConnectionId());
                } catch (InvalidProtocolBufferException e) {
                    LOGGER.error("处理Proto异常", e.getUnfinishedMessage());
                }
                break;
            }
            default:
                break;
        }
    }

    /**
     * 处理后台开播停播请求
     *
     * @param liveStateReq 直播状态变化请求
     * @param key          传进来的key，直接回传回去
     */
    private void pushLiveState(LiveProto.LiveStateReq liveStateReq, String key) {
        boolean isSuccess = true;
        switch (liveStateReq.getType()) {
            case LIVE_START:
                liveShowBO.start((long) liveStateReq.getLsid(), false);
                break;
            case LIVE_STOP:
                liveShowBO.stop((long) liveStateReq.getLsid());
                break;
            default:
                break;
        }
        ZooProto.ZooPack zooPack = AssembleProto.assembleLiveStateResp(isSuccess);
        mqProducerManager.pushMessage2Zoo(key, zooPack);
        mqProducerManager.pushLiveModify((long) liveStateReq.getLsid());
    }

    /**
     * 监听live发送的直播状态改变mq
     *
     * @param key     mq key
     * @param message mq 消息体
     */
    @TopicHandler(topic = LiveTopic.LIVE_STATE_CHANGE)
    @SuppressWarnings("unused")
    public void handleLiveStateChange(String key, byte[] message) {
        Long lid      = Long.parseLong(key);
        int  state    = message[1];
        int  liveType = message[0];
        liveShowBO.dealWithLiveStateChanged(lid, state,liveType);
    }

    /**
     * 监听回放生成mq
     *
     * @param key     mq key
     * @param message mq消息
     */
    @TopicHandler(topic = LiveTopic.PLAYBACK_GENERATE)
    public void handlePlayBackGenerate(String key, PureLivePlayBackMessage message) {
        PlayBackDO playBackDO = new PlayBackDO();
        playBackDO.setLid(message.getLid());
        playBackDO.setUrl(message.getUrl());
        playBackDO.setDuration(message.getDuration());
        playBackBO.addPlayBack(playBackDO);
    }

    /**
     * 订单付款
     */
    @TopicHandler(topic = OrderV3Topic.ORDER_PAYED_TOPIC)
    public void handleOrderPayed(Message<OrderV3Message> message) {
        //1.验证是否关联直播 2.推送 3.定时任务5min推送
        OrderV3Message orderV3Message = message.getBody();
        Long           userId         = orderV3Message.getUserId();
        orderV3Message.getOrderItems().forEach(x -> {
            itemOperationPushManager.pushPaidOperation(userId, x.getItemId());
        });
    }

    /**
     * 下单
     */
    @TopicHandler(topic = OrderV3Topic.ORDER_ADD_TOPIC)
    public void handlerOrderAdded(Message<OrderV3Message> message) {
        //1.验证是否关联直播 2.推送
        OrderV3Message orderV3Message = message.getBody();
        Long           userId         = orderV3Message.getUserId();
        orderV3Message.getOrderItems().forEach(x -> {
            itemOperationPushManager.pushOrderAddedOperation(userId, x.getItemId());
        });
    }

    //TODO 2018-04-11 加购物车topic


    @TopicHandler(topic = LiveTopic.LIVE_CHANGE)
    public void handlerLiveChange(Message<LiveChangeMessage> liveChangeMessage) {
        LiveChangeMessage message = liveChangeMessage.getBody();
        Long              liveId  = message.getId();
        if (Objects.equals(LiveChangeMessage.DELETE, message.getType())) {
            List<UserStageLiveE> userStageLiveES = userStageLiveQueryHandler.list(DBUserStageLiveQuery.builder().liveIds(Arrays.asList(liveId)).build());
            userStageLiveES.stream().forEach(userStageLiveE -> {
                userStageLiveE.detete();
            });
            List<PageTabLiveE> pageTabLiveVS = pageTabLiveRepo.list(DBPageTabLiveQuery.builder().liveIds(Arrays.asList(liveId)).build());
            if (!Utils.isEmpty(pageTabLiveVS)) {
                pageTabLiveVS.stream().forEach(pageTabLiveE -> {
                    pageTabLiveRepo.delete(pageTabLiveE.getId());
                });
            }

        } else if (!Objects.equals(LiveChangeMessage.CREATE, message.getType())) {
            boolean          validate   = tabLiveShowRule.validate(liveId);
            BaseEnum.YesOrNo showStatus = validate ? BaseEnum.YesOrNo.YES : BaseEnum.YesOrNo.NO;


            List<PageTabLiveE> pageTabLiveVS = pageTabLiveRepo.list(DBPageTabLiveQuery.builder().liveIds(Arrays.asList(liveId)).build());
            if (!Utils.isEmpty(pageTabLiveVS)) {
                pageTabLiveVS.stream().forEach(pageTabLiveV -> {
                    pageTabLiveV.setShowStatus(showStatus);
                    pageTabLiveRepo.edit(pageTabLiveV);
                });
            }
            List<UserStageLiveE> userStageLiveES = userStageLiveQueryHandler.list(DBUserStageLiveQuery.builder().liveIds(Arrays.asList(liveId)).build());
            if (!Utils.isEmpty(userStageLiveES)) {
                userStageLiveES.stream().forEach(userStageLiveE -> {
                    userStageLiveE.setShowStatus(showStatus);
                    userStageLiveE.update();
                });
            }
        }


    }

}
