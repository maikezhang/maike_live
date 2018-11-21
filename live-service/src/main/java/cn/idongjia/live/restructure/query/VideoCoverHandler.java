package cn.idongjia.live.restructure.query;

import cn.idongjia.live.db.mybatis.query.DBLiveVideoCoverQuery;
import cn.idongjia.live.restructure.dto.VideoCoverDTO;
import cn.idongjia.live.restructure.repo.VideoCoverRepo;
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
public class VideoCoverHandler {

    @Resource
    private VideoCoverRepo videoCoverRepo;

    @Async
    public Future<List<VideoCoverDTO>> list(DBLiveVideoCoverQuery dbLiveVideoCoverQuery) {
        List<VideoCoverDTO> videoCoverDTOS = videoCoverRepo.list(dbLiveVideoCoverQuery);
        return new AsyncResult(videoCoverDTOS);
    }

    public Map<Long, VideoCoverDTO> map(DBLiveVideoCoverQuery dbLiveVideoCoverQuery) {
        List<VideoCoverDTO> videoCoverDTOS = videoCoverRepo.list(dbLiveVideoCoverQuery);
        return  videoCoverDTOS.stream().collect(Collectors.toMap(VideoCoverDTO::getLiveId, v1 -> v1, (v1, v2) -> v1));
    }
    public VideoCoverDTO getById(Long liveId) {
        return videoCoverRepo.getByLiveShowID(liveId);
    }
}
