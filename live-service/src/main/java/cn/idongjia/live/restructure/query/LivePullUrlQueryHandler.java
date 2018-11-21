package cn.idongjia.live.restructure.query;

import cn.idongjia.live.db.mybatis.po.DLiveCloudPO;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.live.LivePullUrl;
import cn.idongjia.live.restructure.cloud.dcloud.DCloudRepo;
import cn.idongjia.live.restructure.dto.LivePullUrlDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.repo.LiveShowRepo;
import cn.idongjia.live.v2.pojo.query.CraftsLivePPUrl;
import cn.idongjia.log.Log;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static cn.idongjia.log.LogFactory.getLog;

/**
 * @author lc
 * @create at 2018/7/18.
 */
@Component
public class LivePullUrlQueryHandler {

    private static final Log logger = getLog(LivePullUrlQueryHandler.class);

    @Resource
    private LiveShowRepo liveShowRepo;
    @Resource
    private LiveRoomQueryHandler liveRoomQueryHandler;

    @Resource
    private DCloudRepo dCloudRepo;

    public CraftsLivePPUrl getPushPullUrl(Long liveId) {
        LiveShowDTO liveShowDTO = liveShowRepo.getById(liveId);
        String pushUrl = liveShowRepo.getLivePushUrl(liveId);
        LivePullUrlDTO livePullUrlDTO = liveRoomQueryHandler.getPullUrl(liveShowDTO.getRoomId());
        return livePullUrlDTO.toCraftsLivePPUrl(pushUrl);
    }

    public LivePullUrl getLivePullUrl(Long liveShowId) {
        LiveShowDTO liveShowDTO = null;
        try {
            liveShowDTO = liveShowRepo.getById(liveShowId);
        } catch (Exception e) {
            logger.error("查询直播失败{}", e);
            throw LiveException.failure("查询直播失败");
        }
        LivePullUrlDTO pullUrl = liveRoomQueryHandler.getPullUrl(liveShowDTO.getRoomId());
        return pullUrl.toDO();
    }

    public LivePullUrl getPullUrlsByUid(Long uid) {
        DLiveCloudPO po = dCloudRepo.getPullUrlsByUid(uid).toDO();
        LivePullUrl url = new LivePullUrl();
        url.setRtmpUrl(po.getRtmpUrl());
        url.setHlsUrl(po.getHlsUrl());
        url.setFlvUrl(po.getFlvUrl());
        return url;

    }

}
