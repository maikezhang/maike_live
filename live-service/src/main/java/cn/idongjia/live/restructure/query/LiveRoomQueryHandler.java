package cn.idongjia.live.restructure.query;

import cn.idongjia.live.db.mybatis.query.DBLiveRoomQuery;
import cn.idongjia.live.pojo.live.LiveCloudStat;
import cn.idongjia.live.pojo.live.LivePullUrl;
import cn.idongjia.live.pojo.live.LiveRecord;
import cn.idongjia.live.restructure.cloud.qcloud.QCloudClient;
import cn.idongjia.live.restructure.dto.LivePullUrlDTO;
import cn.idongjia.live.restructure.dto.LiveRoomDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.manager.CloudManager;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.restructure.repo.LiveRoomRepo;
import cn.idongjia.live.restructure.repo.LiveShowRepo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/6/11.
 */
@Component
public class LiveRoomQueryHandler {
    @Resource
    private LiveRoomRepo liveRoomRepo;


    @Resource
    private CloudManager cloudManager;

    @Resource
    private UserManager userManager;

    @Resource
    private LiveShowRepo liveShowRepo;

    @Async
    public Future<List<LiveRoomDTO>> list(DBLiveRoomQuery dbLiveRoomQuery) {
        List<LiveRoomDTO> liveRoomDTOS = liveRoomRepo.list(dbLiveRoomQuery);
        return new AsyncResult(liveRoomDTOS);
    }

    @Async
    public Future<Map<Long, LiveRoomDTO>> map(DBLiveRoomQuery dbLiveRoomQuery) {
        List<LiveRoomDTO> liveRoomDTOS = liveRoomRepo.list(dbLiveRoomQuery);
        Map<Long, LiveRoomDTO> liveRoomDTOMap = liveRoomDTOS.stream().collect(Collectors.toMap(LiveRoomDTO::getId, v1 -> v1, (v1, v2) -> v2));
        return new AsyncResult(liveRoomDTOMap);
    }


    public LivePullUrlDTO getPullUrl(Long roomId) {
        LiveRoomDTO liveRoomDTO = liveRoomRepo.getById(roomId);
        if (liveRoomDTO == null) {
            return null;
        }
        LivePullUrl livePullUrl = cloudManager.getPlayUrlByCloudType(liveRoomDTO.getCloudId(), liveRoomDTO.getCloudType());
        return new LivePullUrlDTO(livePullUrl);
    }


    @Async
    public Future<Map<Long, LivePullUrlDTO>> pullUrlMap(List<LiveRoomDTO> liveRoomDTOS) {
        Map<Long, LivePullUrlDTO> livePullUrlDTOMap = liveRoomDTOS.stream().collect(Collectors.toMap(LiveRoomDTO::getId, liveRoomDTO -> {
            LivePullUrl livePullUrl = cloudManager.getPlayUrlByCloudType(liveRoomDTO.getCloudId(), liveRoomDTO.getCloudType());
            return new LivePullUrlDTO(livePullUrl);
        }, (v1, v2) -> v1));
        return new AsyncResult<>(livePullUrlDTOMap);
    }

    public String getPushUrlByUid(Long uid) {
        return liveRoomRepo.getPushUrlByUid(uid);
    }

    /**
     * 根据电话查询推流地址
     *
     * @param mobile
     * @return
     */
    public String getPushUrlByMobile(String mobile) {
        Long uid = userManager.getUserIdByMobile(mobile);
        if (null == uid) {
            return null;
        }
        return getPushUrlByUid(uid);
    }

    public LiveRoomDTO get(Long roomId) {
        return liveRoomRepo.getById(roomId);
    }

    public List<LiveRecord> records(Long uid) {
        return liveRoomRepo.listRecordsByUid(uid);

    }

    public LiveCloudStat getLiveFlowStatic(Long liveId) {
        LiveShowDTO liveShowDTO = liveShowRepo.getById(liveId);
        LiveRoomDTO roomDTO = liveRoomRepo.getById(liveShowDTO.getRoomId());
        QCloudClient qCloudClient = new QCloudClient();
        return qCloudClient.getLiveStaticStatus(roomDTO.getCloudId());
    }

    public String getPushUrl(Long lid, Long txTime) {
        LiveShowDTO liveShowDTO = liveShowRepo.getById(lid);
        LiveRoomDTO liveRoomDTO = get(liveShowDTO.getRoomId());
        return liveShowRepo.getLivePushUrl(liveRoomDTO.getCloudId(), txTime);
    }

    public Integer getCloudType(Long liveShowId) {
        LiveShowDTO liveShowDTO = liveShowRepo.getById(liveShowId);
        LiveRoomDTO liveRoomDTO = get(liveShowDTO.getRoomId());
        return liveRoomDTO.getCloudType();
    }
    /**
     * 根据直播id获取推流地址
     *
     * @param liveShowId 直播id
     * @return 推流地址
     */
    public String getPushUrl(Long liveShowId) {
        return liveShowRepo.getLivePushUrl(liveShowId);
    }


}
