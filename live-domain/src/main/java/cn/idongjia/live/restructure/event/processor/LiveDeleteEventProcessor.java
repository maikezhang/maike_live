package cn.idongjia.live.restructure.event.processor;

import cn.idongjia.live.restructure.event.LiveDeletedData;
import cn.idongjia.live.restructure.event.LiveDeletedEvent;
import cn.idongjia.live.restructure.event.LiveUpdateData;
import cn.idongjia.live.restructure.event.LiveUpdatedEvent;
import cn.idongjia.live.restructure.event.common.AbstractEventProcessor;
import cn.idongjia.live.restructure.event.factory.LiveDeletedEventFactory;
import cn.idongjia.live.restructure.event.factory.LiveUpdatedEventFactory;
import cn.idongjia.live.restructure.event.handler.LiveDeleteOneHandler;
import cn.idongjia.live.restructure.event.handler.LiveUpdatedOneHandler;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.WaitStrategy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/14
 * Time: 下午3:28
 */
@Component
public class LiveDeleteEventProcessor extends AbstractEventProcessor<LiveDeletedData,LiveDeletedEvent> implements InitializingBean {

    private static final int QUEUE_SIZE = 1024;

    @Resource
    private LiveDeleteOneHandler liveDeleteOneHandler;

    @Override
    protected int getQueueSize() {
        return QUEUE_SIZE;
    }

    @Override
    protected EventFactory<LiveDeletedEvent> eventFactory() {
        return new LiveDeletedEventFactory();
    }

    @Override
    public void initPipe() {
        disruptor.handleEventsWith(liveDeleteOneHandler);
    }

    @Override
    protected WaitStrategy getStrategy() {
        return new BlockingWaitStrategy();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.init();
    }
}
