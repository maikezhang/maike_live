package cn.idongjia.live.restructure.query;

import cn.idongjia.live.db.mybatis.query.DBPlayBackQuery;
import cn.idongjia.live.restructure.dto.PlayBackDTO;
import cn.idongjia.live.restructure.repo.PlayBackRepo;
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
public class PlayBackQueryHandler {

    @Resource
    private PlayBackRepo playBackRepo;

    @Async
    public Future<List<PlayBackDTO>> list(DBPlayBackQuery dbPlayBackCoverQuery) {
        List<PlayBackDTO> playBackDTOS = playBackRepo.list(dbPlayBackCoverQuery);
        return new AsyncResult(playBackDTOS);
    }

    @Async
    public Future<Map<Long, List<PlayBackDTO>>> map(DBPlayBackQuery dbPlayBackCoverQuery) {
        List<PlayBackDTO> playBackDTOS = playBackRepo.list(dbPlayBackCoverQuery);
        Map<Long, List<PlayBackDTO>> playBackDTOMap = playBackDTOS.stream().collect(Collectors.groupingBy(PlayBackDTO::getLiveId));
        return new AsyncResult(playBackDTOMap);
    }

    public int count(DBPlayBackQuery dbPlayBackQuery) {
        return playBackRepo.count(dbPlayBackQuery);
    }


}
