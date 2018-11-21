package cn.idongjia.live.restructure.manager;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.live.message.UserStageLiveMessage;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.mq.annocation.MqListener;
import cn.idongjia.mq.message.Message;
import cn.idongjia.mq.message.body.*;
import cn.idongjia.mq.producer.Producer;
import cn.idongjia.mq.topic.LiveTopic;
import cn.idongjia.mq.topic.ZooTopic;
import cn.idongjia.zoo.proto.ZooProto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MqProducerManager {

    private static final Log LOGGER = LogFactory.getLog(MqProducerManager.class);

    @Resource
    private Producer              producer;
    @Resource
    private UserManager           userManager;
    @Resource
    private ConfigManager         configManager;


    /**
     * 向聊天室发送mq消息
     *
     * @param key     传进来的key
     * @param zooPack 聊天室消息结构
     */
    public void pushMessage2Zoo(String key, ZooProto.ZooPack zooPack) {
        producer.send(ZooTopic.ZOO_RESPONSE_TOPIC, key, zooPack.toByteArray());
    }

    /**
     * 向聊天室广播mq消息
     *
     * @param zid     聊天室id
     * @param zooPack 聊天室消息结构
     */
    public void broadCastMessage2Zoo(long zid, ZooProto.ZooPack zooPack) {
        producer.send(ZooTopic.ZOO_PUSH_TOPIC, String.valueOf(zid), zooPack.toByteArray());
    }

    public void broadCastBytes(String topic, String key, byte[] content) {
        producer.send(topic, key, content);
    }

    /**
     * 发送mq消息，消息结构体为PureLiveMessage
     *
     * @param topic   mq Topic
     * @param key     mq key
     * @param message mq 消息结构体
     */
    public void pushMessageWithMessage(String topic, String key, PureLiveMessage message) {
        Message<PureLiveMessage> messageToPush = new Message<>();
        messageToPush.setTopic(topic);
        messageToPush.setKey(key);
        messageToPush.setBody(message);
        producer.send(messageToPush);
    }

    /**
     * 直播开播通知订阅的用户
     *
     * @param plid      直播id
     * @param uid       匠人id
     * @param liveTitle 直播标题
     * @param bookUids  订阅用户id
     */
    public void pushMessage2Dynamic(Long plid, Long uid, String liveTitle, List<Long> bookUids) {
        User                         user        = userManager.getUser(uid);
        Message<PureLiveBookMessage> message     = new Message<>();
        PureLiveBookMessage          bookMessage = new PureLiveBookMessage();
        bookMessage.setUid(uid);
        bookMessage.setPlid(plid);
        bookMessage.setTitle(String.format(configManager.getLiveStartTips(), user.getUsername(), liveTitle));
        bookMessage.setUids(bookUids);
        message.setTopic(LiveTopic.LIVE_START);
        message.setKey(String.valueOf(plid));
        message.setBody(bookMessage);
        producer.send(message);
    }

    /**
     * 广播直播推流状态变化消息
     *
     * @param liveShowId 直播ID
     * @param state      状态0、断流1、续流
     * @param type       直播类型
     */
    public void broadCastLivePushState(long liveShowId, int state, int type) {
        Message<LivePushMessage> message = new Message<>();
        LivePushMessage pushMessage = new LivePushMessage();
        pushMessage.setLiveShowId(liveShowId);
        pushMessage.setState(state);
        pushMessage.setType(type);
        message.setTopic(LiveTopic.LIVE_PUSH_STATE);
        message.setBody(pushMessage);
        message.setKey(String.valueOf(liveShowId));
        try {
            producer.send(message);
        } catch (Exception e) {
            LOGGER.warn("发送mq消息竟然失败了T_T");
        }
    }


    /**
     * 发送mq消息，消息体为byte数组
     *
     * @param topic mq topic
     * @param key   mq key
     * @param bytes mq 消息体
     */
    public void pushMessageWithByte(String topic, String key, byte[] bytes) {
        producer.send(topic, key, bytes);
    }

    //直播更新mq

    public void pushLiveOffLine(Long id){
        pushLiveChange(id,LiveChangeMessage.OFFLINE);
    }

    public void pushLiveOnLine(Long id){
        pushLiveChange(id,LiveChangeMessage.ONLINE);
    }

    public void pushLiveCreate(Long id) {
        pushLiveChange(id, LiveChangeMessage.CREATE);
    }

    public void pushLiveModify(Long id) {
        pushLiveChange(id, LiveChangeMessage.MODIFY);
    }

    public void pushLiveDelete(Long id) {
        pushLiveChange(id, LiveChangeMessage.DELETE);
    }

    public void pushLiveChange(Long id, Integer type) {
        LiveChangeMessage body = new LiveChangeMessage();
        body.setId(id);
        body.setType(type);
        Message<LiveChangeMessage> message = new Message();
        message.setTopic(LiveTopic.LIVE_CHANGE);
        message.setBody(body);
        message.setKey(String.valueOf(id));
        try {
            producer.send(message);
        } catch (Exception e) {
            LOGGER.error("直播修改mq发2送失败", e);
        }
    }

    public void pushUserStageLiveMessage(List<Long> liveIds,int action,int userStage){
        UserStageLiveMessage body = new UserStageLiveMessage();
        body.setAction(action);
        body.setUserStage(userStage);
        body.setLiveIds(liveIds);
        Message<UserStageLiveMessage> message = new Message();
        message.setTopic(cn.idongjia.live.message.LiveTopic.LIVE_USER_STAGE_LIVE);
        message.setBody(body);
        message.setKey(String.valueOf(userStage));
        try {
            producer.send(message);
        } catch (Exception e) {
            LOGGER.error("直播修改mq发送失败", e);
        }
    }

}
