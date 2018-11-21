package cn.idongjia.live.restructure.event;

import cn.idongjia.live.restructure.event.common.EventMeta;
import cn.idongjia.live.restructure.event.common.ValueWrapper;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/14
 * Time: 下午3:24
 */
public class LiveUpdatedEvent extends ValueWrapper<LiveUpdateData> {
    public LiveUpdatedEvent() {
        super(new EventMeta());
    }
}
