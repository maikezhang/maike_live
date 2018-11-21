package cn.idongjia.live.restructure.event.processor;

import cn.idongjia.live.restructure.event.LiveCreatedData;
import cn.idongjia.live.restructure.event.LiveCreatedEvent;
import cn.idongjia.live.restructure.event.common.AbstractEventProcessor;
import cn.idongjia.live.restructure.event.factory.LiveCreatedEventFactory;
import cn.idongjia.live.restructure.event.handler.LiveCreatedOneHandler;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.WaitStrategy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Description: 创建直播的时间处理器
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/13
 * Time: 上午11:18
 */
@Component
public class LiveCreatedEventProsser extends AbstractEventProcessor<LiveCreatedData,LiveCreatedEvent> implements InitializingBean {

    private static final int QUEUE_SIZE = 1024;

    @Resource
    private LiveCreatedOneHandler liveCreatedOneHandler;

    @Override
    protected int getQueueSize() {
        return QUEUE_SIZE;
    }

    @Override
    protected EventFactory<LiveCreatedEvent> eventFactory() {
        return new LiveCreatedEventFactory();
    }

    @Override
    public void initPipe() {
        disruptor.handleEventsWith(liveCreatedOneHandler);

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
