package cn.idongjia.live.restructure.event;

import cn.idongjia.live.restructure.event.common.EventMeta;
import cn.idongjia.live.restructure.event.common.ValueWrapper;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhangyignjie on 2018/09/11.
 */
@Setter
@Getter
public class LiveAnchorBlackEvent extends ValueWrapper<LiveAnchorBlackData> {


    public LiveAnchorBlackEvent() {
        super(new EventMeta());
    }
}
