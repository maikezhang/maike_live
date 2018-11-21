package cn.idongjia.live.restructure.query;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.divine.lib.pojo.request.live.LiveQry;
import cn.idongjia.divine.lib.pojo.response.MultiResponse;
import cn.idongjia.divine.lib.pojo.response.live.general.GeneralLiveCO;
import cn.idongjia.live.api.live.pojo.LiveIndexResp;
import cn.idongjia.live.api.live.pojo.LiveIndexSearch;
import cn.idongjia.live.api.live.pojo.LiveResp;
import cn.idongjia.live.db.mybatis.query.DBLiveResourceQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveRoomQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveShowQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveVideoCoverQuery;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.live.LivePullUrl;
import cn.idongjia.live.pojo.live.LiveShow;
import cn.idongjia.live.pojo.live.LiveShow4Article;
import cn.idongjia.live.pojo.live.LiveTypeConfig;
import cn.idongjia.live.query.LiveSearch;
import cn.idongjia.live.query.live.LiveShowSearch;
import cn.idongjia.live.restructure.convert.ConvertorI;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.domain.entity.zoo.LiveZoo;
import cn.idongjia.live.restructure.dto.LiveIndexRespDTO;
import cn.idongjia.live.restructure.dto.LivePullUrlDTO;
import cn.idongjia.live.restructure.dto.LiveResourceCountDTO;
import cn.idongjia.live.restructure.dto.LiveRoomDTO;
import cn.idongjia.live.restructure.dto.LiveShow4ArticleDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.dto.VideoCoverDTO;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.manager.ArticleManager;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.restructure.manager.DivineSearchManager;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.restructure.manager.ZooManager;
import cn.idongjia.live.restructure.pojo.co.live.LiveDetailForApiCO;
import cn.idongjia.live.restructure.repo.LiveShowRepo;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.outcry.pojo.AuctionSession;
import cn.idongjia.outcry.pojo.Session4Live;
import cn.idongjia.search.pojo.query.live.LiveQuery;
import cn.idongjia.user.lib.pojo.vo.CustomerVo;
import cn.idongjia.util.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/6/11.
 */
@Component
public class LiveShowQueryHandler {
    private static final Log logger = LogFactory.getLog(LiveShowQueryHandler.class);

    @Resource
    private LiveShowRepo liveShowRepo;


    @Resource
    private UserManager userManager;


    @Resource
    private ConvertorI<LiveShow4Article, LiveEntity, LiveShow4ArticleDTO> liveShow4ArticleConvertor;


    @Resource
    private SessionQueryHandler sessionQueryHandler;

    @Resource
    private ConfigManager configManager;

    @Resource
    private VideoCoverHandler videoCoverHandler;

    @Resource
    private LiveResourceQueryHandler liveResourceQueryHandler;


    @Resource
    private SearchQueryHandler searchQueryHandler;

    @Resource
    private LiveZooQueryHandler liveZooQueryHandler;


    @Resource
    private LiveRoomQueryHandler liveRoomQueryHandler;


    @Resource
    private LivePullUrlQueryHandler livePullUrlQueryHandler;

    @Resource
    private DivineSearchManager divineSearchManager;

    @Resource
    private ArticleManager articleManager;

    @Resource
    private ZooManager zooManager;



    @Async
    public Future<List<LiveShowDTO>> list(DBLiveShowQuery dbLiveShowQuery) {
        List<LiveShowDTO> liveShowDTOS = liveShowRepo.listLiveShows(dbLiveShowQuery);
        return new AsyncResult(liveShowDTOS);
    }

    @Async
    public Future<Map<Long, LiveShowDTO>> map(DBLiveShowQuery dbLiveShowQuery) {
        List<LiveShowDTO> liveShowDTOS = liveShowRepo.listLiveShows(dbLiveShowQuery);
        Map<Long, LiveShowDTO> liveShowDTOMap = liveShowDTOS.stream().collect(Collectors.toMap(LiveShowDTO::getId, v1
                -> v1, (v1, v2) -> v1));
        return new AsyncResult(liveShowDTOMap);
    }

    public Integer count(DBLiveShowQuery dbLiveShowQuery) {
        return liveShowRepo.count(dbLiveShowQuery);
    }

    public LiveShowDTO getById(Long id) {
        return liveShowRepo.getById(id);
    }

    public LiveShowDTO getByZooId(Long zid) {
        DBLiveShowQuery   dbLiveShowQuery = DBLiveShowQuery.builder().zooId(zid).build();
        List<LiveShowDTO> liveShowDTOS    = liveShowRepo.listLiveShows(dbLiveShowQuery);
        if (Utils.isEmpty(liveShowDTOS)) {
            return null;
        }
        return liveShowDTOS.get(0);
    }

    public LiveShow4Article getForArticle(Long liveShowId) {
        LiveShowDTO         liveShowDTO         = getById(liveShowId);
        User                user                = userManager.getUser(liveShowDTO.getUserId());
        LiveShow4ArticleDTO liveShow4ArticleDTO = new LiveShow4ArticleDTO();
        liveShow4ArticleDTO.setLiveShowDTO(liveShowDTO);
        liveShow4ArticleDTO.setUser(user);
        return liveShow4ArticleConvertor.dataToClient(liveShow4ArticleDTO);
    }

    public List<LiveShowDTO> getByUid(Long uid) {
        LiveShowDTO       liveShowDTO  = liveShowRepo.getLiveShowByUid(uid);
        List<LiveShowDTO> liveShowDTOS = new ArrayList<>();
        liveShowDTOS.add(liveShowDTO);
        return liveShowDTOS;
    }

    public Map<String, Object> getLiveOnlineNumber(Long liveId) {
        return liveShowRepo.getLiveOnlineNum(liveId);
    }

    /**
     * 通过uid获取主播最近直播的直播id
     *
     * @param uid 主播id
     * @return 直播id
     */
    public LiveShowDTO getLiveShowRecentlyEnd(Long uid) {
        return liveShowRepo.getLiveShowRecentlyEnd(uid);
    }

    /**
     * 通过uid获取主播正在直播的直播id
     *
     * @param uid 主播id
     * @return 直播id
     */
    public LiveShowDTO getLiveShowOnByUid(Long uid) {
        return liveShowRepo.getLiveShowOnByUid(uid);
    }


    public LiveShow get(Long liveShowId) {
        LiveShowDTO liveShowDTO = null;
        try {
            liveShowDTO = getById(liveShowId);
        } catch (Exception e) {
            logger.error("查询直播失败{}", e);
            throw LiveException.failure("查询直播失败");
        }
        LiveZoo        liveZoo     = liveZooQueryHandler.get(liveShowDTO.getZooId());
        LivePullUrlDTO pullUrl     = liveRoomQueryHandler.getPullUrl(liveShowDTO.getRoomId());
        LiveRoomDTO    liveRoomDTO = liveRoomQueryHandler.get(liveShowDTO.getRoomId());
        return liveShowDTO.toLiveShow(liveZoo, pullUrl.toDO(), liveRoomDTO.getCloudType());
    }

    public List<LiveShow> listLiveShow(LiveShowSearch liveShowSearch) {
        DBLiveShowQuery dbLiveShowQuery = QueryFactory.getInstance(liveShowSearch);
        try {
            List<LiveShowDTO> liveShowDTOS = list(dbLiveShowQuery).get();

            List<Long>                     roomIds           = liveShowDTOS.stream().map(LiveShowDTO::getRoomId).collect(Collectors.toList());
            DBLiveRoomQuery                dbLiveRoomQuery   = DBLiveRoomQuery.builder().ids(roomIds).build();
            Future<Map<Long, LiveRoomDTO>> liveRoomMapFuture = liveRoomQueryHandler.map(dbLiveRoomQuery);

            List<Long>             zids           = liveShowDTOS.stream().map(LiveShowDTO::getZooId).collect(Collectors.toList());
            Future<List<LiveZoo>>  liveZooFuture  = liveZooQueryHandler.list(zids);
            Map<Long, LiveRoomDTO> liveRoomDTOMap = liveRoomMapFuture.get();
            List<LiveZoo>          liveZoos       = liveZooFuture.get();
            Map<Long, LiveZoo>     liveZooMap     = liveZoos.stream().collect(Collectors.toMap(LiveZoo::getZid, v1 -> v1, (v1, v2) -> v1));
            return liveShowDTOS.stream().map(liveShowDTO -> {
                LivePullUrlDTO pullUrl  = liveRoomQueryHandler.getPullUrl(liveShowDTO.getRoomId());
                LiveShow       liveShow = liveShowDTO.toLiveShow(liveZooMap.get(liveShowDTO.getZooId()), pullUrl.toDO(), liveRoomDTOMap.get(liveShowDTO.getRoomId()).getCloudType());
                if (liveShow.getState().equals(LiveConst.STATE_LIVE_NOT_BEGIN)) {
                    liveShow.setStartTm(liveShow.getPreStartTm());
                }
                return liveShow;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("查询直播异常{}", e);
            throw LiveException.failure("查询失败");
        }
    }

    public LiveShow getLiveShowByZid(Long zid) {
        LiveShowDTO liveShowDTO = null;
        try {
            liveShowDTO = getByZooId(zid);
            if (liveShowDTO == null) {
                return null;
            }
            LiveZoo        liveZoo     = liveZooQueryHandler.get(liveShowDTO.getZooId());
            LivePullUrlDTO pullUrl     = liveRoomQueryHandler.getPullUrl(liveShowDTO.getRoomId());
            LiveRoomDTO    liveRoomDTO = liveRoomQueryHandler.get(liveShowDTO.getRoomId());
            return liveShowDTO.toLiveShow(liveZoo, pullUrl.toDO(), liveRoomDTO.getCloudType());
        } catch (Exception e) {
            logger.error("查询直播失败{}", e);
            throw LiveException.failure("查询直播失败");
        }


    }

    public List<LiveTypeConfig> getLiveTypeConfig() {

       return liveShowRepo.getLiveTypeConfig();
    }


    public List<Map<String, Object>> getLiveMixTabContent() {
        String                    tabContent = configManager.getLiveMixTabContent();
        JsonParser                parser     = new JsonParser();
        JsonArray                 array      = parser.parse(tabContent).getAsJsonArray();
        List<Map<String, Object>> result     = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {

            JsonArray           arr     = array.get(i).getAsJsonArray();
            Integer             type    = arr.get(0).getAsInt();
            String              content = arr.get(1).getAsString();
            Map<String, Object> map1    = new HashMap<>();
            map1.put("type", type);
            map1.put("content", content);
            result.add(map1);
        }

        return result;
    }


    public List<Long> idsByUid(Long uid) {
        DBLiveShowQuery dbLiveShowQuery = DBLiveShowQuery.builder()
                .userIds(Arrays.asList(uid)).online(LiveConst.STATUS_LIVE_ONLINE).status(Arrays.asList(LiveConst.STATUS_LIVE_NORMAL)).build();
        try {
            List<LiveShowDTO> liveShowDTOS = list(dbLiveShowQuery).get();
            return liveShowDTOS.stream().map(LiveShowDTO::getId).collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("查询失败{}", e);
            throw LiveException.failure("查询失败");
        }

    }

    public LiveShow getNextLive(Long uid, Long currentLiveId, Long preStartTimeBegin) {
        if (uid == null || currentLiveId == null) {
            return null;
        }
        DBLiveShowQuery dbLiveShowQuery = DBLiveShowQuery.builder()
                .userIds(Arrays.asList(uid))
                .orderBy("prestarttm asc")
                .minEstimatedStartTime(preStartTimeBegin == null ? null : new Timestamp(preStartTimeBegin))
                .states(Arrays.asList(LiveEnum.LiveState.UNSTART.getCode()))
                .status(Arrays.asList(LiveConst.STATUS_LIVE_NORMAL))
                .build();

        LiveShowDTO nextLiveDTO = liveShowRepo.getNextLive(dbLiveShowQuery, currentLiveId);
        if (nextLiveDTO == null) {
            return null;
        }
        LiveZoo     liveZoo     = liveZooQueryHandler.get(nextLiveDTO.getZooId());
        LivePullUrl livePullUrl = livePullUrlQueryHandler.getLivePullUrl(nextLiveDTO.getId());
        LiveRoomDTO liveRoomDTO = liveRoomQueryHandler.get(nextLiveDTO.getRoomId());
        return nextLiveDTO.toLiveShow(liveZoo, livePullUrl, liveRoomDTO.getCloudType());

    }

    public LiveShowDTO getNextLive(DBLiveShowQuery dbLiveShowQuery, Long currentLiveId) {

        return liveShowRepo.getNextLive(dbLiveShowQuery, currentLiveId);


    }

    public BaseList<cn.idongjia.live.api.live.pojo.LiveIndexResp> listForIndex(LiveIndexSearch liveIndexSearch) {
        List<cn.idongjia.live.api.live.pojo.LiveIndexResp>     liveIndexResps = liveShowRepo.searchLive(liveIndexSearch);
        BaseList<cn.idongjia.live.api.live.pojo.LiveIndexResp> baseList       = new BaseList<>();
        baseList.setItems(liveIndexResps);
        return baseList;
    }

    public List<LiveShow> getLiveShowByUid(Long uid) {
        List<LiveShowDTO> liveShowDTOS;

        try {
            liveShowDTOS = getByUid(uid);

        }catch (Exception e){
            return new ArrayList<>();
        }
        if (Utils.isEmpty(liveShowDTOS)) {
            return new ArrayList<>();
        }

        try {
            List<Long>                     roomIds           = liveShowDTOS.stream().map(LiveShowDTO::getRoomId).collect(Collectors.toList());
            DBLiveRoomQuery                dbLiveRoomQuery   = DBLiveRoomQuery.builder().ids(roomIds).build();
            Future<Map<Long, LiveRoomDTO>> liveRoomMapFuture = liveRoomQueryHandler.map(dbLiveRoomQuery);

            List<Long>             zids           = liveShowDTOS.stream().map(LiveShowDTO::getZooId).collect(Collectors.toList());
            Future<List<LiveZoo>>  liveZooFuture  = liveZooQueryHandler.list(zids);
            Map<Long, LiveRoomDTO> liveRoomDTOMap = liveRoomMapFuture.get();
            List<LiveZoo>          liveZoos       = liveZooFuture.get();
            Map<Long, LiveZoo>     liveZooMap     = liveZoos.stream().collect(Collectors.toMap(LiveZoo::getZid, v1 -> v1, (v1, v2) -> v1));
            return liveShowDTOS.stream().map(liveShowDTO -> {
                LivePullUrlDTO pullUrl = liveRoomQueryHandler.getPullUrl(liveShowDTO.getRoomId());
                return liveShowDTO.toLiveShow(liveZooMap.get(liveShowDTO.getZooId()), pullUrl.toDO(), liveRoomDTOMap.get(liveShowDTO.getRoomId()).getCloudType());
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("获取直播失败{}", e);
            throw LiveException.failure("获取直播失败");
        }
    }


    public BaseList<LiveResp> getGeneralList(LiveSearch liveSearch){
        BaseList<LiveResp> result = new BaseList<>();
        LiveQry qry = QueryFactory.assembleGeneralListQry(liveSearch);
        MultiResponse<GeneralLiveCO> search = divineSearchManager.search(qry);
        int total = search.getTotal();
        if(total>0){
            result.setCount(total);
            List<GeneralLiveCO> generalLiveCOS = (List<GeneralLiveCO>) search.getData();
            List<Long> zids=generalLiveCOS.stream().map(GeneralLiveCO::getZid).collect(Collectors.toList());
            Map<Long, Integer>  mapBatchZooRealCount            = zooManager.mapBatchZooRealCount(zids);

            List<Long> liveIds=generalLiveCOS.stream()
                    .filter(x-> !Objects.equals(LiveEnum.LiveState.FINISHED.getCode(),x.getState().intValue()))
                    .map(GeneralLiveCO::getId)
                    .collect(Collectors.toList());

            Map<Long, Boolean> mapLiveTop= articleManager.getLiveTopList(liveIds);
            List<LiveResp> collect = generalLiveCOS.stream().map(generalLiveCO -> {
                return LiveIndexRespDTO.esDataToLiveResp(generalLiveCO, mapLiveTop.get(generalLiveCO.getId()),mapBatchZooRealCount.get(generalLiveCO.getZid()));

            }).collect(Collectors.toList());
            result.setItems(collect);


        }else{
            result.setCount(0);

        }

        return result;
    }



}
