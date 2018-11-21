package cn.idongjia.live.restructure.event.processor;

import cn.idongjia.live.restructure.event.LiveStartedData;
import cn.idongjia.live.restructure.event.LiveStartedEvent;
import cn.idongjia.live.restructure.event.common.AbstractEventProcessor;
import cn.idongjia.live.restructure.event.factory.LiveStartedEventFactotry;
import cn.idongjia.live.restructure.event.handler.LiveStartedStepOneHandler;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.WaitStrategy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class LiveStartedEventProcessor extends AbstractEventProcessor<LiveStartedData,LiveStartedEvent> implements InitializingBean {

    private static final int QUEUE_SIZE = 1024;

    @Resource
    private LiveStartedStepOneHandler liveStartedStepOneHandler;

    @Override
    protected int getQueueSize() {
        return QUEUE_SIZE;
    }

    @Override
    protected EventFactory eventFactory() {
        return new LiveStartedEventFactotry();
    }

    @Override
    public void initPipe() {
        disruptor.handleEventsWith(liveStartedStepOneHandler);
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
