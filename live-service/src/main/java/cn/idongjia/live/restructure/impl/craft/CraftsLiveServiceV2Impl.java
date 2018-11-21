package cn.idongjia.live.restructure.impl.craft;

import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.query.LiveSearch;
import cn.idongjia.live.restructure.biz.LiveShowBO;
import cn.idongjia.live.restructure.query.LiveCraftQueryHandler;
import cn.idongjia.live.restructure.query.LivePullUrlQueryHandler;
import cn.idongjia.live.restructure.query.LiveResourceQueryHandler;
import cn.idongjia.live.restructure.query.LiveShowQueryHandler;
import cn.idongjia.live.support.ObjectUtils;
import cn.idongjia.live.v2.pojo.CraftsLive;
import cn.idongjia.live.v2.pojo.CraftsLive4List;
import cn.idongjia.live.v2.pojo.CraftsLiveDetail;
import cn.idongjia.live.v2.pojo.ItemResource;
import cn.idongjia.live.v2.pojo.ItemResourcePackage;
import cn.idongjia.live.v2.pojo.LiveResource;
import cn.idongjia.live.v2.pojo.query.CraftsLivePPUrl;
import cn.idongjia.live.v2.pojo.query.ResourceSearch;
import cn.idongjia.live.v2.service.CraftsLiveServiceV2;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.idongjia.live.support.LiveConst.STATE_LIVE_NOT_BEGIN;
import static cn.idongjia.live.support.LiveConst.STATE_LIVE_OVER;

/**
 * @author zhang created on 2018/1/17 下午1:04
 * @version 1.0
 */
@Service("craftsLiveServiceV2")
public class CraftsLiveServiceV2Impl implements CraftsLiveServiceV2 {

    @Resource
    private LiveShowBO liveShowBO;
    @Resource
    private LiveShowQueryHandler liveShowQueryHandler;

    @Resource
    private LiveResourceQueryHandler liveResourceQueryHandler;

    @Resource
    private LivePullUrlQueryHandler livePullUrlQueryHandler;

    @Resource
    private LiveCraftQueryHandler liveCraftQueryHandler;

    @Override
    public Long craftsLiveAdd(CraftsLive craftsLive) {
        if (Objects.isNull(craftsLive)) {
            throw LiveException.failure("直播参数不能为空");
        }
        if (Utils.isEmpty(craftsLive.getTemplateJsonStr())) {
            throw LiveException.failure("直播图文详情不能为空");
        }
        if (Objects.isNull(craftsLive.getLiveType())) {
            throw LiveException.failure("直播类型不能为空");
        }

        return liveShowBO.createPushPureLive(craftsLive);
    }

    @Override
    public Long craftsLiveUpdate(CraftsLive craftsLive) {
        if (Objects.isNull(craftsLive)) {
            throw LiveException.failure("直播参数不能为空");
        }
        if (Objects.isNull(craftsLive.getLid())) {
            throw LiveException.failure("直播id不能为空");
        }
        if (Utils.isEmpty(craftsLive.getTemplateJsonStr())) {
            throw LiveException.failure("直播图文详情不能为空");
        }
        liveShowBO.updatePushPurelive(craftsLive);
        return craftsLive.getLid();
    }

    @Override
    public Integer craftsLiveResourceManage(Long liveId, List<LiveResource> resources) {
        if (Objects.isNull(liveId)) {
            throw LiveException.failure("直播id不能为空");
        }
        return liveShowBO.craftsLiveResourceManage(liveId, resources);
    }

    @Override
    public Integer craftsLiveResourceReSort(Long liveId, List<LiveResource> resources) {
        ObjectUtils.checkNotNull(liveId, "直播id不能为空");
        return liveShowBO.craftsLiveResourceManage(liveId, resources);
    }

    @Override
    public BaseList<CraftsLive4List> getUnstartList(LiveSearch search) {
        search.setState(STATE_LIVE_NOT_BEGIN);
        ObjectUtils.checkNotNull(search.getUid(), "匠人id不能为null");
        search.setOrderBy("prestarttm");
        search.setOrder("asc ");

        return liveCraftQueryHandler.getCraftsList(search);
    }

    @Override
    public BaseList<CraftsLive4List> getEndList(LiveSearch search) {
        ObjectUtils.checkNotNull(search.getUid(), "匠人id不能null");
        search.setState(STATE_LIVE_OVER);
        search.setOrderBy("endtm");
        search.setOrder("desc");

        return liveCraftQueryHandler.getCraftsList(search);
    }

    @Override
    public CraftsLive getInProgressLiveByUid(Long uid) {
        ObjectUtils.checkNotNull(uid, "匠人id不能为null");
        return liveCraftQueryHandler.getCraftsInProgressLiveByUid(uid);
    }

    @Override
    public CraftsLiveDetail getLiveDetail(Long id, Long uid) {
        ObjectUtils.checkNotNull(id, "直播id不能为null");
        return liveCraftQueryHandler.getCraftsLiveDetail(id, uid);
    }

    @Override
    public List<ItemResource> listItemResource(Long liveId) {
        if (Objects.isNull(liveId)) {
            return new ArrayList<>();
        }
        ObjectUtils.checkArgument(liveId > 0, "直播id不合理(<=0)");
        return liveResourceQueryHandler.listItemResource(liveId);
    }

    @Override
    public void liveAutoOnline(Long liveId, Integer autoOnline) {
        ObjectUtils.checkNotNull(liveId, "直播id不合理");
        liveShowBO.autoOnline(liveId, autoOnline);
    }

    @Override
    public ItemResourcePackage listCraftsItems(ResourceSearch search) {
        ObjectUtils.checkNotNull(search.getCraftsUid(), "匠人id不能为null");
        return liveResourceQueryHandler.listCraftsItems(search);
    }

    @Override
    public List<ItemResource> getSelectedItems(Long liveId) {
        ObjectUtils.checkNotNull(liveId, "直播id不能为null");
        return liveResourceQueryHandler.getSelectedItems(liveId);
    }

    @Override
    public CraftsLive getCraftsLive(Long liveId) {
        ObjectUtils.checkNotNull(liveId, "直播id不合理");
        return liveCraftQueryHandler.getCraftsLive(liveId);
    }

    @Override
    public Map<String, String> getServicePhoneNumber() {
        String phoneNumber = liveShowBO.getServicePhoneNumber();
        return new HashMap<String, String>() {{
            put("phoneNumber", phoneNumber);
        }};
    }

    @Override
    public CraftsLivePPUrl getPushPullUrl(Long liveId) {
        ObjectUtils.checkNotNull(liveId, "直播id不能为null");
        return livePullUrlQueryHandler.getPushPullUrl(liveId);
    }

    @Override
    public CraftsLive getLiveStatus(Long liveId) {
        ObjectUtils.checkNotNull(liveId, "直播id不能为null");
        return liveCraftQueryHandler.getLiveStatus(liveId);
    }
}
