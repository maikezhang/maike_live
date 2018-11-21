package cn.idongjia.live.restructure.query;

import cn.idongjia.live.db.mybatis.query.DBLiveShowQuery;
import cn.idongjia.live.restructure.convert.ConvertorI;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.domain.entity.user.LiveAnchor;
import cn.idongjia.live.restructure.dto.LiveAroundDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.restructure.repo.LiveShowRepo;
import cn.idongjia.live.v2.pojo.LiveAround;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/7/18.
 */
@Component
public class LiveAroundQueryHandler {

    @Resource
    private LiveShowRepo liveShowRepo;
    @Resource
    private UserManager userManager;
    @Resource
    private ConvertorI<LiveAround, LiveEntity, LiveAroundDTO> liveAroundConvertor;

    public List<LiveAround> listAround(Long preStartTime) {
        Long preStartTimeFrom = preStartTime - TimeUnit.MINUTES.toMillis(40);
        Long preStartTimeEnd = preStartTime + TimeUnit.MINUTES.toMillis(40);
        //查询条件封装
        DBLiveShowQuery dbLiveShowQuery = DBLiveShowQuery.builder().minEstimatedStartTime(new Timestamp(preStartTimeFrom))
                .maxEstimatedStartTime(new Timestamp(preStartTimeEnd))
                .orderBy("prestarttm asc").page(1).limit(20).online(cn.idongjia.live.pojo.live.LiveEnum.LiveOnline
                        .ONLINE.getCode())
                .build();
        List<Integer> liveTypes = dbLiveShowQuery.getTypes();
        if (!CollectionUtils.isEmpty(liveTypes)) {
            if (liveTypes.contains(LiveEnum.LiveType.PURE_LIVE.getCode())) {
                liveTypes.add(LiveEnum.LiveType.CRAFTS_PURCHASE_TYPE.getCode());
                liveTypes.add(LiveEnum.LiveType.CRAFTS_TALK_TYPE.getCode());
                liveTypes.add(LiveEnum.LiveType.ELSE_TYPE.getCode());
                liveTypes.add(LiveEnum.LiveType.TREASURE_TYPE.getCode());
                liveTypes.add(LiveEnum.LiveType.OPEN_MATERIAL_TYPE.getCode());
                dbLiveShowQuery.setTypes(liveTypes);
            }

        }
        List<LiveShowDTO> liveShowDTOS = liveShowRepo.listLiveShows(dbLiveShowQuery);

        //匠人查询
        final List<Long> userIds = liveShowDTOS.stream().map(LiveShowDTO::getUserId).collect(Collectors.toList());
        final Map<Long, LiveAnchor> liveAnchorMap = userManager.takeCraftmsnWithCategoryList(userIds);
        List<LiveAround> liveArounds = null;

        if (!Utils.isEmpty(liveShowDTOS)) {
            liveArounds = liveShowDTOS.stream().map(liveShowDTO -> {
                LiveAroundDTO liveAroundDTO = new LiveAroundDTO();
                LiveAnchor liveAnchor = liveAnchorMap.get(liveShowDTO.getUserId());
                liveAroundDTO.setLiveAnchor(liveAnchor);
                liveAroundDTO.setLiveShowDTO(liveShowDTO);
                return liveAroundConvertor.dataToClient(liveAroundDTO);
            }).filter(liveAround -> null != liveAround).collect(Collectors.toList());

        } else {
            liveArounds = new ArrayList<>();
        }

        return liveArounds;

    }
}
