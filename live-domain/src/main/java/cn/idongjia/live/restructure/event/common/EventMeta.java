package cn.idongjia.live.restructure.event.common;

import cn.idongjia.util.Utils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 事件描述信息
 *
 * @author lc
 * @create at 2018/6/5.
 */
@Getter
@Setter
@ToString
public class EventMeta {
    private long createTime;

    public EventMeta() {
        this.createTime = Utils.getCurrentMillis();
    }
}
