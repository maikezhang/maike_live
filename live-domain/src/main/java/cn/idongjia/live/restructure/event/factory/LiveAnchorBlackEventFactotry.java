package cn.idongjia.live.restructure.event.factory;


import cn.idongjia.live.restructure.event.LiveAnchorBlackEvent;

public class LiveAnchorBlackEventFactotry implements com.lmax.disruptor.EventFactory<LiveAnchorBlackEvent> {


    @Override
    public LiveAnchorBlackEvent newInstance() {
        return new LiveAnchorBlackEvent();
    }
}
