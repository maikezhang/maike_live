package cn.idongjia.live.restructure.event.factory;


import cn.idongjia.live.restructure.event.LiveStartedEvent;

public class LiveStartedEventFactotry implements com.lmax.disruptor.EventFactory<LiveStartedEvent> {


    @Override
    public LiveStartedEvent newInstance() {
        return new LiveStartedEvent();
    }
}
