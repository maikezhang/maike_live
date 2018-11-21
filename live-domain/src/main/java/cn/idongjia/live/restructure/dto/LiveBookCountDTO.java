package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.db.mybatis.po.LiveBookCountPO;
import cn.idongjia.live.db.mybatis.po.LiveBookPO;

/**
 * 直播订阅DTO
 *
 * @author lc
 * @create at 2018/7/6.
 */
public class LiveBookCountDTO extends BaseDTO<LiveBookCountPO>
{
    public LiveBookCountDTO(LiveBookCountPO entity) {
        super(entity);
    }

    public Integer getCount() {
        return entity.getCount();
    }

    public void setCount(int count) {
        entity.setCount(count);
    }
    public void setLiveId(Long id){
        entity.setLiveId(id);
    }
}
