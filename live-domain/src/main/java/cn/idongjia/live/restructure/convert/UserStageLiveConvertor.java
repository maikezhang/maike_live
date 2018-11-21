package cn.idongjia.live.restructure.convert;

import cn.idongjia.live.db.mybatis.po.UserStageLivePO;
import cn.idongjia.live.restructure.domain.entity.live.UserStageLiveE;
import cn.idongjia.live.restructure.enums.UserStageEnum;
import cn.idongjia.live.restructure.pojo.co.UserStageLiveDetailCO;
import cn.idongjia.live.restructure.rule.TabLiveShowRule;
import cn.idongjia.live.support.BaseEnum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 新老用户强运营数据转换工具
 *
 * @author lc
 * @create at 2018/7/7.
 */
@Component
public class UserStageLiveConvertor implements ConvertorI<UserStageLiveDetailCO, UserStageLiveE, UserStageLivePO> {

    @Resource
    private TabLiveShowRule tabLiveShowRule;

    @Override
    public UserStageLiveE dataToEntity(UserStageLivePO dataObject) {
        if (dataObject == null) {
            return null;
        }
        UserStageLiveE userStageLiveE = new UserStageLiveE();
        userStageLiveE.setId(dataObject.getId());
        userStageLiveE.setLiveId(dataObject.getLiveId());
        userStageLiveE.setStage(BaseEnum.parseInt2Enum(dataObject.getStage(), UserStageEnum.Stage.values()).orElse
                (null));
        userStageLiveE.setWeight(dataObject.getWeight());
        userStageLiveE.setCreateTime(dataObject.getCreateTime());
        userStageLiveE.setStatus(BaseEnum.parseInt2Enum(dataObject.getStage(), BaseEnum.DataStatus.values()).orElse
                (BaseEnum.DataStatus.NORMAL_STATUS));
        userStageLiveE.setUpdateTime(dataObject.getUpdateTime());
        userStageLiveE.setShowStatus(BaseEnum.parseInt2Enum(dataObject.getShowStatus(), BaseEnum.YesOrNo.values()).orElse(null));
        return userStageLiveE;
    }

    @Override
    public UserStageLivePO entityToData(UserStageLiveE entityObject) {
        if (entityObject == null) {
            return null;
        }
        UserStageLivePO userStageLivePO = new UserStageLivePO();
        userStageLivePO.setId(entityObject.getId());
        userStageLivePO.setLiveId(entityObject.getLiveId());
        userStageLivePO.setStage(entityObject.getStage().getCode());
        userStageLivePO.setWeight(entityObject.getWeight());
        userStageLivePO.setCreateTime(entityObject.getCreateTime());
        userStageLivePO.setStatus(entityObject.getStatus().getCode());
        userStageLivePO.setShowStatus(entityObject.getShowStatus().getCode());
        userStageLivePO.setUpdateTime(entityObject.getUpdateTime());
        return userStageLivePO;
    }

    @Override
    public UserStageLiveE clientToEntity(UserStageLiveDetailCO clientObject) {
        if (clientObject == null) {
            return null;
        }
        UserStageLiveE userStageLiveE = new UserStageLiveE();
        userStageLiveE.setWeight(clientObject.getWeight());
        userStageLiveE.setLiveId(clientObject.getLiveId());
        userStageLiveE.setStage(BaseEnum.parseInt2Enum(clientObject.getStage(), UserStageEnum.Stage.values()).orElse(null));
        boolean validate = tabLiveShowRule.validate(clientObject.getLiveId());
        userStageLiveE.setShowStatus(validate?BaseEnum.YesOrNo.YES:BaseEnum.YesOrNo.NO);
        return userStageLiveE;
    }

    @Override
    public UserStageLiveDetailCO entityToClient(UserStageLiveE entityObject) {
        if (entityObject == null) {
            return null;
        }
        UserStageLiveDetailCO userStageLiveDetailCO=new UserStageLiveDetailCO();
        userStageLiveDetailCO.setWeight(entityObject.getWeight());
        userStageLiveDetailCO.setStage(entityObject.getStage().getCode());
        userStageLiveDetailCO.setLiveId(entityObject.getLiveId());
        userStageLiveDetailCO.setId(entityObject.getId());
        return userStageLiveDetailCO;
    }
}
