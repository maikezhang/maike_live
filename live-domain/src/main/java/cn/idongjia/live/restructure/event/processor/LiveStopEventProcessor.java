package cn.idongjia.live.restructure.event.processor;

import cn.idongjia.live.restructure.event.LiveStopData;
import cn.idongjia.live.restructure.event.LiveStopEvent;
import cn.idongjia.live.restructure.event.common.AbstractEventProcessor;
import cn.idongjia.live.restructure.event.factory.LiveStartedEventFactotry;
import cn.idongjia.live.restructure.event.factory.LiveStopEventFactotry;
import cn.idongjia.live.restructure.event.handler.LiveStopStepOneHandler;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.WaitStrategy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class LiveStopEventProcessor extends AbstractEventProcessor<LiveStopData,LiveStopEvent> implements InitializingBean {

    private static final int QUEUE_SIZE = 1024;

    @Resource
    private LiveStopStepOneHandler liveStopStepOneHandler;

    @Override
    protected int getQueueSize() {
        return QUEUE_SIZE;
    }

    @Override
    protected EventFactory eventFactory() {
        return new LiveStopEventFactotry();
    }

    @Override
    public void initPipe() {
        disruptor.handleEventsWith(liveStopStepOneHandler);
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
