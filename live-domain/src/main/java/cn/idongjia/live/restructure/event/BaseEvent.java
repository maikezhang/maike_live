package cn.idongjia.live.restructure.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author zhang created on 2017/9/6 下午5:05
 * @version 1.0
 */
public abstract class BaseEvent extends ApplicationEvent {

    public BaseEvent(Object source) {
        super(source);
    }

    @Override
    public String toString() {
        return this.source.toString();
    }
}
