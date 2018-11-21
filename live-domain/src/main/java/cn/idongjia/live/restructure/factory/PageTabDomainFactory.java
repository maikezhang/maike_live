package cn.idongjia.live.restructure.factory;

import cn.idongjia.live.restructure.domain.entity.tab.PageTabE;
import org.springframework.stereotype.Component;

/**
 * @author lc
 * @create at 2018/7/7.
 */
@Component
public class PageTabDomainFactory implements DomainFactoryI<PageTabE> {
    @Override
    public PageTabE create() {
        return new PageTabE();
    }
}
