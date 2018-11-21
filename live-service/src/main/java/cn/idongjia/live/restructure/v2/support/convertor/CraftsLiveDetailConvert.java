package cn.idongjia.live.restructure.v2.support.convertor;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.divine.lib.pojo.response.live.general.GeneralLiveCO;
import cn.idongjia.divine.lib.pojo.response.live.general.ItemCO;
import cn.idongjia.live.api.purelive.ResourceService;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.purelive.PureLiveDetailDO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.manager.GemManager;
import cn.idongjia.live.restructure.manager.OutcryManager;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.restructure.v2.support.enumeration.LiveResourceType;
import cn.idongjia.live.support.spring.SpringUtils;
import cn.idongjia.live.v2.pojo.CraftsLiveDetail;
import cn.idongjia.live.v2.pojo.LiveResource;
import cn.idongjia.live.v2.pojo.LiveResourceDetail;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.outcry.pojo.AuctionSessionRel;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.idongjia.live.support.GsonUtil.getFirstStringInJsonArray;


/**
 * Created by YueXiaodong on 2018/01/22.
 */
public class CraftsLiveDetailConvert {
    private static final OutcryManager   outcryManager   = SpringUtils.getBean("outcryManager", OutcryManager.class).
            orElseThrow(() -> LiveException.failure("获取outcryManager失败"));
    private static final UserManager     userManager     = SpringUtils.getBean("userManager", UserManager.class).orElseThrow(() -> LiveException.failure("获取userManager失败"));
    private static final GemManager      gemManager      = SpringUtils.getBean("gemManager", GemManager.class).orElseThrow(() -> LiveException.failure("获取gemManager失败"));
    private static final ResourceService resourceService = SpringUtils.getBean("resourceServiceImpl", ResourceService.class).orElseThrow(() -> LiveException.failure("获取resourceService失败"));
    private static final Log LOGGER = LogFactory.getLog(CraftsLiveDetailConvert.class);


    private CraftsLiveDetailConvert() {
    }

    public static CraftsLiveDetail toCraftsLiveDetail(GeneralLiveCO generalLiveCO, LiveShowDTO nextLiveDTO) {
        CraftsLiveDetail detail = CraftsLiveDetailConvert.toCraftsLiveDetail(generalLiveCO);
        if (nextLiveDTO == null) {
            return detail;
        }
        detail.setNextLiveId(nextLiveDTO.getId());
        detail.setNextPreStartTime(nextLiveDTO.getEstimatedStartTime());
        return detail;
    }

    public static CraftsLiveDetail toCraftsLiveDetail(GeneralLiveCO generalLiveCO) {
        CraftsLiveDetail detail = new CraftsLiveDetail();
        detail.setLid(generalLiveCO.getId());
        detail.setHostAvatar(generalLiveCO.getAvatar());
        detail.setHostName(generalLiveCO.getCraftsmanName());
        detail.setTitle(generalLiveCO.getCraftsmanTitle());
        detail.setUserCount(generalLiveCO.getUv());
        detail.setStartTime(generalLiveCO.getStartTime());
        detail.setZid(generalLiveCO.getZid());
        if (LiveEnum.LiveType.LIVE_AUCTION.getCode() != generalLiveCO.getType()) {
            List<ItemCO> items = generalLiveCO.getItems();
            if (CollectionUtils.isEmpty(items)) {
                items = new ArrayList<>();
            }
            List<PureLiveDetailDO> detailDOs = items.stream().map(itemCO -> {
                PureLiveDetailDO detailDO = new PureLiveDetailDO();
                detailDO.setResourceId(itemCO.getItemId());
                return detailDO;
            }).collect(Collectors.toList());
            CraftsLiveDetailConvert.setLiveResources(detail, detailDOs);
        } else if (LiveEnum.LiveType.LIVE_AUCTION.getCode() == generalLiveCO.getType()) {
            CraftsLiveDetailConvert.setAuctionResources(detail, generalLiveCO.getSessionId());
        }
        return detail;
    }

    //添加拍卖资源
    private static void setAuctionResources(CraftsLiveDetail detail, Long asid) {
        CraftsLiveDetailConvert.outcryManager.takeAuctionSession(asid).ifPresent(session -> {
            Map<Long, AuctionSessionRel> relMap = session.getRelates().stream().collect(Collectors.toMap(rel -> rel.getIid(), rel -> rel, (rel1, rel2) -> rel1));
            detail.setResources(CraftsLiveDetailConvert.auction2Resources(session.getRelates()));
            //主推
            if (session.getCurrentItemId() == null) { //没有主推
                return;
            }
            LiveResourceDetail mainResource = new LiveResourceDetail();
            mainResource.setResourceId(session.getCurrentItemId());
            mainResource.setResourceType(LiveResourceType.AUCTION.getCode());
            mainResource.setId(session.getCurrentItemId());
            AuctionSessionRel rel = relMap.getOrDefault(session.getCurrentItemId(), null);
            if (rel != null) {
                if (rel.getCurrentOfferUid() != null) {
                    User user = CraftsLiveDetailConvert.userManager.getUser(rel.getCurrentOfferUid());
                    mainResource.setUsername(user == null ? null : user.getUsername());
                }
                mainResource.setPic(rel.getPic());
                mainResource.setPrice(rel.getCurrentprice().toString());
                mainResource.setTitle(rel.getTitle());
            }
            detail.setMain(mainResource);
        });
    }

    //拍卖转资源
    private static List<LiveResource> auction2Resources(List<AuctionSessionRel> rels) {
        if (rels == null || rels.size() == 0) {
            return null;
        }
        List<LiveResource> resources = new ArrayList<>();
        for (AuctionSessionRel rel : rels) {
            LiveResource resource = new LiveResource();
            resource.setResourceType(LiveResourceType.AUCTION.getCode());
            resource.setResourceId(rel.getIid());
            resources.add(resource);
        }
        return resources;
    }

    /**
     * 关联的商品资源列表
     */
    private static void setLiveResources(CraftsLiveDetail detail, List<PureLiveDetailDO> detailDOs) {
        Long itemId = CraftsLiveDetailConvert.resourceService.mainResource(detail.getLid(), LiveResourceType.ITEM.getCode());
        CraftsLiveDetailConvert.gemManager.takeItem(itemId).ifPresent(x -> {
            LiveResourceDetail mainResource = new LiveResourceDetail();
            mainResource.setPic(getFirstStringInJsonArray(x.getPictures()));
            mainResource.setResourceId(x.getIid());
            mainResource.setResourceType(LiveResourceType.ITEM.getCode());
            mainResource.setPrice(x.getPrice());
            mainResource.setTitle(x.getTitle());
            detail.setMain(mainResource);
        });
        List<LiveResource> resources = new ArrayList<>();
        for (PureLiveDetailDO detailDO : detailDOs) {
            if (Objects.nonNull(detailDO.getResourceId())) {
                LiveResource resource = new LiveResource();
                resource.setResourceType(LiveResourceType.ITEM.getCode());
                resource.setResourceId(detailDO.getResourceId());
                resources.add(resource);
            }
        }
        detail.setResources(resources);
    }
}
