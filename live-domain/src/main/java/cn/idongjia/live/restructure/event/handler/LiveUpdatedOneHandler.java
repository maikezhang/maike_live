package cn.idongjia.live.restructure.event.handler;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.event.LiveUpdateData;
import cn.idongjia.live.restructure.event.LiveUpdatedEvent;
import cn.idongjia.live.restructure.manager.*;
import cn.idongjia.live.restructure.redis.ShowDescSendRedis;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.mq.message.body.PureLiveMessage;
import cn.idongjia.mq.topic.PureLiveTopic;
import com.google.common.base.Strings;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/14
 * Time: 下午3:30
 */
@Component
public class LiveUpdatedOneHandler implements EventHandler<LiveUpdatedEvent>, WorkHandler<LiveUpdatedEvent> {

    @Resource
    private ConfigManager configManager;

    @Resource
    private TaskManager taskManager;

    @Resource
    private MqProducerManager mqProducerManager;

    @Resource
    private UserManager userManager;
    @Resource
    private ShowDescSendRedis showDescSendRedis;

    @Resource
    private ZooManager zooManager;

    @Override
    public void onEvent(LiveUpdatedEvent event, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(event);
    }

    @Override
    public void onEvent(LiveUpdatedEvent event) throws Exception {
        LiveUpdateData data=event.getValue();
        switch (data.getUpdateType()){
            case LIVE_UPDATE:
                updateLive(data);
                break;
            case LIVE_ONLINE_UPDATE:
                updateLiveOnline(data);
                break;
            case LIVE_GENERALWEIGHT_UPDATE:
                updateLiveGeneralWeight(data);
                break;
            case LIVE_AUTOONLINE_UPDATE:
                updateAutoOnline(data);
            default:
                break;
        }

    }

    private void updateAutoOnline(LiveUpdateData data){
        if(Objects.equals(data.getAutoOnline(), BaseEnum.YesOrNo.YES)){
            Long executeTime = data.getEstimatedStartTime() - TimeUnit.HOURS.toMillis(configManager.getLiveAutoOnlineInterval());
            taskManager.addAutoOnlineTask(data.getId(), executeTime);
        }else{
            taskManager.cancelAutoOnlineTask(data.getId());
        }
    }

    /**
     * 更新直播通用权重事件处理
     * @param data
     */
    private void updateLiveGeneralWeight(LiveUpdateData data){
        mqProducerManager.pushLiveModify(data.getId());
    }

    /**
     * 更新直播上下线状态
     * @param data 更新数据
     */
    private void updateLiveOnline(LiveUpdateData data){
        if(Objects.equals(data.getLiveType(), LiveEnum.LiveType.PURE_LIVE)){
            //发送mq消息给首页，以更新状态
            PureLiveMessage pureLiveMessage = new PureLiveMessage();
            pureLiveMessage.setPlid(data.getId());
            mqProducerManager.pushMessageWithMessage(PureLiveTopic.PURE_LIVE_UPDATE, data.getId().toString(), pureLiveMessage);
        }
        Long   chatRoomId = data.getZid();
        String showDesc   = data.getShowDesc();
        if(Objects.nonNull(chatRoomId) && !Strings.isNullOrEmpty(showDesc)) {
            //不能重复发送
            Optional<Integer> optional = showDescSendRedis.takeRedis(data.getId());
            if(!optional.isPresent()) {
                zooManager.sendChatMessage(chatRoomId, showDesc, data.getHuid());
                showDescSendRedis.putRedis(data.getId(), 1);
            }
        }
        switch (data.getOnline()) {
            case OFFLINE:
                mqProducerManager.pushLiveOffLine(data.getId());
                break;
            case ONLINE:
                mqProducerManager.pushLiveOnLine(data.getId());
                break;
                default:break;
        }


    }

    /**
     * 更新直播
     * @param data 更新数据
     */
    private void updateLive(LiveUpdateData data){

        if(data.getIsTaskAutoOnline()){
            Long executeTime = data.getEstimatedStartTime()
                    - TimeUnit.HOURS.toMillis(configManager.getLiveAutoOnlineInterval());
            taskManager.updateAutoOnlineTask(data.getId(), executeTime);
        }

        //发送mq消息给首页，以更新状态
        mqProducerManager.pushMessageWithMessage(PureLiveTopic.PURE_LIVE_UPDATE, data.getId().toString()
                , buildPureLiveMessage(data));

        mqProducerManager.pushLiveModify(data.getId());
    }

    private PureLiveMessage buildPureLiveMessage(LiveUpdateData data) {
        PureLiveMessage pureLiveMessage = new PureLiveMessage();
        pureLiveMessage.setPlid(data.getId());
        pureLiveMessage.setCover(data.getPic());
        pureLiveMessage.setTitle(data.getTitle());
        pureLiveMessage.setAlState(data.getState().getCode());
        //根据主播ID查询用户信息
        User user = userManager.getUser(data.getHuid());
        pureLiveMessage.setzUserName(user.getUsername());
        pureLiveMessage.setzAvatar(user.getAvatar());
        //根据主播ID查询匠人信息
        pureLiveMessage.setZctf(user.getAvatar());
        pureLiveMessage.setzUid(user.getUid());
        pureLiveMessage.setCreateTm(data.getCreateTime());
        return pureLiveMessage;
    }
}
