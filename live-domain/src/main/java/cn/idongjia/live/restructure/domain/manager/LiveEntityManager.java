package cn.idongjia.live.restructure.domain.manager;

import cn.idongjia.live.db.mybatis.po.LivePurePO;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.dto.LivePureDTO;
import cn.idongjia.live.restructure.dto.LiveRoomDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.repo.LivePureRepo;
import cn.idongjia.live.restructure.repo.LiveRoomRepo;
import cn.idongjia.live.restructure.repo.LiveShowRepo;
import cn.idongjia.live.restructure.repo.TimeStrategyRepo;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.enumeration.LiveStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/7
 * Time: 下午8:35
 */

@Component
public class LiveEntityManager implements EntityManager<LiveEntity> {

    @Resource
    public LiveShowRepo liveShowRepo;


    @Resource
    private LivePureRepo livePureRepo;
    @Resource
    private LiveRoomRepo liveRoomRepo;


    @Resource
    private TimeStrategyRepo timeStrategyRepo;


    private LiveEntityManager() {


    }


    @Override
    public LiveEntity load(Long liveShowId) {
        if (liveShowId == null) {
            throw LiveException.failure("获取直播失败");
        }
        LiveShowDTO liveShowDTO = liveShowRepo.getById(liveShowId);
        LiveRoomDTO liveRoomDTO = liveRoomRepo.getById(liveShowDTO.getRoomId());
        LiveEntity liveEntity = new LiveEntity();

        if (liveShowDTO.getType().intValue() == LiveEnum.LiveType.PURE_LIVE.getCode()) {
            LivePureDTO pureLiveDTO = new LivePureDTO(new LivePurePO());

            pureLiveDTO = livePureRepo.getByLiveId(liveShowId);
            liveEntity.setDesc(pureLiveDTO.getDesc());
            liveEntity.setExemption(pureLiveDTO.getExemption());
            liveEntity.setPic(pureLiveDTO.getPic());
            liveEntity.setWeight(pureLiveDTO.getWeight());

        }
//        TimeStrategyDTO timeStrategyDTO = timeStrategyRepo.getStrategy(pureLiveDTO.getTimeStrategy());
        liveEntity.setCloudType(liveRoomDTO.getCloudType());
        liveEntity.setRoomId(liveRoomDTO.getId());
        liveEntity.setCreateTime(liveShowDTO.getCreateTime());
        liveEntity.setCreateTime(liveShowDTO.getCreateTime());
        liveEntity.setEndTime(liveShowDTO.getEndTime());
        liveEntity.setEstimatedEndTime(liveShowDTO.getEstimatedEndTime());
        liveEntity.setEstimatedStartTime(liveShowDTO.getEstimatedStartTime());
        liveEntity.setHuid(liveShowDTO.getUserId());
        liveEntity.setId(liveShowId);
//        liveEntity.setLivePlayType(BaseEnum.parseInt2Enum(timeStrategyDTO.getType(), LivePlayType.values()).orElse(LivePlayType.DAILY));
        liveEntity.setLiveType(BaseEnum.parseInt2Enum(liveShowDTO.getType(), LiveEnum.LiveType.values()).orElse(null));
        //        liveEntity.setLiveVideoCover();
//        liveEntity.setPeriodEndTm(timeStrategyDTO.getPeriodEndTime());
//        liveEntity.setPeriodStartTm(timeStrategyDTO.getPeriodStartTime());
        liveEntity.setPreviewTime(liveShowDTO.getPreviewTime());
        liveEntity.setRecommendWeight(liveShowDTO.getGeneralWeight());
        liveEntity.setScreenDirection(liveShowDTO.getScreenDirection());
        liveEntity.setStartTime(liveShowDTO.getStartTime());
        liveEntity.setState(BaseEnum.parseInt2Enum(liveShowDTO.getState(), LiveEnum.LiveState.values()).orElse(null));
        liveEntity.setStatus(BaseEnum.parseInt2Enum(liveShowDTO.getLiveStatus(), LiveStatus.values()).orElse(null));
        liveEntity.setOnline(BaseEnum.parseInt2Enum(liveShowDTO.getOnline(), LiveEnum.LiveOnline.values()).orElse(null));
        liveEntity.setTitle(liveShowDTO.getTitle());
//        liveEntity.setUrl(pureLiveDTO.getVideoUrl());
        liveEntity.setShowDesc(liveShowDTO.getShowDesc());
        liveEntity.setZid(liveShowDTO.getZooId());
        return liveEntity;
    }


}
