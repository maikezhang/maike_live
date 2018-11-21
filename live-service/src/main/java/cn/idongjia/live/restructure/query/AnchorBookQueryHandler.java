package cn.idongjia.live.restructure.query;

import cn.idongjia.live.db.mybatis.query.DBAnchorBookQuery;
import cn.idongjia.live.restructure.dto.AnchorBookDTO;
import cn.idongjia.live.restructure.repo.AnchorBookRepo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author lc
 * @create at 2018/6/11.
 */
@Component
public class AnchorBookQueryHandler {
    @Resource
    private AnchorBookRepo liveAnchorBookRepo;

    @Async
    public Future<List<AnchorBookDTO>> list(DBAnchorBookQuery dbLiveBookQuery) {
        List<AnchorBookDTO> liveAnchorBookDTOS = liveAnchorBookRepo.list(dbLiveBookQuery);
        return new AsyncResult<>(liveAnchorBookDTOS);
    }

    public Integer count(DBAnchorBookQuery dbAnchorBookQuery) {
        return liveAnchorBookRepo.count(dbAnchorBookQuery);
    }

}
