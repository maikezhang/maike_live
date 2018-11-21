package cn.idongjia.live.restructure.event.factory;

import cn.idongjia.live.restructure.event.LiveDeletedEvent;
import cn.idongjia.live.restructure.event.LiveUpdatedEvent;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/14
 * Time: 下午3:33
 */
public class LiveDeletedEventFactory implements com.lmax.disruptor.EventFactory<LiveDeletedEvent> {
    @Override
    public LiveDeletedEvent newInstance() {
        return new LiveDeletedEvent();
    }
}
