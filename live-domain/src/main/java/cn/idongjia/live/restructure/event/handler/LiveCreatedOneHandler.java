package cn.idongjia.live.restructure.event.handler;

import cn.idongjia.live.restructure.event.LiveCreatedData;
import cn.idongjia.live.restructure.event.LiveCreatedEvent;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.restructure.manager.MqProducerManager;
import cn.idongjia.live.restructure.manager.TaskManager;
import cn.idongjia.live.support.BaseEnum;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/13
 * Time: 上午11:22
 */
@Component
public class LiveCreatedOneHandler implements EventHandler<LiveCreatedEvent>, WorkHandler<LiveCreatedEvent> {

    @Resource
    private TaskManager taskManager;

    @Resource
    private ConfigManager configManager;

    @Resource
    private MqProducerManager mqProducerManager;

    @Override
    public void onEvent(LiveCreatedEvent event, long sequence, boolean endOfBatch) throws Exception {
       onEvent(event);
    }

    @Override
    public void onEvent(LiveCreatedEvent event) throws Exception {


        LiveCreatedData createdData=event.getValue();
        switch (createdData.getCreateType()){
            case LIVE_CREATE:
                createLive(createdData);
                break;
            case LIVE_PLAYBACK_CREATE:
                createLivePlayBack(createdData);
                break;
            default:
                break;
        }




    }

    private void createLive(LiveCreatedData createdData){
        //判断是否发送自动上线任务
        if(Objects.equals(createdData.getAutoOnline(), BaseEnum.YesOrNo.YES)){
            Long executeTime = createdData.getEstimatedStartTime()
                    - TimeUnit.HOURS.toMillis(configManager.getLiveAutoOnlineInterval());
            taskManager.addAutoOnlineTask(createdData.getId(), executeTime);
        }
        //发送创建直播mq
        mqProducerManager.pushLiveCreate(createdData.getId());
    }

    private void createLivePlayBack(LiveCreatedData createdData){
        //发送创建直播mq
        mqProducerManager.pushLiveModify(createdData.getId());
    }
}
