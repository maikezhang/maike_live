package cn.idongjia.live.restructure.biz;

import cn.idongjia.divine.lib.pojo.Conf;
import cn.idongjia.divine.lib.pojo.request.UserInfo;
import cn.idongjia.divine.lib.pojo.request.live.LiveQry;
import cn.idongjia.divine.lib.pojo.response.auction.AuctionItemCO;
import cn.idongjia.divine.lib.pojo.response.auction.SessionAuctionRel;
import cn.idongjia.divine.lib.pojo.response.live.general.GeneralLiveCO;
import cn.idongjia.gem.lib.pojo.ItemPext;
import cn.idongjia.live.api.live.pojo.LiveItemResp;
import cn.idongjia.live.api.live.pojo.response.MultiResponse;
import cn.idongjia.live.db.mybatis.query.*;
import cn.idongjia.live.restructure.convert.ConvertorI;
import cn.idongjia.live.restructure.convert.LiveCOConvertor;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.domain.entity.tab.CraftmanCategoryV;
import cn.idongjia.live.restructure.domain.entity.tab.PageTabE;
import cn.idongjia.live.restructure.domain.entity.tab.PageTabLiveE;
import cn.idongjia.live.restructure.domain.entity.zoo.LiveZoo;
import cn.idongjia.live.restructure.dto.*;
import cn.idongjia.live.restructure.enums.AnchorBlackWhiteEnum;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.manager.DivineSearchManager;
import cn.idongjia.live.restructure.manager.ESSearchManager;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.restructure.pojo.co.tab.LiveCO;
import cn.idongjia.live.restructure.pojo.query.PageTabLiveApiQry;
import cn.idongjia.live.restructure.query.*;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.outcry.pojo.RelAuction4Live;
import cn.idongjia.outcry.pojo.Session4Live;
import cn.idongjia.user.lib.pojo.vo.CustomerVo;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/7/9.
 */
@Component
public class PageTabBO {


    private static final Log LOGGER    = LogFactory.getLog(PageTabBO.class);
    private static final int PAGE_SIZE = 20;


    @Resource
    private SessionQueryHandler sessionQueryHandler;

    @Resource
    private PageTabQueryHandler pageTabQueryHandler;

    @Resource
    private LiveBookQueryHandler liveBookQueryHandler;

    @Resource
    private LiveRoomQueryHandler liveRoomQueryHandler;

    @Resource
    private PageTabLiveQueryHandler pageTabLiveQueryHandler;

    @Resource
    private DivineSearchManager divineSearchManager;


    public MultiResponse<LiveCO> pageTabLiveApi(PageTabLiveApiQry pageTabLiveApiQry) {

        pageTabLiveApiQry.setLimit(10);
        Long tabId = pageTabLiveApiQry.getTabId();
        Long uid   = pageTabLiveApiQry.getUid();
        if (pageTabLiveApiQry.getLimit() == null || pageTabLiveApiQry.getLimit() == 0) {
            pageTabLiveApiQry.setLimit(PAGE_SIZE);
        }
        MultiResponse<LiveCO> response = null;
        if (tabId == 0) {
            //推荐tab
            LiveQry  qry  = new LiveQry();
            UserInfo info = new UserInfo();
            info.setDeviceId(pageTabLiveApiQry.getDeviceId());
            info.setUserId(uid == 0 ? 0 : uid);
            qry.setUserInfo(info);
            qry.setPage(pageTabLiveApiQry.getPage());
            qry.setLimit(pageTabLiveApiQry.getLimit());
            qry.setOnline(LiveConst.STATUS_LIVE_ONLINE);
            qry.setStates(Arrays.asList(LiveConst.STATE_LIVE_NOT_BEGIN, LiveConst.STATE_LIVE_IN_PROGRESS));
            qry.setStatus(Arrays.asList(LiveConst.STATUS_LIVE_NORMAL));
            qry.setShowLocations(Arrays.asList(AnchorBlackWhiteEnum.AnchorBlackWhiteType.MP_APP_TYPE.getCode()
                    , AnchorBlackWhiteEnum.AnchorBlackWhiteType.ONLY_APP_TYPE.getCode()));
            List<GeneralLiveCO> generalLiveCOS = divineSearchManager.liveRecommend(qry);
            if (!CollectionUtils.isEmpty(generalLiveCOS)) {
                response = assembleLive(generalLiveCOS, uid);
            } else {
                response = MultiResponse.buildSuccess();
            }
        } else if (tabId == 1) {
            //其他tab数据
            response = elseLiveTab(pageTabLiveApiQry);

        } else {
            //获取tab数据
            PageTabE pageTabE = pageTabQueryHandler.get(tabId);
            if (!Objects.isNull(pageTabE)) {
                if (Objects.equals(pageTabE.getType().getCode(), LiveEnum.TabType.NORAMAL.getCode())) {
                    //普通tab
                    List<CraftmanCategoryV> craftmanCategories = pageTabE.getCraftmanCategories();
                    if (Utils.isEmpty(craftmanCategories)) {
                        response = MultiResponse.buildSuccess();
                    } else {
                        response = normalLiveTab(craftmanCategories, null, pageTabLiveApiQry.getPage(), pageTabLiveApiQry.getLimit(), uid);
                    }
                } else {
                    //自定义tab
                    response = selfDefinedLiveTab(pageTabLiveApiQry);
                }


            } else {
                response = MultiResponse.buildSuccess();
            }
        }

        return response;
    }

    public MultiResponse<LiveCO> elseLiveTab(PageTabLiveApiQry pageTabLiveApiQry) {
        //其他tab数据
        try {
            List<PageTabE> pageTabES =
                    pageTabQueryHandler.list(DBPageTabQuery.builder().online(BaseEnum.YesOrNo.YES.getCode()).type(LiveEnum.TabType.NORAMAL.getCode()).status(LiveEnum.TabStatus.NORMAL_STATUS
                            .getCode()).build());
            List<CraftmanCategoryV> craftmanCategoryVS = pageTabES.stream().map(PageTabE::getCraftmanCategories).flatMap(List::stream).distinct().collect(Collectors.toList());
            if (Utils.isEmpty(craftmanCategoryVS)) {
                craftmanCategoryVS = null;
            }
            return normalLiveTab(null, craftmanCategoryVS, pageTabLiveApiQry.getPage(), pageTabLiveApiQry.getLimit(), pageTabLiveApiQry.getUid());


        } catch (Exception e) {
            LOGGER.error("查询直播失败{}", e);
            return MultiResponse.buildSuccess();
        }
    }

    public MultiResponse<LiveCO> normalLiveTab(List<CraftmanCategoryV> inCategories, List<CraftmanCategoryV> notInCategories, int page, int limit, Long uid) {
        LiveQry qry = new LiveQry();

        if (!Utils.isEmpty(inCategories)) {
            List<Long> categoryIds = inCategories.stream().map(craftmanCategoryV -> craftmanCategoryV.getId().longValue()).collect(Collectors.toList());
            qry.setCategoryIds(categoryIds);
        }
        if (!Utils.isEmpty(notInCategories)) {
            List<Long> categoryIds = notInCategories.stream().map(craftmanCategoryV -> craftmanCategoryV.getId().longValue()).collect(Collectors.toList());
            qry.setExcludeCategoryIds(categoryIds);
        }
        qry.setPage(page);
        qry.setLimit(limit);
        qry.setOnline(LiveConst.STATUS_LIVE_ONLINE);
        qry.setStates(Arrays.asList(LiveConst.STATE_LIVE_NOT_BEGIN, LiveConst.STATE_LIVE_IN_PROGRESS));
        qry.setStatus(Arrays.asList(LiveConst.STATUS_LIVE_NORMAL));
        qry.setShowLocations(Arrays.asList(AnchorBlackWhiteEnum.AnchorBlackWhiteType.MP_APP_TYPE.getCode()
                , AnchorBlackWhiteEnum.AnchorBlackWhiteType.ONLY_APP_TYPE.getCode()));
        List<GeneralLiveCO> generalLiveCOS = divineSearchManager.liveTab(qry);
        if (CollectionUtils.isEmpty(generalLiveCOS)) {
            return MultiResponse.buildSuccess();
        }
        return assembleLive(generalLiveCOS, uid);
    }


    public MultiResponse<LiveCO> selfDefinedLiveTab(PageTabLiveApiQry qry) {
        DBPageTabLiveQuery query = DBPageTabLiveQuery.builder()
                .showStatus(BaseEnum.YesOrNo.YES.getCode())
                .tabId(qry.getTabId())
                .page(qry.getPage())
                .limit(qry.getLimit())
                .orderBy("weight desc")
                .build();
        List<PageTabLiveE> pageTabLiveVS = pageTabLiveQueryHandler.list(query);
        if (Utils.isEmpty(pageTabLiveVS)) {
            return MultiResponse.buildSuccess();
        } else {
            List<Long> liveIds = pageTabLiveVS.stream().map(PageTabLiveE::getLiveId).collect(Collectors.toList());

            Map<Long, Integer> orderMap = new HashMap<>();
            for (int i = 0; i < liveIds.size(); i++) {
                orderMap.put(liveIds.get(i), i);
            }

            List<GeneralLiveCO> generalLiveCOS = divineSearchManager.liveList(liveIds);
            if (CollectionUtils.isEmpty(generalLiveCOS)) {
                return MultiResponse.buildSuccess();
            }

            //solr不保证顺序，重排
            Collections.sort(generalLiveCOS, new Comparator<GeneralLiveCO>() {
                @Override
                public int compare(GeneralLiveCO o1, GeneralLiveCO o2) {
                    return orderMap.get(o1.getId()) - orderMap.get(o2.getId());
                }
            });

            return assembleLive(generalLiveCOS, qry.getUid());
        }
    }

    public MultiResponse<LiveCO> assembleLive(List<GeneralLiveCO> generalLiveCOS, Long uid) {

        List<Long> bookLiveIds = new ArrayList<>();
        //获取专场id
        List<Long> sessionIds = generalLiveCOS.stream()
                .filter(generalLiveCO -> Objects.nonNull(generalLiveCO.getSessionId()) && !Objects.equals(generalLiveCO.getSessionId(), Conf.defaultDate))
                .map(GeneralLiveCO::getSessionId).collect(Collectors.toList());
        List<Long> liveIds = generalLiveCOS.stream().map(GeneralLiveCO::getId).collect(Collectors.toList());
        List<Long> roomIds = generalLiveCOS.stream().map(GeneralLiveCO::getRoomId).collect(Collectors.toList());

        Map<Long, SessionAuctionRel> sessionAuctionRelMap = new HashMap<>();
        Map<Long, LivePullUrlDTO>    livePullUrlDTOMap    = null;
        List<Long>                   bookedSessionIds     = null;
        if (!CollectionUtils.isEmpty(sessionIds)) {
            //获取专场对应的拍品数据
            sessionAuctionRelMap = divineSearchManager.mapSessionAuction(sessionIds);
            //获取专场的订阅关系
            if (Objects.nonNull(uid)) {
                bookedSessionIds = sessionQueryHandler.listBookedSessionIds(sessionIds, uid);
            }
        }
        try {
            List<LiveBookDTO> liveBookDTOS = liveBookQueryHandler.list(DBLiveBookQuery.builder().status(LiveConst.STATUS_BANNER_NORMAL).userId(uid).liveIds(liveIds).build()).get();
            bookLiveIds = liveBookDTOS.stream().map(LiveBookDTO::getLiveId).collect(Collectors.toList());
            List<LiveRoomDTO> liveRoomDTOS = liveRoomQueryHandler.list(DBLiveRoomQuery.builder().ids(roomIds).build()).get();
            livePullUrlDTOMap = liveRoomQueryHandler.pullUrlMap(liveRoomDTOS).get();
        } catch (Exception e) {
            LOGGER.warn("查询失败");
        }

        List<Long>                   finalBookLiveIds          = bookLiveIds;
        List<Long>                   finalBookedSessionIds     = bookedSessionIds;
        Map<Long, SessionAuctionRel> finalSessionAuctionRelMap = sessionAuctionRelMap;
        Map<Long, LivePullUrlDTO>    finalLivePullUrlDTOMap    = livePullUrlDTOMap;
        List<LiveCO> collect = generalLiveCOS.stream().map(generalLiveCO -> {
            LiveForTabDTO       liveForTabDTO  = new LiveForTabDTO();
            List<AuctionItemCO> auctionItemCOS = new ArrayList<>();
            boolean             bookStatus     = false;
            switch (generalLiveCO.getType().intValue()) {
                case 1:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    if (!CollectionUtils.isEmpty(finalBookLiveIds) && Objects.nonNull(uid)) {
                        bookStatus = finalBookLiveIds.contains(generalLiveCO.getId());
                    }
                    break;
                case 2:
                    if (!CollectionUtils.isEmpty(finalBookLiveIds) && Objects.nonNull(uid)) {
                        bookStatus = finalBookLiveIds.contains(generalLiveCO.getId());
                    }
                    if (Objects.nonNull(finalSessionAuctionRelMap.get(generalLiveCO.getSessionId()))) {
                        auctionItemCOS = finalSessionAuctionRelMap.get(generalLiveCO.getSessionId()).getAuctionItemCOS();
                    }
                    break;
                default:
                    break;
            }
            liveForTabDTO.setBookState(bookStatus ? 1 : 0);

            liveForTabDTO.setLivePullUrlDTO(finalLivePullUrlDTOMap.get(generalLiveCO.getRoomId()));

            liveForTabDTO.setLiveAuctionItems(auctionItemCOS);

            liveForTabDTO.setGeneralLiveCO(generalLiveCO);
            return LiveCOConvertor.searchDataToClient(liveForTabDTO);

        }).collect(Collectors.toList());
        return MultiResponse.of(collect, 0);
    }


}
