package cn.idongjia.live.restructure.factory;

import cn.idongjia.live.restructure.domain.entity.live.UserStageLiveE;
import org.springframework.stereotype.Component;

/**
 * @author lc
 * @create at 2018/7/7.
 */
@Component
public class UserStageDomainFactory implements DomainFactoryI<UserStageLiveE> {
    @Override
    public UserStageLiveE create() {
        return new UserStageLiveE();
    }
}
