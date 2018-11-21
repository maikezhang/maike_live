package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.db.mybatis.po.UserStageLivePO;

/**
 * @author lc
 * @create at 2018/7/7.
 */
public class UserStageLiveDTO extends BaseDTO<UserStageLivePO> {
    public UserStageLiveDTO(UserStageLivePO entity) {
        super(entity);
    }

    public void setLiveId(Long liveId) {
        entity.setLiveId(liveId);
    }

    public void setWeight(Integer weight) {
        entity.setWeight(weight);
    }

    public void setStage(int stage) {
        entity.setStage(stage);
    }

    public void setStatus(int status) {
        entity.setStatus(status);
    }

    public void setCreateTime(Long createTime){
        entity.setCreateTime(createTime);
    }
    public void setUpdateTime(Long updateTime){
        entity.setUpdateTime(updateTime);
    }

    public Long getId() {
        return entity.getId();
    }
}
