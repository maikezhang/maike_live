package cn.idongjia.live.restructure.factory;

import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;

import java.util.Optional;

/**
 * @author lc
 * @create at 2018/7/17.
 */
public abstract class LiveAbstractFactory<T> {
    /**
     * 获取直播
     * @param t
     * @return
     */
    public abstract LiveEntity getEntity(T t);
}
