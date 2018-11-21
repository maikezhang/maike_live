package cn.idongjia.live.restructure.event;

import cn.idongjia.live.restructure.event.common.EventMeta;
import cn.idongjia.live.restructure.event.common.ValueWrapper;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by 岳晓东 on 2018/01/02.
 */
@Setter
@Getter
public class LiveCreatedEvent extends ValueWrapper<LiveCreatedData> {


    public LiveCreatedEvent() {
        super(new EventMeta());
    }
}
