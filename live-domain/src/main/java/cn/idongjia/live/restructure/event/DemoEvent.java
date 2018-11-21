package cn.idongjia.live.restructure.event;


import cn.idongjia.live.restructure.event.common.EventMeta;
import cn.idongjia.live.restructure.event.common.ValueWrapper;

public class DemoEvent extends ValueWrapper<DemoData> {


    public DemoEvent() {
        super(new EventMeta());
    }
}
