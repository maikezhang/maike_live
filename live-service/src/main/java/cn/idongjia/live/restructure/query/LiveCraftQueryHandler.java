package cn.idongjia.live.restructure.query;

import cn.idongjia.divine.lib.pojo.response.live.general.GeneralLiveCO;
import cn.idongjia.live.api.live.pojo.LiveIndexResp;
import cn.idongjia.live.db.mybatis.query.DBLivePureQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveResourceQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveShowQuery;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.live.LiveTypeConfig;
import cn.idongjia.live.query.LiveSearch;
import cn.idongjia.live.restructure.convert.ConvertorI;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.dto.CraftsLive4ListDTO;
import cn.idongjia.live.restructure.dto.CraftsLiveDTO;
import cn.idongjia.live.restructure.dto.LivePureDTO;
import cn.idongjia.live.restructure.dto.LiveResourceDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.dto.VideoCoverDTO;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.restructure.manager.DivineSearchManager;
import cn.idongjia.live.restructure.manager.TemplateManager;
import cn.idongjia.live.restructure.manager.ZooManager;
import cn.idongjia.live.restructure.v2.support.convertor.CraftsLiveDetailConvert;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.live.v2.pojo.CraftsLive;
import cn.idongjia.live.v2.pojo.CraftsLive4List;
import cn.idongjia.live.v2.pojo.CraftsLiveDetail;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.outcry.pojo.AuctionSession;
import cn.idongjia.outcry.pojo.Session4Live;
import cn.idongjia.search.pojo.query.live.LiveQuery;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/7/19.
 */
@Component
public class LiveCraftQueryHandler {
    private static final Log logger = LogFactory.getLog(LiveCraftQueryHandler.class);

    @Resource
    private LiveShowQueryHandler liveShowQueryHandler;

    @Resource
    private ConvertorI<CraftsLive4List, LiveEntity, CraftsLive4ListDTO> craftsLive4ListConvertor;


    @Resource
    private ConvertorI<CraftsLive, LiveEntity, CraftsLiveDTO> craftsLiveConvertor;

    @Resource
    private SessionQueryHandler sessionQueryHandler;

    @Resource
    private LivePureQueryHandler livePureQueryHandler;

    @Resource
    private ConfigManager configManager;

    @Resource
    private VideoCoverHandler videoCoverHandler;

    @Resource
    private LiveResourceQueryHandler liveResourceQueryHandler;

    @Resource
    private TemplateManager templateManager;

    @Resource
    private DivineSearchManager divineSearchManager;

    public BaseList<CraftsLive4List> getCraftsList(LiveSearch liveSearch) {
        BaseList<CraftsLive4List> craftsLive4ListBaseList = new BaseList<>();
        DBLiveShowQuery           dbLiveShowQuery         = QueryFactory.getInstance(liveSearch);
        try {
            dbLiveShowQuery.setStatus(Arrays.asList(LiveConst.STATUS_LIVE_NORMAL));
            Integer count = liveShowQueryHandler.count(dbLiveShowQuery);
            craftsLive4ListBaseList.setCount(count);
            if (count > 0) {
                List<LiveShowDTO> liveShowDTOS = liveShowQueryHandler.list(dbLiveShowQuery).get();
                //获取专场封面图
                List<Long> auctionLiveIds = liveShowDTOS.stream().filter(liveShowDTO -> liveShowDTO.getType().intValue() == LiveConst.TYPE_LIVE_AUCTION)
                        .map(LiveShowDTO::getId).collect(Collectors.toList());
                Map<Long, Session4Live> sessionMap = sessionQueryHandler.mapBySessionId(auctionLiveIds);

                //纯直播查询
                List<Long>             liveShowIds     = liveShowDTOS.stream().map(LiveShowDTO::getId).collect(Collectors.toList());
                DBLivePureQuery        dbLivePureQuery = DBLivePureQuery.builder().liveIds(liveShowIds).build();
                Map<Long, LivePureDTO> livePureDTOMap  = livePureQueryHandler.map(dbLivePureQuery).get();
                Integer                isDebugMinutes  = configManager.getIsDebugMinutes();

                craftsLive4ListBaseList.setItems(liveShowDTOS.stream().map(liveShowDTO -> {
                    CraftsLive4ListDTO craftsLive4ListDTO = new CraftsLive4ListDTO();
                    Long               liveShowDTOId      = liveShowDTO.getId();
                    Session4Live       auctionSession     = sessionMap.get(liveShowDTOId);
                    craftsLive4ListDTO.setAuctionSession(auctionSession);
                    craftsLive4ListDTO.setDebugMinutes(isDebugMinutes);
                    craftsLive4ListDTO.setLiveShowDTO(liveShowDTO);
                    LivePureDTO livePureDTO = livePureDTOMap.get(liveShowDTOId);
                    craftsLive4ListDTO.setPureLiveDTO(livePureDTO);
                    return craftsLive4ListConvertor.dataToClient(craftsLive4ListDTO);

                }).collect(Collectors.toList()));
            }
        } catch (Exception e) {
            logger.error("查询失败{}", e);
        }
        return craftsLive4ListBaseList;
    }

    public CraftsLive getCraftsInProgressLiveByUid(Long uid) {
        try {
            DBLiveShowQuery dbLiveShowQuery = DBLiveShowQuery.builder().page(1).status(Arrays.asList(LiveConst.STATUS_LIVE_NORMAL)).limit(1).states(Collections.singletonList(LiveEnum.LiveState
                    .PLAYING.getCode())).userIds(Arrays.asList(uid)).build();
            List<LiveShowDTO> liveShowDTOS = liveShowQueryHandler.list(dbLiveShowQuery).get();
            if (Utils.isEmpty(liveShowDTOS)) {
                return null;
            }
            LiveShowDTO                   liveShowDTO           = liveShowDTOS.get(0);
            Long                          liveId                = liveShowDTO.getId();
            VideoCoverDTO                 videoCoverDTO         = videoCoverHandler.getById(liveId);
            LivePureDTO                   livePureDTO           = livePureQueryHandler.getById(liveId);
            Map<Long, Session4Live>       longAuctionSessionMap = sessionQueryHandler.mapBySessionId(Arrays.asList(liveId));
            DBLiveResourceQuery           dbLiveResourceQuery   = DBLiveResourceQuery.builder().liveIds(Arrays.asList(liveId)).build();
            Future<List<LiveResourceDTO>> liveResourceFuture    = liveResourceQueryHandler.list(dbLiveResourceQuery);
            List<LiveResourceDTO>         liveResourceDTOS      = liveResourceFuture.get();
            String                        templateJson          = getAppDetails(liveResourceDTOS);
            CraftsLiveDTO                 craftsLiveDTO         = new CraftsLiveDTO();
            Session4Live                  auctionSession        = longAuctionSessionMap.get(liveId);
            craftsLiveDTO.setAuctionSession(auctionSession);
            craftsLiveDTO.setLiveShowDTO(liveShowDTO);
            craftsLiveDTO.setPureLiveDTO(livePureDTO);
            craftsLiveDTO.setResourceDTOS(liveResourceDTOS);
            craftsLiveDTO.setTemplateJson(templateJson);
            craftsLiveDTO.setVideoCoverDTO(videoCoverDTO);
            return craftsLiveConvertor.dataToClient(craftsLiveDTO);
            //屏幕方向
        } catch (Exception e) {
            logger.error("查询失败{}", e);
            return null;
        }
    }

    private String getAppDetails(List<LiveResourceDTO> liveResourceDTOS) {
        if (Utils.isEmpty(liveResourceDTOS)) {
            return null;
        }
        String templateJsonStr = null;
        //用于调用前端接口
        Long templateId = null;
        for (LiveResourceDTO liveResourceDTO : liveResourceDTOS) {
            //判断直播资源是否为超级模板
            if (liveResourceDTO.getResourceType().equals(LiveConst.TYPE_DETAIL_TEMPLATE)) {
                templateId = liveResourceDTO.getResourceId();
                break;
            }
        }
        if (templateId != null) {
            try {
                templateJsonStr = templateManager.appQueryTemplate(templateId);
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }
        return templateJsonStr;
    }

    public CraftsLive getCraftsLive(Long liveId) {
        try {
            LiveShowDTO                   liveShowDTO           = liveShowQueryHandler.getById(liveId);
            VideoCoverDTO                 videoCoverDTO         = videoCoverHandler.getById(liveId);
            LivePureDTO                   livePureDTO           = livePureQueryHandler.getById(liveShowDTO.getId());
            DBLiveResourceQuery           dbLiveResourceQuery   = DBLiveResourceQuery.builder().liveIds(Arrays.asList(liveId)).status(LiveConst.STATUS_DETAIL_NORMAL).build();
            Future<List<LiveResourceDTO>> liveResourceFuture    = liveResourceQueryHandler.list(dbLiveResourceQuery);
            List<LiveResourceDTO>         liveResourceDTOS      = liveResourceFuture.get();
            Map<Long, Session4Live>       longAuctionSessionMap = sessionQueryHandler.mapBySessionId(Arrays.asList(liveId));
            String                        templateJson          = getAppDetails(liveResourceDTOS);
            CraftsLiveDTO                 craftsLiveDTO         = new CraftsLiveDTO();
            craftsLiveDTO.setVideoCoverDTO(videoCoverDTO);
            craftsLiveDTO.setTemplateJson(templateJson);
            craftsLiveDTO.setResourceDTOS(liveResourceDTOS);
            craftsLiveDTO.setPureLiveDTO(livePureDTO);
            craftsLiveDTO.setLiveShowDTO(liveShowDTO);
            Session4Live auctionSession = longAuctionSessionMap.get(liveId);
            craftsLiveDTO.setAuctionSession(auctionSession);
            List<LiveTypeConfig> liveTypeConfig = liveShowQueryHandler.getLiveTypeConfig();
            for (LiveTypeConfig config : liveTypeConfig) {
                if (config.getId().equals(liveShowDTO.getType())) {
                    craftsLiveDTO.setLiveTypeName(config.getName());
                }
            }
            return craftsLiveConvertor.dataToClient(craftsLiveDTO);
        } catch (Exception e) {
            logger.error("查询直播异常{}", e);
            throw LiveException.failure("获取直播失败");
        }
    }

    public CraftsLive getLiveStatus(Long liveId) {
        LiveShowDTO liveShowDTO = liveShowQueryHandler.getById(liveId);
        CraftsLive  craftsLive  = new CraftsLive();
        craftsLive.setState(liveShowDTO.getState());
        craftsLive.setOnline(liveShowDTO.getOnline());
        return craftsLive;
    }


    public CraftsLiveDetail getCraftsLiveDetail(Long id, Long uid) {
//        LiveQuery query = new LiveQuery();
//        query.setLiveId(id);
//        query.setStart(0);
//        query.setNum(1);
//        BaseList<LiveIndexResp> indexRespBaseList = searchQueryHandler.list(query);
//        if (indexRespBaseList.getCount() == 0) {
//            return null;
//        }

        GeneralLiveCO generalLiveCO = divineSearchManager.getById(id);
        //查询该主播当前直播的下一个直播
        if (uid != null) {
            DBLiveShowQuery search = DBLiveShowQuery.builder()
                    .orderBy("prestarttm asc").userIds(Arrays.asList(uid)).status(Arrays.asList(LiveConst.STATUS_LIVE_NORMAL))
                    .minPreviewTime(new Timestamp(generalLiveCO.getPreStartTime()))
                    .states(Arrays.asList(LiveEnum.LiveState.UNSTART.getCode())).build();

            //转为10位 时间戳，秒
            LiveShowDTO nextLiveDTO = liveShowQueryHandler.getNextLive(search, generalLiveCO.getId());
            return CraftsLiveDetailConvert.toCraftsLiveDetail(generalLiveCO, nextLiveDTO);
        }
        return CraftsLiveDetailConvert.toCraftsLiveDetail(generalLiveCO);
    }

}
