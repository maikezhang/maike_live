package cn.idongjia.live.restructure.impl.purelive;

import cn.idongjia.live.api.purelive.PureLiveService;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.purelive.PureLive;
import cn.idongjia.live.pojo.purelive.PureLive4Article;
import cn.idongjia.live.pojo.purelive.PureLiveDO;
import cn.idongjia.live.pojo.purelive.PureLiveDetailDO;
import cn.idongjia.live.query.purelive.PureLiveSearch;
import cn.idongjia.live.restructure.biz.LiveShowBO;
import cn.idongjia.live.restructure.query.LiveFeedsQueryHandler;
import cn.idongjia.live.restructure.query.LivePreviewQueryHandler;
import cn.idongjia.live.restructure.query.LivePureQueryHandler;
import cn.idongjia.live.restructure.query.LiveResourceQueryHandler;
import cn.idongjia.live.restructure.query.LiveShowQueryHandler;
import cn.idongjia.live.restructure.v2.support.enumeration.LiveResourceType;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.outcry.exception.AuctionException;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component("pureLiveServiceImpl")
public class PureLiveServiceImpl implements PureLiveService {


    @Resource
    private LiveShowBO liveShowBO;

    @Resource
    private LiveResourceQueryHandler liveResourceQueryHandler;

    @Resource
    private LiveShowQueryHandler liveShowQueryHandler;

    @Resource
    private LivePreviewQueryHandler livePreviewQueryHandler;

    @Resource
    private LivePureQueryHandler livePureQueryHandler;

    @Resource
    private LiveFeedsQueryHandler liveFeedsQueryHandler;

    @Override
    public Long createPureLive(PureLive pureLive) {

        if (Objects.isNull(pureLive.getLiveType())) {
            throw LiveException.failure("直播类型不能为空");
        }
        return liveShowBO.createPureLive(pureLive, LiveConst.BACK_LIVE_CREATE);
    }

    @Override
    public boolean updatePureLiveById(Long pureLiveId, PureLive pureLive) {

        return liveShowBO.updatePureLive(pureLiveId, pureLive, LiveConst.BACK_LIVE_CREATE);
    }

    @Override
    public boolean deletePureLiveById(Long pureLiveId) {

        return liveShowBO.delete(pureLiveId);
    }

    @Override
    public PureLive getPureLiveById(Long pureLiveId) {
        return livePureQueryHandler.getPureLive(pureLiveId, null);
    }

    @Override
    public List<PureLive> listPureLive(PureLiveSearch pureLiveSearch) {
        return livePureQueryHandler.list(pureLiveSearch);
    }

    @Override
    public Integer getPureLiveCount(PureLiveSearch pureLiveSearch) {
        return livePureQueryHandler.count(pureLiveSearch);
    }

    @Override
    public BaseList<PureLive> listPureLiveWithCount(PureLiveSearch pureLiveSearch) {
        if (Utils.isEmpty(pureLiveSearch.getOrderBy())) {
            pureLiveSearch.setOrderBy("id");
            pureLiveSearch.setOrder("desc");
        }
        return livePureQueryHandler.page(pureLiveSearch);
    }

    @Override
    public Map<String, Object> getPureLiveUserCount(Long pureLiveId) {
        return liveShowQueryHandler.getLiveOnlineNumber(pureLiveId);
    }

    @Override
    public boolean modifyPureLiveZrc(Long pureLiveId, Integer zrc) {
        return liveShowBO.modifyLiveShowZrc(pureLiveId, zrc);
    }

    @Override
    public List<Map<String, Object>> getPureLiveResources(List<Long> resIds, Integer resourceType) {
        if (Objects.isNull(resourceType)) {
            resourceType = LiveResourceType.TEMPLATE.getCode();
        }
        return liveResourceQueryHandler.urlMap(resIds, resourceType);
    }

    @Override
    public PureLive4Article acquirePureLiveData(Long pureLiveId) {
        return livePureQueryHandler.get4Article(pureLiveId);
    }

    @Override
    public List<Long> listPureLivesByUid(Long uid) {
        return liveShowQueryHandler.idsByUid(uid);
    }

    @Override
    public Long getTemplateByLid(Long pureLiveId) {
        return liveResourceQueryHandler.getTemplateId(pureLiveId);
    }

    @Override
    public Map<String, Object> getFeeds(Long uid, PureLiveSearch pureLiveSearch) {
        if (pureLiveSearch.getLimit() == null) {
            pureLiveSearch.setLimit(10);
        }
        return liveFeedsQueryHandler.getFeeds(uid, pureLiveSearch);
    }

    @Override
    public List<Object> getClassifiedFeeds(Long tid, PureLiveSearch pureLiveSearch) {
        if (pureLiveSearch.getLimit() == null) {
            pureLiveSearch.setLimit(10);
        }
        return liveFeedsQueryHandler.getClassifiedFeeds(tid, pureLiveSearch);
    }

    @Override
    public List<Object> getBookCenterFeeds(Long uid, PureLiveSearch pureLiveSearch) {
        if (pureLiveSearch.getLimit() == null) {
            pureLiveSearch.setLimit(10);
        }
        return liveFeedsQueryHandler.getBookCenterFeeds(uid, pureLiveSearch);
    }

    @Override
    public List<Map<String, Object>> getForeShows(Long uid, String lastDate) {
        return livePreviewQueryHandler.getForeShows(uid, lastDate);
    }

    @Override
    public PureLiveDO getBasePureLive(Long lid) {
        return livePureQueryHandler.getPureLiveDO(lid);
    }

    @Override
    public List<Object> listPureLiveByCraftsUid(Long uid, PureLiveSearch pureLiveSearch, boolean isOld) {
        if (isOld) {
            return liveFeedsQueryHandler.listPureLiveByCraftsIdOld(uid, pureLiveSearch);
        } else {
            return liveFeedsQueryHandler.listPureLiveByCraftsId(uid, pureLiveSearch);
        }
    }

    @Override
    public PureLive getPureLiveByLidWithLock(Long lid) {
        return livePureQueryHandler.getPureLive(lid, null);
    }

    @Override
    public PureLive getPureLiveByLidWithUid(Long uid, Long lid) {
        return livePureQueryHandler.getPureLive(lid, uid);
    }

    @Override
    public boolean replicateZooMessage(Long fromLid, Long toLid) {
        return liveShowBO.replicateZooMessage(fromLid, toLid);
    }

    @Override
    public Map<String, Object> listItemResource(Long liveId) {
        if (Objects.isNull(liveId)) {
            throw LiveException.failure("直播id不能为空");
        }
        return liveResourceQueryHandler.mapItemResource(liveId);
    }

    @Override
    public Map<Long, String> addResource(Long liveId, List<PureLiveDetailDO> resources) {
        if (Objects.isNull(liveId)) {
            throw LiveException.failure("直播id不合理");
        }
        return liveShowBO.addLiveResource(liveId, resources);
    }

    @Override
    public void reSortResource(Long liveId, List<PureLiveDetailDO> resources) {
        if (Objects.isNull(liveId)) {
            return;
        }
        liveShowBO.reSortResource(liveId, resources);
    }

    @Override
    public void deleteResource(Long liveId, List<PureLiveDetailDO> resources) {
        if (Objects.isNull(liveId)) {
            return;
        }
        liveShowBO.delLiveResource(liveId, resources);
    }

    @Override
    public void liveAutoOnline(Long liveId, Integer autoLine) {
        if (Objects.isNull(liveId)) {
            return;
        }
        if (Objects.isNull(autoLine)) {
            autoLine = 0;
        }
        liveShowBO.autoOnline(liveId, autoLine);
    }


    @Override
    public void pushUserItemOperation(Long userId, Long itemId, Integer type) {
        if (type == null) {
            throw AuctionException.failure("type is null");
        }
        liveShowBO.pushUserItemOperation(userId, itemId, type);
    }
}
