package cn.idongjia.live.restructure.query;

import cn.idongjia.live.db.mybatis.po.LiveBookCountPO;
import cn.idongjia.live.db.mybatis.po.LiveBookPO;
import cn.idongjia.live.db.mybatis.query.DBLiveBookQuery;
import cn.idongjia.live.restructure.dto.LiveBookCountDTO;
import cn.idongjia.live.restructure.dto.LiveBookDTO;
import cn.idongjia.live.restructure.repo.LiveBookRepo;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.util.Utils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/6/11.
 */
@Component
public class LiveBookQueryHandler {
    @Resource
    private LiveBookRepo livePureBookRepo;

    @Async
    public Future<List<LiveBookDTO>> list(DBLiveBookQuery dbLiveBookQuery) {
        List<LiveBookPO> liveBookPOS = livePureBookRepo.list(dbLiveBookQuery).stream().map(LiveBookDTO::toDO).collect
                (Collectors.toList());
        List<LiveBookDTO> liveBookDTOS = liveBookPOS.stream().map(LiveBookDTO::new).collect(Collectors.toList());
        if (liveBookDTOS == null) {
            liveBookDTOS = new ArrayList<>();
        }
        return new AsyncResult<>(liveBookDTOS);
    }


    public Integer count(DBLiveBookQuery dbLiveBookQuery) {
        return livePureBookRepo.count(dbLiveBookQuery);
    }

    public Map<Long, LiveBookCountDTO> countMap(List<Long> liveIds) {
        List<LiveBookCountPO> liveBookCountPOS = livePureBookRepo.countMap(liveIds);
        if (Utils.isEmpty(liveBookCountPOS)) {
            return new HashMap();
        }
        return liveBookCountPOS.stream().collect(Collectors.toMap(LiveBookCountPO::getLiveId, v1 -> new LiveBookCountDTO(v1), (v1, v2) ->
                v1));
    }

    public boolean hasBook(Long uid,Long liveId){

        DBLiveBookQuery query= DBLiveBookQuery.builder()
                .liveIds(Arrays.asList(liveId))
                .userId(uid)
                .status(LiveConst.STATUS_BANNER_NORMAL)
                .build();
        List<LiveBookDTO> list = livePureBookRepo.list(query);
        if(CollectionUtils.isEmpty(list)){
            return false;
        }else{
            return true;
        }

    }

}
