package cn.idongjia.live.restructure.event.factory;

import cn.idongjia.live.restructure.event.LiveUpdatedEvent;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/14
 * Time: 下午3:33
 */
public class LiveUpdatedEventFactory implements com.lmax.disruptor.EventFactory<LiveUpdatedEvent> {
    @Override
    public LiveUpdatedEvent newInstance() {
        return new LiveUpdatedEvent();
    }
}
