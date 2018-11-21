package cn.idongjia.live.restructure.event;


import cn.idongjia.live.restructure.event.common.EventMeta;
import cn.idongjia.live.restructure.event.common.ValueWrapper;

public class LiveStartedEvent extends ValueWrapper<LiveStartedData> {


    public LiveStartedEvent() {
        super(new EventMeta());
    }
}
