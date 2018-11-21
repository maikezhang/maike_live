package cn.idongjia.live.restructure.query;

import cn.idongjia.live.db.mybatis.query.DBLiveBannerQuery;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.live.LiveBanner;
import cn.idongjia.live.query.live.LiveBannerSearch;
import cn.idongjia.live.restructure.biz.LiveBannerBO;
import cn.idongjia.live.restructure.dto.LiveBannerDTO;
import cn.idongjia.live.restructure.repo.LiveBannerRepo;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/6/13.
 */
@Component
public class LiveBannerQueryHandler {

    private static final Log LOGGER = LogFactory.getLog(LiveModuleQueryHander.class);
    @Autowired
    private LiveBannerRepo liveBannerRepo;

    @Async
    public Future<List<LiveBannerDTO>> list(DBLiveBannerQuery dbLiveBannerQuery) {
        List<LiveBannerDTO> liveBannerDTOS = liveBannerRepo.getBanner(dbLiveBannerQuery);
        return new AsyncResult<>(liveBannerDTOS);

    }

    public Integer count(DBLiveBannerQuery dbLiveBannerQuery) {
        return liveBannerRepo.count(dbLiveBannerQuery);
    }

    public List<LiveBannerDTO> getBanner(LiveBannerSearch liveBannerSearch) {
        LiveBannerBO.handleBaseSearch(liveBannerSearch);
        DBLiveBannerQuery dbLiveBannerQuery = QueryFactory.getInstance(liveBannerSearch);
        List<LiveBannerDTO> liveBannerDTOS = null;
        try {
            liveBannerDTOS = list(dbLiveBannerQuery).get();
//            List<Long> liveIds = liveBannerDTOS.stream().map(LiveBannerDTO::getLiveId).collect(Collectors.toList());
//            Map<Long, LiveShowDTO> liveShowDTOMap = liveShowQueryHandler.map(DBLiveShowQuery.builder().ids(liveIds).build()).get();
//            Map<Long, LivePureDTO> livePureDTOMap = livePureQueryHandler.map(DBLivePureQuery.builder().liveIds(liveIds).build()).get();
//
//            List<Long> userIds = liveShowDTOMap.values().stream().map(LiveShowDTO::getUserId).collect(Collectors.toList());
//            List<Long> zooIds = liveShowDTOMap.values().stream().map(LiveShowDTO::getZooId).collect(Collectors.toList());
//            Map<Long, CustomerVo> customerVoMap = userManager.takeBatchCustomer(userIds);
//            Map<Long, LiveZoo> liveZooMap = liveZooQueryHandler.map(zooIds).get();
            return liveBannerDTOS;

        } catch (Exception e) {
            LOGGER.error("查询banner失败{}", e);
            throw LiveException.failure("查询banner失败");
        }
    }

}
