package cn.idongjia.live.restructure.query;

import cn.idongjia.gem.lib.pojo.ItemPext;
import cn.idongjia.live.api.live.pojo.LiveApiResp;
import cn.idongjia.live.api.live.pojo.LiveItemResp;
import cn.idongjia.live.db.mybatis.query.DBLiveResourceQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveVideoCoverQuery;
import cn.idongjia.live.db.mybatis.query.DBPlayBackQuery;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.restructure.dto.AuctionSessionRelDTO;
import cn.idongjia.live.restructure.dto.LiveItemDTO;
import cn.idongjia.live.restructure.dto.LiveResourceDTO;
import cn.idongjia.live.restructure.dto.PlayBackDTO;
import cn.idongjia.live.restructure.dto.SearchIndexRespDTO;
import cn.idongjia.live.restructure.dto.VideoCoverDTO;
import cn.idongjia.live.restructure.manager.SearchManager;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.outcry.pojo.RelAuction4Live;
import cn.idongjia.outcry.pojo.Session4Live;
import cn.idongjia.search.pojo.query.live.LiveQuery;
import cn.idongjia.user.lib.pojo.vo.CustomerVo;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/7/19.
 */
@Component
public class LiveApiQueryHandler {

    private static final Log logger = LogFactory.getLog(LiveShowQueryHandler.class);


    @Resource
    private SearchManager searchManager;

    @Resource
    private LiveResourceQueryHandler liveResourceQueryHandler;

    @Resource
    private SessionQueryHandler sessionQueryHandler;

    @Resource
    private UserManager userManager;

    @Resource
    private VideoCoverHandler videoCoverHandler;

    @Resource
    private ItemQueryHandler itemQueryHandler;

    @Resource
    private LiveIndexQueryHandler liveIndexQueryHandler;

    @Resource
    private PlayBackQueryHandler playBackQueryHandler;
//    public List<LiveApiResp> getBatchLive(List<Long> lids) {
//        LiveQuery liveQuery = new LiveQuery();
//        liveQuery.setLiveIds(lids);
//        BaseList<SearchIndexRespDTO> searchIndexRespDTOBaseList = searchManager.takeLiveFromIndex(liveQuery);
//        List<SearchIndexRespDTO>     items                      = searchIndexRespDTOBaseList.getItems();
//        return assembleLiveApiResp(items);
//    }

    public List<LiveApiResp> assembleLiveApiResp(List<SearchIndexRespDTO> searchIndexRespDTOS) {
        if (Utils.isEmpty(searchIndexRespDTOS)) {
            return new ArrayList<>();
        }
        List<LiveApiResp> liveApiResps = new ArrayList<>();
        List<Long>        asids        = new ArrayList<>();
        List<Long>        liveIds      = new ArrayList<>();
        List<Long>        uids         = new ArrayList<>();
        List<Long>        playbackLiveIds=new ArrayList<>();
        searchIndexRespDTOS.stream().forEach(searchIndexRespDTO -> {
            Integer liveType = searchIndexRespDTO.getLiveType();
            switch (liveType) {
                //纯直播
                case 1:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    liveIds.add(searchIndexRespDTO.getId());
                    uids.add(searchIndexRespDTO.getUserId());
                    playbackLiveIds.add(searchIndexRespDTO.getId());
                    break;
                //直播拍
                case 2:
                    liveIds.add(searchIndexRespDTO.getId());
                    asids.add(searchIndexRespDTO.getAsid());
                    uids.add(searchIndexRespDTO.getUserId());
                    break;
                default:
                    break;
            }
        });

        try {
            Map<Long, List<LiveResourceDTO>> liveResourceMap = null;
            if (!Utils.isEmpty(liveIds)) {
                Future<Map<Long, List<LiveResourceDTO>>> liveResourceMapFuture = liveResourceQueryHandler.map(DBLiveResourceQuery.builder().status(0).liveIds(liveIds).build());
                liveResourceMap = liveResourceMapFuture.get();
            }
            if (liveResourceMap == null) {
                liveResourceMap = new HashMap<>();
            }
            Map<Long, Session4Live> auctionSessionMap = null;
            if (!Utils.isEmpty(asids)) {
                auctionSessionMap = sessionQueryHandler.listSessionSimpleData(asids);
            }
            if (auctionSessionMap == null) {
                auctionSessionMap = new HashMap<>();
            }
            Map<Long, CustomerVo>        customerVoMap        = userManager.takeBatchCustomer(uids);
            Map<Long, VideoCoverDTO>     longVideoCoverDTOMap = null;
            Map<Long, List<PlayBackDTO>> playBackMap          = null;
            if (!Utils.isEmpty(liveIds)) {
                longVideoCoverDTOMap = videoCoverHandler.map(DBLiveVideoCoverQuery.builder().liveIds(liveIds).build());
                Future<Map<Long, List<PlayBackDTO>>> playBackMapFuture = playBackQueryHandler.map(DBPlayBackQuery.builder().liveIds(playbackLiveIds).status(0).build());
                playBackMap = playBackMapFuture.get();
            } else {
                longVideoCoverDTOMap = new HashMap<>();
                playBackMap = new HashMap<>();
            }

            Map<Long, List<LiveResourceDTO>> finalLiveResourceMap   = liveResourceMap;
            Map<Long, Session4Live>        finalAuctionSessionMap = auctionSessionMap;
//            Map<Long, VideoCoverDTO>         sessionVideoMap        = new HashMap<>();
//            for (Map.Entry<Long, AuctionSession> auctionSession : auctionSessionMap.entrySet()) {
//                VideoCoverDTO  videoCoverDTO = new VideoCoverDTO(new VideoCoverPO());
//                AuctionSession value         = auctionSession.getValue();
//                videoCoverDTO.setDuration(value.getVideoCoverDuration());
//                videoCoverDTO.setId(value.getVideoCoverId());
//                videoCoverDTO.setPic(value.getVideoCoverPic());
//                videoCoverDTO.setUrl(value.getVideoCoverUrl());
//                sessionVideoMap.put(value.getAsid(), videoCoverDTO);
//            }
            Map<Long, List<LiveItemResp>> itemMap = searchIndexRespDTOS.stream().collect(Collectors.toMap(SearchIndexRespDTO::getId,
                    searchIndexRespDTO -> {
                        int liveType = searchIndexRespDTO.getLiveType();
                        // 1：纯直播未开始2：纯直播已开始3：纯直播已结束 4直播拍未开始5直播拍拍卖中6直播拍已结束

                        List<LiveItemResp> liveItemResps = new ArrayList<>();
                        switch (liveType) {
                            //纯直播
                            case 1:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                                List<LiveResourceDTO> liveResourceDTOS = finalLiveResourceMap.get(searchIndexRespDTO.getId());
                                if (!Utils.isEmpty(liveResourceDTOS)) {
                                    List<Long> itemIds = liveResourceDTOS.stream().filter(liveResourceDTO -> liveResourceDTO.getResourceType().equals(cn.idongjia.live.support.enumeration
                                            .LiveResourceType.ITEM.getCode())).map(LiveResourceDTO::getResourceId).collect(Collectors.toList());

                                    Map<Long, ItemPext> itemPextMap = null;
                                    try {
                                        itemPextMap = itemQueryHandler.assembleMap(itemIds);
                                    } catch (Exception e) {
                                        logger.error("查询拍品失败{}", e);
                                    }
                                    Map<Long, ItemPext> finalItemPextMap = itemPextMap;
                                    liveItemResps = liveResourceDTOS.stream().map(liveResourceDTO -> {
                                        ItemPext itemPext = finalItemPextMap.get(liveResourceDTO.getResourceId());
                                        if (itemPext != null) {
                                            LiveItemDTO liveItemDTO = new LiveItemDTO(itemPext);
                                            return liveItemDTO.toLiveItemResp(liveResourceDTO, null);
                                        }
                                        return null;

                                    }).collect(Collectors.toList());
                                }
                                break;
                            //直播拍
                            case 2:
                                Session4Live auctionSession = finalAuctionSessionMap.get(searchIndexRespDTO.getAsid());
                                if (auctionSession == null) {
                                    liveItemResps = new ArrayList<>();

                                } else {
                                    List<RelAuction4Live> auctionSessionRels = auctionSession.getRelAuctions();
                                    if (!Utils.isEmpty(auctionSessionRels)) {
                                        liveItemResps = auctionSessionRels.stream().map(auctionSessionRel -> {
                                            AuctionSessionRelDTO auctionSessionRelDTO = new AuctionSessionRelDTO(auctionSessionRel);
                                            return auctionSessionRelDTO.assembleLiveItemResp();
                                        }).collect(Collectors.toList());
                                    } else {
                                        liveItemResps = new ArrayList<>();
                                    }
                                }

                                break;
                            default:
                                break;
                        }
                        return liveItemResps.stream().filter(liveItemResp -> liveItemResp != null).collect(Collectors.toList());
                    }, (v1, v2) -> v1));


            Map<Long, VideoCoverDTO>     finalVideoCoverDTOMap = longVideoCoverDTOMap;
            Map<Long, List<PlayBackDTO>> finalPlayBackMap      = playBackMap;
            liveApiResps = searchIndexRespDTOS.stream().map(searchIndexRespDTO -> {
                Long          id            = searchIndexRespDTO.getId();
                int           liveType      = searchIndexRespDTO.getLiveType();
                VideoCoverDTO videoCoverDTO = finalVideoCoverDTOMap.get(id);
//                switch (liveType) {
//                    case 1:
//                    case 3:
//                    case 4:
//                    case 5:
//                    case 6:
//                        videoCoverDTO = finalVideoCoverDTOMap.get(id);
//                        break;
//                    case 2:
//                        videoCoverDTO = sessionVideoMap.get(searchIndexRespDTO.getAsid());
//                        break;
//                }
                return searchIndexRespDTO.assembleLiveApiResp(customerVoMap.get(searchIndexRespDTO.getUserId()), videoCoverDTO, itemMap.get(id), finalPlayBackMap.get(id),liveType);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("查询拍品失败{}", e);
        }

        return liveApiResps.stream().filter(liveApiResp -> liveApiResp != null).collect(Collectors.toList());
    }

    public List<LiveApiResp> listForApi(Integer page, Integer limit, Integer type) {
        List<SearchIndexRespDTO> searchIndexRespDTOS = liveIndexQueryHandler.listFromIndex(page, limit, type);
        return assembleLiveApiResp(searchIndexRespDTOS);
    }
}
