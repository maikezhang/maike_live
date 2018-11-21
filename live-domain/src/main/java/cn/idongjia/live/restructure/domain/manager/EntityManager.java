package cn.idongjia.live.restructure.domain.manager;

/**
 * @author lc on 2018/7/18.
 * @class cn.idongjia.live.restructure.domain.manager.EntityManager
 */
public interface EntityManager<T> {
    public T load(Long id);

}
