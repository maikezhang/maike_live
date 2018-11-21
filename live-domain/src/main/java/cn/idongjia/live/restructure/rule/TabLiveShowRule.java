package cn.idongjia.live.restructure.rule;

import cn.idongjia.live.restructure.domain.entity.live.UserStageLiveE;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.repo.LiveShowRepo;
import cn.idongjia.live.support.BaseEnum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author lc
 * @create at 2018/7/8.
 */
@Component
public class TabLiveShowRule implements Rule<Long> {

    @Resource
    private LiveShowRepo liveShowRepo;

    @Override
    public boolean validate(Long liveId) {
        LiveShowDTO liveShowDTO = liveShowRepo.getById(liveId);
        if (liveShowDTO != null) {
            boolean isShow = liveShowDTO.getState() != LiveEnum.LiveState.FINISHED.getCode() && liveShowDTO.getOnline() ==
                    LiveEnum.LiveOnline.ONLINE.getCode();
            return isShow ;
        }
        return false;
    }
}
