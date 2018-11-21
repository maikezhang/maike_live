package cn.idongjia.live.restructure.domain.entity.live;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/7/6
 * Time: 下午6:18
 */
public class CraftsPurchaseLiveRole extends PureLiveRole {
    @Override
    public void create(LiveEntity liveEntity) {
        super.create(liveEntity);
    }

    @Override
    public boolean update(LiveEntity oldEntity, LiveEntity newEntity) {
        return super.update(oldEntity, newEntity);
    }

    @Override
    public void updateLiveOnline(LiveEntity entity, Integer online) {
        super.updateLiveOnline(entity, online);
    }
}
