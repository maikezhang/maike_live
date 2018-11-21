package cn.idongjia.live.restructure.rule;

import cn.idongjia.live.db.mybatis.query.DBUserStageLiveQuery;
import cn.idongjia.live.restructure.domain.entity.live.UserStageLiveE;
import cn.idongjia.live.restructure.repo.UserStageLiveRepo;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author lc
 * @create at 2018/7/8.
 */
@Component
public class UserStageLiveDuplicateRule implements Rule<UserStageLiveE> {

    @Resource
    private UserStageLiveRepo userStageLiveRepo;

    @Override
    public boolean validate(UserStageLiveE userStageLiveE) {
        List<UserStageLiveE> userStageLiveES = userStageLiveRepo.list(DBUserStageLiveQuery.builder().status(BaseEnum.DataStatus.NORMAL_STATUS.getCode()).liveIds(Arrays.asList(userStageLiveE.getLiveId())).stages(Arrays.asList(userStageLiveE.getStage().getCode()))
                .status(BaseEnum.DataStatus
                .NORMAL_STATUS.getCode()).build());

        return Utils.isEmpty(userStageLiveES);
    }
}
