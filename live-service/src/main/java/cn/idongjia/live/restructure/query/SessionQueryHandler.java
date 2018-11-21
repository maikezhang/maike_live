package cn.idongjia.live.restructure.query;

import cn.idongjia.divine.lib.pojo.response.live.general.GeneralLiveCO;
import cn.idongjia.live.api.live.pojo.LiveIndexResp;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.restructure.manager.DivineSearchManager;
import cn.idongjia.live.restructure.manager.OutcryManager;
import cn.idongjia.outcry.pojo.AuctionSession;
import cn.idongjia.outcry.pojo.AuctionSessionRel;
import cn.idongjia.outcry.pojo.Session4Live;
import cn.idongjia.search.pojo.query.live.LiveQuery;
import cn.idongjia.util.Utils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/6/12.
 */
@Component
public class SessionQueryHandler {

    @Resource
    private OutcryManager outcryManager;

    @Resource
    private DivineSearchManager divineSearchManager;

//    @Async
//    public Future<Map<Long, List<AuctionSessionRel>>> listAuctionRel(List<Long> sessionIds) {
//        Map<Long, List<AuctionSessionRel>> auctionSessionsRels = outcryManager.getAuctionSessionsRels(sessionIds, 5);
//        return new AsyncResult(auctionSessionsRels);
//    }


    public Map<Long, Session4Live> mapBySessionId(List<Long> liveIds) {

        List<GeneralLiveCO> generalLiveCOS = divineSearchManager.liveList(liveIds);
        if (!CollectionUtils.isEmpty(generalLiveCOS)) {
            List<Long> asids = generalLiveCOS.stream().map(GeneralLiveCO::getSessionId).collect(Collectors.toList());
            Map<Long, Session4Live> auctionSessionMap = outcryManager.mapByLiveId(asids);
            if (Utils.isEmpty(auctionSessionMap)) {
                return new HashMap<>();
            }
            return auctionSessionMap;
        }
        return new HashMap<>();
    }
    public Map<Long, Session4Live> mapByLiveId(List<Long> liveIds) {

        List<GeneralLiveCO> generalLiveCOS = divineSearchManager.liveList(liveIds);
        if (!CollectionUtils.isEmpty(generalLiveCOS)) {
            List<Long> asids = generalLiveCOS.stream().map(GeneralLiveCO::getSessionId).collect(Collectors.toList());
            Map<Long, Session4Live> auctionSessionMap = outcryManager.mapByLiveId(asids);
            if (Utils.isEmpty(auctionSessionMap)) {
                return new HashMap<>();
            }
            return auctionSessionMap;
        }
        return new HashMap<>();
    }



//    public Map<Long, AuctionSession> listLiveSession(List<Long> asids) {
//        return outcryManager.takeAuctionSessions(asids);
//    }


    public List<Long> listBookedSessionIds(List<Long> sessionids,Long userId){
        return outcryManager.takeBookedSessionIds(sessionids,userId);
    }

    public Map<Long, Session4Live> listSessionSimpleData(List<Long> asids) {
        return outcryManager.takeAuctionSessionSimpleData(asids);
    }
}
