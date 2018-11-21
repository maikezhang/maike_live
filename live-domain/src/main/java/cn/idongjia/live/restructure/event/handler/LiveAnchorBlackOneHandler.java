package cn.idongjia.live.restructure.event.handler;

import cn.idongjia.live.db.mybatis.query.DBLiveShowQuery;
import cn.idongjia.live.db.mybatis.query.DBPageTabLiveQuery;
import cn.idongjia.live.restructure.domain.entity.tab.PageTabLiveE;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.event.LiveAnchorBlackData;
import cn.idongjia.live.restructure.event.LiveAnchorBlackEvent;
import cn.idongjia.live.restructure.repo.LiveShowRepo;
import cn.idongjia.live.restructure.repo.PageTabLiveRepo;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/6/8.
 */
@Component
public class LiveAnchorBlackOneHandler implements EventHandler<LiveAnchorBlackEvent>, WorkHandler<LiveAnchorBlackEvent> {

    private static final Log LOGGER = LogFactory.getLog(LiveAnchorBlackOneHandler.class);

    @Resource
    private LiveShowRepo liveShowRepo;

    @Resource
    private PageTabLiveRepo pageTabLiveRepo;

    @Override
    public void onEvent(LiveAnchorBlackEvent event, long sequence, boolean endOfBatch) throws Exception {
        onEvent(event);
    }

    @Override
    public void onEvent(LiveAnchorBlackEvent event) throws Exception {
        LiveAnchorBlackData data = event.getValue();

        List<Long> anchorIds = data.getAnchorIds();

        DBLiveShowQuery   query        = DBLiveShowQuery.builder().online(1).userIds(anchorIds).status(Arrays.asList(0)).build();
        List<LiveShowDTO> liveShowDTOS = liveShowRepo.listLiveShows(query);
        if (CollectionUtils.isEmpty(liveShowDTOS)) {
            return;
        }
        List<Long> liveIds = liveShowDTOS.stream().map(LiveShowDTO::getId).collect(Collectors.toList());

        DBPageTabLiveQuery tabLiveQuery  = DBPageTabLiveQuery.builder().liveIds(liveIds).build();
        List<PageTabLiveE> pageTabLiveES = pageTabLiveRepo.list(tabLiveQuery);
        if (CollectionUtils.isEmpty(pageTabLiveES)) {
            return;
        }

        pageTabLiveES.forEach(pageTabLiveE -> {
            pageTabLiveRepo.delete(pageTabLiveE.getId());
        });

    }
}
