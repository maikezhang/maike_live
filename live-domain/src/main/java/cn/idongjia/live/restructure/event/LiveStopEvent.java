package cn.idongjia.live.restructure.event;


import cn.idongjia.live.restructure.event.common.EventMeta;
import cn.idongjia.live.restructure.event.common.ValueWrapper;

public class LiveStopEvent extends ValueWrapper<LiveStopData> {


    public LiveStopEvent() {
        super(new EventMeta());
    }
}
