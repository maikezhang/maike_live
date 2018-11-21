package cn.idongjia.live.restructure.domain.service;

import cn.idongjia.live.pojo.live.LiveShow;
import cn.idongjia.live.restructure.domain.DomainServiceI;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.domain.manager.LiveEntityManager;
import cn.idongjia.live.restructure.factory.LiveAbstractFactory;
import cn.idongjia.live.restructure.factory.LiveShowEntityFactory;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.restructure.manager.ZooManager;
import cn.idongjia.live.restructure.repo.LiveRoomRepo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author lc
 * @create at 2018/7/18.
 */
@Component
public class LiveDomainService extends DomainServiceI {

    @Resource
    private LiveEntityManager liveEntityManager;

    @Resource
    private LiveRoomRepo liveRoomRepo;

    @Resource
    private ZooManager zooManager;

    @Resource
    private UserManager userManager;


    /**
     * 更新直播  提供直播拍的更新
     *
     * @param liveShowId 直播id
     * @param liveShow
     * @return
     */
    public boolean updateLiveShow(Long liveShowId, LiveShow liveShow) {

        //加载更新前的直播数据
        LiveEntity entity = liveEntityManager.load(liveShowId);
        //如果主播id 不为空重新分配直播间
        if (liveShow.getUid() != null) {
            Long liveRoomId = liveRoomRepo.changeRoom(liveShow.getUid(), liveShow.getCloudType());
            liveShow.setRoomId(liveRoomId);
        }
        //直播实体工厂组装
        LiveAbstractFactory factory = new LiveShowEntityFactory();
        LiveEntity liveEntity = factory.getEntity(liveShow);
        entity.update(liveEntity);

        // 更新聊天室信息
        zooManager.modifyZooRoom(entity.getZid(), liveShow.getTitle(), liveShow.getZrc(), liveShow.getSuid());

        return true;
    }

    /**
     * 创建直播  提供直播拍创建直播
     *
     * @param liveShow 直播数据
     * @return 直播id
     */
    public Long createLiveShow(LiveShow liveShow) {

        Long zid = null;
        //验证主播是否存在
        userManager.getUser(liveShow.getUid());
        //获取或者创建聊天室id
        if (Objects.isNull(liveShow.getZid())) {
            zid = zooManager.addZooRoom(liveShow.getTitle(), liveShow.getZrc(), liveShow.getSuid());
        }

        liveShow.setZid(zid);
        //直播实体工厂组装
        LiveAbstractFactory factory = new LiveShowEntityFactory();
        LiveEntity liveEntity = factory.getEntity(liveShow);
        liveEntity.create();

        return liveEntity.getId();

    }
}
