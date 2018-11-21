package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.db.mybatis.po.LiveResourceCountPO;

/**
 * @author lc
 * @create at 2018/6/12.
 */
public class LiveResourceCountDTO extends BaseDTO<LiveResourceCountPO> {
    public LiveResourceCountDTO(LiveResourceCountPO entity) {
        super(entity);
    }

    public int getCount() {
        return entity.getCount();
    }
}
