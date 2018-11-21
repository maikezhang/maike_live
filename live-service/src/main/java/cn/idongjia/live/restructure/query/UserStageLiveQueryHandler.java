package cn.idongjia.live.restructure.query;

import cn.idongjia.live.db.mybatis.query.DBLivePureQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveShowQuery;
import cn.idongjia.live.db.mybatis.query.DBUserStageLiveQuery;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.convert.UserStageLiveConvertor;
import cn.idongjia.live.restructure.domain.entity.live.UserStageLiveE;
import cn.idongjia.live.restructure.dto.LivePureDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.restructure.pojo.co.UserStageLiveDetailCO;
import cn.idongjia.live.restructure.repo.LiveShowRepo;
import cn.idongjia.live.restructure.repo.UserStageLiveRepo;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.outcry.pojo.AuctionSession;
import cn.idongjia.outcry.pojo.Session4Live;
import cn.idongjia.user.lib.pojo.vo.CustomerVo;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 新老用户强运营直播查询
 *
 * @author lc
 * @create at 2018/7/6.
 */
@Component
public class UserStageLiveQueryHandler {
    private static final Log logger = LogFactory.getLog(UserStageLiveQueryHandler.class);

    @Resource
    private UserStageLiveRepo userStageLiveRepo;
    @Resource
    private LiveShowQueryHandler liveShowQueryHandler;


    @Resource
    private LivePureQueryHandler livePureQueryHandler;

    @Resource
    private SessionQueryHandler sessionQueryHandler;

    @Resource
    private UserManager userManager;

    @Resource
    private UserStageLiveConvertor userStageLiveConvertor;


    public List<UserStageLiveE> list(int page, Integer showStatus) {
        DBUserStageLiveQuery dbUserStageLiveQuery = DBUserStageLiveQuery.builder().status(BaseEnum.DataStatus.NORMAL_STATUS.getCode()).showStatus(showStatus).page(page).build();
        return userStageLiveRepo.list(dbUserStageLiveQuery);
    }

    public Optional<UserStageLiveE> getById(long id) {
        return userStageLiveRepo.get(id);
    }

    public List<UserStageLiveE> list(DBUserStageLiveQuery dbUserStageLiveQuery) {
        return userStageLiveRepo.list(dbUserStageLiveQuery);
    }

    public Map<Long, UserStageLiveE> map(DBUserStageLiveQuery dbUserStageLiveQuery) {
        List<UserStageLiveE> userStageLiveES = userStageLiveRepo.list(dbUserStageLiveQuery);
        Map<Long, UserStageLiveE> userStageLiveEMap = null;
        if (null != userStageLiveES) {
            userStageLiveEMap = userStageLiveES.stream().collect(Collectors.toMap(UserStageLiveE::getLiveId, v1 -> v1, (v1, v2) -> v1));
        }
        if (null == userStageLiveEMap) {
            userStageLiveEMap = new HashMap<>();
        }
        return userStageLiveEMap;
    }


    public Integer count(DBUserStageLiveQuery dbUserStageLiveQuery) {
        return userStageLiveRepo.count(dbUserStageLiveQuery);
    }


    public List<UserStageLiveDetailCO> page(DBUserStageLiveQuery dbUserStageLiveQuery) {
        List<UserStageLiveDetailCO> userStageLiveCOS = null;
        List<UserStageLiveE> userStageLiveES = list(dbUserStageLiveQuery);
        if (Utils.isEmpty(userStageLiveES)) {
            return new ArrayList<>();
        }
        List<Long> liveIds = userStageLiveES.stream().map(UserStageLiveE::getLiveId).collect(Collectors.toList());
        Future<Map<Long, LiveShowDTO>> liveFuture = liveShowQueryHandler.map(DBLiveShowQuery.builder().ids(liveIds).status(LiveConst.STATUS_LIVE_AVALIABLE).build());
        Future<Map<Long, LivePureDTO>> livePureFuture = livePureQueryHandler.map(DBLivePureQuery.builder().liveIds(liveIds).build());

        try {
            Map<Long, LiveShowDTO> liveShowDTOMap = liveFuture.get();
            Map<Long, LivePureDTO> livePureDTOMap = livePureFuture.get();
            List<Long> userIds = new ArrayList<>();
            List<Long> sessionLiveIds = new ArrayList<>();
            liveShowDTOMap.values().stream().forEach(liveShowDTO -> {
                userIds.add(liveShowDTO.getUserId());
                if (LiveEnum.LiveType.LIVE_AUCTION.getCode() == liveShowDTO.getType()) {
                    sessionLiveIds.add(liveShowDTO.getId());
                }
            });
            logger.info("查询专场数据{}", sessionLiveIds);
            Map<Long, Session4Live> sessionMap = sessionQueryHandler.mapByLiveId(sessionLiveIds);
            Map<Long, CustomerVo> customerVoMap = userManager.takeBatchCustomer(userIds);
            userStageLiveCOS = userStageLiveES.stream().map(userStageLiveE -> {
                UserStageLiveDetailCO userStageLiveDetailCO = userStageLiveConvertor.entityToClient(
                        userStageLiveE);
                Long liveId = userStageLiveE.getLiveId();
                if (sessionLiveIds.contains(liveId)) {
                    Session4Live auctionSession = sessionMap.get(liveId);
                    if (null != auctionSession) {
                        userStageLiveDetailCO.setSessionId(auctionSession.getId());
                        userStageLiveDetailCO.setPic(auctionSession.getPic());
                    }
                } else {
                    LivePureDTO livePureDTO = livePureDTOMap.get(liveId);
                    if(Objects.nonNull(livePureDTO)){
                        userStageLiveDetailCO.setPic(livePureDTO.getPic());
                    }

                }
                LiveShowDTO liveShowDTO = liveShowDTOMap.get(liveId);

                if (null != liveShowDTO) {
                    CustomerVo customerVo = customerVoMap.get(liveShowDTO.getUserId());
                    if (null != customerVo) {
                        userStageLiveDetailCO.setCraftman(customerVo.getName());
                    }
                    userStageLiveDetailCO.setState(liveShowDTO.getState());
                    userStageLiveDetailCO.setTitle(liveShowDTO.getTitle());
                }


                return userStageLiveDetailCO;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.warn("获取直播失败{}", e);
            throw LiveException.failure("获取直播失败");
        }

        return userStageLiveCOS;
    }

}
