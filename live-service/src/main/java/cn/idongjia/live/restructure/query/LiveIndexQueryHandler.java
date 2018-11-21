package cn.idongjia.live.restructure.query;

import cn.idongjia.live.restructure.dto.SearchIndexRespDTO;
import cn.idongjia.live.restructure.repo.LiveShowRepo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lc
 * @create at 2018/7/19.
 */
@Component
public class LiveIndexQueryHandler {


    @Resource
    private LiveShowRepo liveShowRepo;

    public List<SearchIndexRespDTO> listFromIndex(Integer page, Integer limit, Integer type) {

        return liveShowRepo.getLiveList(page, limit, type);
    }

}
