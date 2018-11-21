package cn.idongjia.live.restructure.domain.entity.live;

import cn.idongjia.live.db.mybatis.po.LiveShowPO;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.live.LiveShow;
import cn.idongjia.live.pojo.purelive.PureLive;
import cn.idongjia.live.restructure.repo.LiveShowRepo;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.live.support.spring.SpringUtils;
import cn.idongjia.live.v2.pojo.CraftsLive;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 拍卖直播角色
 *
 * @author lc
 * @create at 2018/6/6.
 */
public class AuctionLiveRole implements LiveBaseRole {

    /**
     * 创建
     *
     * @param liveEntity
     */
    @Transactional(propagation = Propagation.NESTED, rollbackFor = Throwable.class)
    @Override
    public void create(LiveEntity liveEntity) {
        createBase(liveEntity);
    }

    @Override
    public void delete(long liveId) {
        LiveShowRepo liveShowRepo = SpringUtils.getBean("liveShowRepo", LiveShowRepo.class).orElseThrow(() -> LiveException.failure("获取liveShowRepo实例失败"));
        LiveShowPO po=new LiveShowPO();
        po.setId(liveId);
        po.setStatus(LiveConst.STATUS_LIVE_DEL);
        liveShowRepo.updateLiveShow(po);
    }


    @Override
    public boolean update(LiveEntity oldEntity, LiveEntity newEntity) {
       return updateLiveShow(oldEntity,newEntity);
    }

    @Override
    public void updateLiveOnline(LiveEntity entity, Integer online) {
        updateLiveShowOnline(entity.getId(),online);
    }



}
