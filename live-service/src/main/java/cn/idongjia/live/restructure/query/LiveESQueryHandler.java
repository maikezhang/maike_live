package cn.idongjia.live.restructure.query;

import cn.idongjia.live.db.mybatis.po.LiveBookCountPO;
import cn.idongjia.live.db.mybatis.query.DBUserStageLiveQuery;
import cn.idongjia.live.restructure.domain.entity.live.UserStageLiveE;
import cn.idongjia.live.restructure.domain.entity.user.LiveAnchor;
import cn.idongjia.live.restructure.domain.entity.zoo.LiveZoo;
import cn.idongjia.live.restructure.dto.LiveBookCountDTO;
import cn.idongjia.live.restructure.dto.LiveShow4IndexDTO;
import cn.idongjia.live.restructure.manager.OutcryManager;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.restructure.manager.ZooManager;
import cn.idongjia.live.restructure.pojo.co.LiveWithCategoryCO;
import cn.idongjia.live.restructure.pojo.query.ESLiveQry;
import cn.idongjia.live.restructure.repo.LiveShowRepo;
import cn.idongjia.live.restructure.repo.UserStageLiveRepo;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/7/19.
 */
@Component
public class LiveESQueryHandler {
    @Resource
    private LiveShowRepo liveShowRepo;

    @Resource
    private ZooManager zooManager;

    @Resource
    private UserManager userManager;

    @Resource
    private LiveBookQueryHandler liveBookQueryHandler;

    @Resource
    private UserStageLiveRepo userStageLiveRepo;

    @Resource
    private OutcryManager outcryManager;

    public List<LiveWithCategoryCO> searchLiveWithCategroy(ESLiveQry esLiveQry) {
        List<LiveShow4IndexDTO> liveShow4IndexDTOS = liveShowRepo.searchLiveWithCategory(esLiveQry.getIds(), esLiveQry.getUpdateTime(), esLiveQry.getOffset(), esLiveQry.getLimit());
        if (Utils.isEmpty(liveShow4IndexDTOS)) {
            return new ArrayList<>();
        }
        List<Long>      liveIds          = new ArrayList<>();
        List<Long>      sessionIds       = new ArrayList<>();
        List<Long>      pureLiveIds      = new ArrayList<>();
        Map<Long, Long> sessionLiveIdMap = new HashMap<>();
        liveShow4IndexDTOS.stream().forEach(liveShow4IndexDTO -> {
            liveIds.add(liveShow4IndexDTO.getId());
            if (liveShow4IndexDTO.getSessionId() != null) {
                sessionIds.add(liveShow4IndexDTO.getSessionId());
                sessionLiveIdMap.put(liveShow4IndexDTO.getSessionId(), liveShow4IndexDTO.getId());
            } else {
                pureLiveIds.add(liveShow4IndexDTO.getId());
            }
        });
        List<Long>         zids   = liveShow4IndexDTOS.stream().map(LiveShow4IndexDTO::getZooId).collect(Collectors.toList());
        Map<Long, LiveZoo> zooMap = zooManager.map(zids);
        Map<Long, LiveAnchor> liveAnchorMap = userManager.takeCraftmsnWithCategoryList(liveShow4IndexDTOS.stream()
                .map(LiveShow4IndexDTO::getUserId).collect(Collectors.toList()));
        Map<Long, LiveBookCountDTO> bookCountDTOMap = new HashMap<>();
        //批量获取专场的订阅人数
        Map<Long, Integer> sessionUidBySessionIdMap = outcryManager.mapBookSessionUidBySessionId(sessionIds);
        sessionIds.stream().forEach(sessionId -> {
            LiveBookCountDTO liveBookCountDTO = new LiveBookCountDTO(new LiveBookCountPO());

            Long liveId = sessionLiveIdMap.get(sessionId);
            Integer count=sessionUidBySessionIdMap.get(sessionId);
            liveBookCountDTO.setCount(count==null ? 0 : count);
            liveBookCountDTO.setLiveId(liveId);
            bookCountDTOMap.put(liveId, liveBookCountDTO);
        });
        if (!Utils.isEmpty(pureLiveIds)) {
            Map<Long, LiveBookCountDTO> liveBookCountDTOMap = liveBookQueryHandler.countMap(pureLiveIds);
            if (!Utils.isEmpty(liveBookCountDTOMap)) {
                bookCountDTOMap.putAll(liveBookCountDTOMap);
            }
        }

        List<UserStageLiveE>            userStageLiveES  = userStageLiveRepo.list(DBUserStageLiveQuery.builder().status(BaseEnum.DataStatus.NORMAL_STATUS.getCode()).liveIds(liveIds).build());
        Map<Long, List<UserStageLiveE>> liveUserStageMap = userStageLiveES.stream().collect(Collectors.groupingBy(UserStageLiveE::getLiveId));
        return liveShow4IndexDTOS.stream().map(liveShow4IndexDTO -> {
            List<UserStageLiveE> stageLiveES = liveUserStageMap.get(liveShow4IndexDTO.getId());
            Long                 userId      = liveShow4IndexDTO.getUserId();
            LiveAnchor           liveAnchor  = liveAnchorMap.get(userId);
            return liveShow4IndexDTO.assembleLiveWithCategory(zooMap.getOrDefault(liveShow4IndexDTO.getZooId(), null),
                    liveAnchor, bookCountDTOMap.get(liveShow4IndexDTO.getId()), stageLiveES);
        }).collect(Collectors.toList());
    }
}
