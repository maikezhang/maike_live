package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.db.mybatis.po.LiveAnchorPO;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/27
 * Time: 下午5:38
 */
public class LiveAnchorDTO extends BaseDTO<LiveAnchorPO> {
    public LiveAnchorDTO(LiveAnchorPO entity) {
        super(entity);
    }

    public Long getId(){
        return entity.getId();
    }
    public Long getUserId(){
        return entity.getUserId();
    }
    public Integer getAnchorState(){
        return entity.getAnchorState();
    }
    public Long getUpdateTime(){
        return entity.getUpdateTime();
    }
    public Long getCreateTime(){
        return entity.getCreateTime();
    }

    public void setAnchorState(int anchorState) {
        entity.setAnchorState(anchorState);
    }

    public void setUpdateTime(long updateTime) {
        entity.setUpdateTime(updateTime);
    }
}
