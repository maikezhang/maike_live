package cn.idongjia.live.restructure.domain.entity.live;

import cn.idongjia.live.restructure.domain.entity.Entity;
import cn.idongjia.live.restructure.enums.UserStageEnum;
import cn.idongjia.live.restructure.repo.UserStageLiveRepo;
import cn.idongjia.live.restructure.rule.UserStageLiveDuplicateRule;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.spring.SpringUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * 新老用户关联直播强运营实体
 *
 * @author lc
 * @create at 2018/7/7.
 */
@Getter
@Setter
public class UserStageLiveE extends Entity {


    /**
     * id
     */
    private Long id;
    /**
     * 直播id
     */
    private Long liveId;

    /**
     * 权重
     */
    private Integer weight;

    /**
     * 用户阶段
     */
    private UserStageEnum.Stage stage;

    /**
     * 显示状态
     */
    private BaseEnum.YesOrNo showStatus;

    private BaseEnum.DataStatus status;


    private UserStageLiveRepo userStageLiveRepo = SpringUtils.takeBean("userStageLiveRepo", UserStageLiveRepo.class);

    private UserStageLiveDuplicateRule userStageLiveDuplicateRule = SpringUtils.takeBean("userStageLiveDuplicateRule", UserStageLiveDuplicateRule.class);


    public Integer save() {
        boolean validate = userStageLiveDuplicateRule.validate(this);
        if (!validate) {
            return 0;
        }
        return userStageLiveRepo.add(this);
    }

    public int detete() {
        status = BaseEnum.DataStatus.DELETE_STATUS;
        return userStageLiveRepo.update(this);
    }

    public int update() {
        return userStageLiveRepo.update(this);
    }
}
