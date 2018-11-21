package cn.idongjia.live.restructure.manager;

import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.outcry.pojo.AuctionSession;
import cn.idongjia.outcry.pojo.AuctionSessionRel;
import cn.idongjia.outcry.pojo.Session4Live;
import cn.idongjia.outcry.service.BookService;
import cn.idongjia.outcry.service.SessionQueryService;
import cn.idongjia.outcry.service.SessionService;
import cn.idongjia.outcry.support.BaseEnum;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

/**
 * 拍卖管理
 *
 * @author lc
 * @create at 2017/12/20.
 */
@Component
public class OutcryManager {

    @Resource
    private SessionService outcrySessionService;

    @Resource
    private SessionQueryService sessionQueryService;

    @Resource
    private BookService bookService;


    private static final Log LOGGER = LogFactory.getLog(OutcryManager.class);

    /**
     * 获取专场详情
     *
     * @param asid
     * @return
     */
    public Optional<AuctionSession> takeAuctionSession(long asid) {
        try {
            return Optional.ofNullable(outcrySessionService.getAuctionSession(asid));

        } catch (Exception e) {
            LOGGER.warn("调用outcry服务失败" + e.getMessage());

            return Optional.empty();
        }
    }

    public Map<Long, Session4Live> takeAuctionSessions(List<Long> asids) {

        if (Utils.isEmpty(asids)) {
            return new HashMap<>();
        }
        try {
            TakeAuctionSessionTask auctionSessionTask = new TakeAuctionSessionTask(asids);
            List<Session4Live> auctionSessions = auctionSessionTask.compute();
            Map<Long, Session4Live> sessionMap = new HashMap<>();
            sessionMap = auctionSessions.stream().collect(Collectors.toMap(Session4Live::getId,
                    auctionSession ->
                            auctionSession));
            return sessionMap;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public Map<Long, Session4Live> mapByLiveId(List<Long> asids) {

        if (Utils.isEmpty(asids)) {
            return new HashMap<>();
        }
        try {
            TakeAuctionSessionTask auctionSessionTask = new TakeAuctionSessionTask(asids);
            List<Session4Live> auctionSessions = auctionSessionTask.compute();
            Map<Long, Session4Live> sessionMap = new HashMap<>();
            sessionMap = auctionSessions.stream().collect(Collectors.toMap(Session4Live::getLiveId,
                    auctionSession ->
                            auctionSession));
            return sessionMap;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }


    public Optional<AuctionSession> takeAuctionSessionWithoutRel(long asid) {
        try {
            return Optional.ofNullable(outcrySessionService.getAuctionSessionNoRels(asid));
        } catch (Exception e) {
            return Optional.empty();
        }

    }

    public Map<Long, List<AuctionSessionRel>> getAuctionSessionsRels(List<Long> sessionIds, int i) {
        List<AuctionSession> auctionSessions = outcrySessionService.getAuctionSessions(sessionIds, 5);

        if (!Utils.isEmpty(auctionSessions)) {
            Map<Long, List<AuctionSessionRel>> auctionRelMap = auctionSessions.stream().collect(Collectors.toMap
                    (AuctionSession::getAsid, u1 -> {
                        List<AuctionSessionRel> relates = u1.getRelates();
                        return relates == null ? new ArrayList<>() : relates;
                    }, (u1, u2) -> u1));
            return auctionRelMap;
        }
        return new HashMap<>();
    }

    private class TakeAuctionSessionTask extends RecursiveTask<List<Session4Live>> {
        private List<Long> asids;

        TakeAuctionSessionTask(List<Long> asids) {
            this.asids = asids;
        }

        @Override
        protected List<Session4Live> compute() {
            List<Session4Live> auctionSessions = new ArrayList<>();
            if (asids.size() > 5) {
                int size = asids.size();
                int idx = size / 2;
                List<Long> firstSubList = asids.subList(0, idx);

                List<Long> secondSubList = asids.subList(idx, size);
                TakeAuctionSessionTask firstTask = new TakeAuctionSessionTask(firstSubList);
                TakeAuctionSessionTask secondTask = new TakeAuctionSessionTask(secondSubList);
                firstTask.fork();
                secondTask.fork();
                List<Session4Live> firstSessions = firstTask.join();
                List<Session4Live> secondSessions = secondTask.join();
                if (!Utils.isEmpty(firstSessions)) {
                    auctionSessions.addAll(firstSessions);
                }
                if (!Utils.isEmpty(secondSessions)) {
                    auctionSessions.addAll(secondSessions);
                }
                return auctionSessions;
            } else {
                auctionSessions = sessionQueryService.getBatchSession4Live(asids, 5);
            }
            return auctionSessions;


        }

    }

    public Map<Long,Session4Live> takeAuctionSessionSimpleData(List<Long> asid){

        if(CollectionUtils.isEmpty(asid)){
            return new HashMap<>();
        }
        List<Session4Live> batchSession4Lives=new ArrayList<>();
        try {
             batchSession4Lives = sessionQueryService.getBatchSession4Live(asid, 5);

        }catch (Exception e){
            LOGGER.info("调用cn.idongjia.outcry.service.SessionQueryService.getBatchSession4Live接口失败，{}",e);
        }
        if(CollectionUtils.isEmpty(batchSession4Lives)){
            return new HashMap<>();
        }

        return batchSession4Lives.stream().collect(Collectors.toMap(Session4Live::getId,batchSession4Live->batchSession4Live));

    }


    public List<Session4Live> takeLiveIdByAsid(List<Long> asids){
        if(CollectionUtils.isEmpty(asids)){
            return new ArrayList<>();
        }
        List<Session4Live> auctionSessions = sessionQueryService.getBatchSession4Live(asids, 0);
        if(CollectionUtils.isEmpty(auctionSessions)){
            return new ArrayList<>();
        }

        return auctionSessions;

    }

    public List<Long> bookUids(Long asid){
        try {

            List<Long> bookUids=bookService.bookUids(asid,2);
            return bookUids;
        }catch (Exception e){
            LOGGER.warn("获取直播专场的订阅用户失败{}",e);
        }

        return new ArrayList<>();
    }

    public List<Long> takeBookedSessionIds(List<Long> sessionIds,Long uid){
        return bookService.getUserBookContents(sessionIds,2,uid);

    }

    public Map<Long, Integer> mapBookSessionUidBySessionId(List<Long> sessionIds){

        try {
            Map<Long, Integer> batchBookUserCount = bookService.getBatchBookUserCount(sessionIds, 2);
            return batchBookUserCount;
        }catch (Exception e){
            LOGGER.warn("调用cn.idongjia.outcry.service.bookService.getBatchBookUserCount接口失败{}",e);
        }
        return new HashMap<>();
    }
}

