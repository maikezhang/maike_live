package cn.idongjia.live.restructure.event.factory;


import cn.idongjia.live.restructure.event.DemoEvent;

public class DemoEventFactory implements com.lmax.disruptor.EventFactory<DemoEvent> {


    @Override
    public DemoEvent newInstance() {
        return new DemoEvent();
    }
}
