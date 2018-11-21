package cn.idongjia.live.restructure.query;

import cn.idongjia.live.restructure.domain.entity.zoo.LiveZoo;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.manager.CloudManager;
import cn.idongjia.live.restructure.manager.ZooManager;
import cn.idongjia.live.restructure.repo.LiveShowRepo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/6/11.
 */
@Component
public class LiveZooQueryHandler {
    @Resource
    private ZooManager zooManager;


    @Resource
    private CloudManager cloudManager;

    @Resource
    private LiveShowRepo liveShowRepo;


    public LiveZoo get(Long zooId) {
        Optional<LiveZoo> liveZoo = zooManager.assembleLiveZoo(zooId);
        return liveZoo.get();
    }

    @Async
    public Future<Map<Long,Integer>> batchLiveCount(Map<Long,Long> liveZooMap) {
        Map<Long,Integer> liveCountMap=new HashMap<>();
        List<Long> zooIds=liveZooMap.values().stream().collect(Collectors.toList());
        Map<Long,Integer> batchCount= zooManager.batchZooCount(zooIds);
        liveZooMap.forEach((v1,v2)->{
            liveCountMap.put(v1,batchCount.get(v2));
        });
        return new AsyncResult(liveCountMap);
    }

    @Async
    public Future<Map<Long,Integer>> batchCount(List<Long> zids) {
        Map<Long,Integer> batchCount= zooManager.batchZooCount(zids);
        return new AsyncResult(batchCount);
    }

    @Async
    public Future<List<LiveZoo>> list(List<Long> zids) {
        List<LiveZoo> liveZoos = zooManager.list(zids);
        return new AsyncResult(liveZoos);
    }

    @Async
    public Future<Map<Long, LiveZoo>> map(List<Long> zids) {
        List<LiveZoo> liveZoos = zooManager.list(zids);
        Map<Long, LiveZoo> liveZooMap = liveZoos.stream().collect(Collectors.toMap(LiveZoo::getZid, v1 -> v1, (v1, v2) -> v1));
        return new AsyncResult(liveZooMap);
    }

    public Integer getOnlineNum(Long zid) {
        return zooManager.getZooRoomUserCountTimely(zid);
    }


    public Integer onlineNum(Long liveShowId) {
        LiveShowDTO liveShowDTO = liveShowRepo.getById(liveShowId);
        return getOnlineNum(liveShowDTO.getZooId());
    }
}
