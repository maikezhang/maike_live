package cn.idongjia.live.restructure.event;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by 岳晓东 on 2018/01/02.
 */
@Getter
@Setter
public class LiveModifiedEvent extends BaseEvent {

    private Long liveId;

    public LiveModifiedEvent(Long source) {
        super(source);
        this.liveId = source;
    }
}
