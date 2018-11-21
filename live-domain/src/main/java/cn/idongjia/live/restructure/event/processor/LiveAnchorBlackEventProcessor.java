package cn.idongjia.live.restructure.event.processor;

import cn.idongjia.live.restructure.event.LiveAnchorBlackData;
import cn.idongjia.live.restructure.event.LiveAnchorBlackEvent;
import cn.idongjia.live.restructure.event.LiveDeletedData;
import cn.idongjia.live.restructure.event.LiveDeletedEvent;
import cn.idongjia.live.restructure.event.common.AbstractEventProcessor;
import cn.idongjia.live.restructure.event.factory.LiveAnchorBlackEventFactotry;
import cn.idongjia.live.restructure.event.factory.LiveDeletedEventFactory;
import cn.idongjia.live.restructure.event.handler.LiveAnchorBlackOneHandler;
import cn.idongjia.live.restructure.event.handler.LiveDeleteOneHandler;
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
public class LiveAnchorBlackEventProcessor extends AbstractEventProcessor<LiveAnchorBlackData,LiveAnchorBlackEvent> implements InitializingBean {

    private static final int QUEUE_SIZE = 1024;

    @Resource
    private LiveAnchorBlackOneHandler liveAnchorBlackOneHandler;

    @Override
    protected int getQueueSize() {
        return QUEUE_SIZE;
    }

    @Override
    protected EventFactory<LiveAnchorBlackEvent> eventFactory() {
        return new LiveAnchorBlackEventFactotry();
    }

    @Override
    public void initPipe() {
        disruptor.handleEventsWith(liveAnchorBlackOneHandler);
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
