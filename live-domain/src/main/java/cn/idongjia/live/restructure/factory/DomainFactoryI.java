package cn.idongjia.live.restructure.factory;

import cn.idongjia.live.restructure.domain.entity.Entity;

/**
 * @author lc on 2018/7/7.
 * @class cn.idongjia.live.restructure.factory.DomainFactoryI
 */
public interface DomainFactoryI< T extends Entity> {

    T create();
}
