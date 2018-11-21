package cn.idongjia.live.restructure.event.handler;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.event.LiveDeletedData;
import cn.idongjia.live.restructure.event.LiveDeletedEvent;
import cn.idongjia.live.restructure.event.LiveUpdateData;
import cn.idongjia.live.restructure.event.LiveUpdatedEvent;
import cn.idongjia.live.restructure.manager.*;
import cn.idongjia.live.restructure.redis.ShowDescSendRedis;
import cn.idongjia.live.support.BaseEnum;
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
public class LiveDeleteOneHandler implements EventHandler<LiveDeletedEvent>, WorkHandler<LiveDeletedEvent> {

    @Resource
    private MqProducerManager mqProducerManager;

    @Override
    public void onEvent(LiveDeletedEvent event, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(event);
    }

    @Override
    public void onEvent(LiveDeletedEvent event) throws Exception {
        LiveDeletedData data=event.getValue();

        mqProducerManager.pushLiveDelete(data.getId());

    }


}
