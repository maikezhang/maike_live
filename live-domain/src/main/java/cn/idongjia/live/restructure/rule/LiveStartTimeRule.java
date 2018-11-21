package cn.idongjia.live.restructure.rule;

import cn.idongjia.common.context.DJContext;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author lc
 * @create at 2018/7/17.
 */
@Component
public class LiveStartTimeRule implements Rule<LiveEntity> {
    @Resource
    private ConfigManager configManager;

    @Override
    public boolean validate(LiveEntity liveEntity) {
        if (!Objects.equals(DJContext.getAF(), 1) && Utils.getCurrentMillis() < liveEntity.getEstimatedStartTime()
                - TimeUnit.MINUTES.toMillis(configManager.getMaxPreStartTime())) {
            return true;
        }
        return false;
    }
}
