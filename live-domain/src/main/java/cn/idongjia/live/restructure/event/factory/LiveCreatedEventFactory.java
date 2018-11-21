package cn.idongjia.live.restructure.event.factory;

import cn.idongjia.live.restructure.event.LiveCreatedEvent;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/13
 * Time: 下午1:29
 */
public class LiveCreatedEventFactory implements com.lmax.disruptor.EventFactory<LiveCreatedEvent> {
    @Override
    public LiveCreatedEvent newInstance() {
        return new LiveCreatedEvent();
    }
}
