package cn.idongjia.live.restructure.event.factory;


import cn.idongjia.live.restructure.event.LiveStartedEvent;
import cn.idongjia.live.restructure.event.LiveStopEvent;

public class LiveStopEventFactotry implements com.lmax.disruptor.EventFactory<LiveStopEvent> {


    @Override
    public LiveStopEvent newInstance() {
        return new LiveStopEvent();
    }
}
