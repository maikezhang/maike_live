package cn.idongjia.live.restructure.query;

import cn.idongjia.live.db.mybatis.query.DBAnchorBlackWhiteQuery;
import cn.idongjia.live.restructure.dto.AnchorBlackWhiteDTO;
import cn.idongjia.live.restructure.repo.AnchorBlackWhiteRepo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/2
 * Time: 下午4:59
 */
@Component
public class AnchorBlackWhiteQueryHandler {

    @Resource
    private AnchorBlackWhiteRepo anchorBlackWhiteRepo;


    @Async
    public Future<List<AnchorBlackWhiteDTO>> list(DBAnchorBlackWhiteQuery query){

        List<AnchorBlackWhiteDTO> anchorBlackWhiteDTOS = anchorBlackWhiteRepo.list(query);
        return new AsyncResult<>(anchorBlackWhiteDTOS);
    }

    public AnchorBlackWhiteDTO get(long anchorId){

        return anchorBlackWhiteRepo.get(anchorId);

    }
}
