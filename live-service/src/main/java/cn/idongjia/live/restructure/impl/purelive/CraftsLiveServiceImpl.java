package cn.idongjia.live.restructure.impl.purelive;

import cn.idongjia.live.api.purelive.CraftsPureLiveService;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.purelive.PureLive;
import cn.idongjia.live.pojo.purelive.PureLiveDetailDO;
import cn.idongjia.live.query.purelive.PureLiveSearch;
import cn.idongjia.live.restructure.biz.LiveShowBO;
import cn.idongjia.live.restructure.query.LivePureQueryHandler;
import cn.idongjia.live.restructure.query.LiveResourceQueryHandler;
import cn.idongjia.live.restructure.query.LiveShowQueryHandler;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.live.v2.pojo.ItemResource;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 匠人PC端接口实现
 *
 * @author zhang
 */
@Component("craftsPureLiveServiceImpl")
public class CraftsLiveServiceImpl implements CraftsPureLiveService {

    @Resource
    private LiveShowBO liveShowBO;

    @Resource
    private LiveResourceQueryHandler liveResourceQueryHandler;

    @Resource
    private LiveShowQueryHandler liveShowQueryHandler;

    @Resource
    private LivePureQueryHandler livePureQueryHandler;

    @Override
    public Long createCraftsPureLive(PureLive pureLive) {
        if (Objects.isNull(pureLive.getLiveType())) {
            throw LiveException.failure("直播类型不能为空");
        }
        return liveShowBO.createPureLive(pureLive, LiveConst.CRAFT_LIVE_CREATE);
    }

    @Override
    public boolean updateCraftsPureLive(Long pureLiveId, PureLive pureLive) {

        return liveShowBO.updatePureLive(pureLiveId, pureLive, LiveConst.CRAFT_LIVE_CREATE);
    }

    @Override
    public boolean deleteCraftsPureLive(Long pureLiveId) {

        return liveShowBO.delete(pureLiveId);
    }

    @Override
    public BaseList<PureLive> listCraftsPureLives(Long uid, PureLiveSearch pureLiveSearch) {
        pureLiveSearch.setType(LiveConst.TYPE_LIVE_NORMAL);
        pureLiveSearch.setOrder("desc");
        pureLiveSearch.setOrderBy("id ");
        return livePureQueryHandler.page(uid, pureLiveSearch);
    }

    @Override
    public PureLive getCraftsPureLive(Long pureLiveId) {
        return livePureQueryHandler.getPureLive(pureLiveId, null);
    }

    @Override
    public Integer countCraftsPureLives(Long uid, PureLiveSearch pureLiveSearch) {
        return livePureQueryHandler.countCraftsPureLives(uid, pureLiveSearch);
    }

    @Override
    public boolean setPureLiveUpdate(Long lid) {
//        return craftsPureLiveRepo.setPureLiveUpdate(lid);
        return false;
    }

    @Override
    public List<ItemResource> listItemResource(Long liveId) {
        if (Objects.isNull(liveId)) {
            throw LiveException.failure("直播id不合理");
        }
        return liveResourceQueryHandler.listItemResource(liveId);
    }

    @Override
    public Map<Long, String> addItemResource(Long liveId, List<PureLiveDetailDO> resources) {
        if (Objects.isNull(liveId)) {
            throw LiveException.failure("直播id不合理");
        }
        if (Utils.isEmpty(resources)) {
            return new HashMap<>();
        }
        return liveShowBO.addLiveResource(liveId, resources);
    }

    @Override
    public void deleteItemResource(Long liveId, List<PureLiveDetailDO> resources) {
        if (Objects.isNull(liveId)) {
            throw LiveException.failure("直播id不合理");
        }
        liveShowBO.delLiveResource(liveId, resources);
    }

    @Override
    public void manageItemResource(Long liveId, List<PureLiveDetailDO> resources) {
        if (Objects.isNull(liveId)) {
            throw LiveException.failure("直播不合理");
        }
        liveShowBO.reSortResource(liveId, resources);
    }
}
