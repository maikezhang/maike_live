package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.db.mybatis.po.LiveLikePO;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/2
 * Time: 下午3:23
 */
public class LiveLikeDTO extends BaseDTO<LiveLikePO> {
    public LiveLikeDTO(LiveLikePO entity) {
        super(entity);
    }

    public Long getId(){
        return entity.getId();
    }

    public Long getLiveId(){
        return entity.getLiveId();
    }

    public Long getUserId(){
        return entity.getUserId();
    }

    public Integer getStatus(){
        return entity.getStatus();
    }

    public Long getCreateTime(){
        return entity.getCreateTime();
    }

    public Long getUpdateTime(){
        return entity.getUpdateTime();
    }




}
