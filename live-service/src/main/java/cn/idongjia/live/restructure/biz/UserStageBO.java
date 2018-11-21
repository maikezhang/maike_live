package cn.idongjia.live.restructure.biz;

import cn.idongjia.live.db.mybatis.query.DBLiveShowQuery;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.convert.UserStageLiveConvertor;
import cn.idongjia.live.restructure.domain.entity.live.UserStageLiveE;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.manager.MqProducerManager;
import cn.idongjia.live.restructure.pojo.co.UserStageLiveDetailCO;
import cn.idongjia.live.restructure.query.LiveShowQueryHandler;
import cn.idongjia.live.restructure.query.UserStageLiveQueryHandler;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/7/9.
 */
@Component
public class UserStageBO {
    private static final Log LOGGER = LogFactory.getLog(UserStageBO.class);

    @Resource
    private LiveShowQueryHandler liveShowQueryHandler;

    @Resource
    private UserStageLiveQueryHandler userStageLiveQueryHandler;
    @Resource
    private UserStageLiveConvertor userStageLiveConvertor;

    @Resource
    private MqProducerManager mqProducerManager;


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer batchAdd(List<Long> liveIds, int stage) {
        long now = Utils.getCurrentMillis();
        if (Utils.isEmpty(liveIds)) {
            throw LiveException.failure("添加数据不能为空");
        }
        HashSet h=new HashSet(liveIds);
        liveIds.clear();
        liveIds.addAll(h);
        List<LiveShowDTO> liveShowDTOS=new ArrayList<>();
        try {
            liveShowDTOS = liveShowQueryHandler.list(DBLiveShowQuery.builder().ids(liveIds).status(Arrays.asList(LiveConst.STATUS_LIVE_NORMAL)).build()).get();
        } catch (Exception e) {
            LOGGER.error("获取直播失败");
        }
        if (liveShowDTOS.size() != liveIds.size()) {
            List<Long> exsitLiveIds = liveShowDTOS.stream().map(LiveShowDTO::getId).collect(Collectors.toList());
            liveIds.removeAll(exsitLiveIds);
            String msg = String.format("直播 %s 不存在", liveIds);
            throw LiveException.failure(msg);
        }

        long count = liveIds.stream().map(liveId -> {
            UserStageLiveDetailCO userStageLiveDetailCO = new UserStageLiveDetailCO();
            userStageLiveDetailCO.setLiveId(liveId);
            userStageLiveDetailCO.setStage(stage);
            userStageLiveDetailCO.setWeight(0);
            UserStageLiveE userStageLiveE = userStageLiveConvertor.clientToEntity(userStageLiveDetailCO);
            userStageLiveE.setCreateTime(now);
            userStageLiveE.setUpdateTime(now);
            userStageLiveE.setLiveId(liveId);
            userStageLiveE.setStatus(BaseEnum.DataStatus.NORMAL_STATUS);
            return userStageLiveE.save();
        }).count();
        if(count>0){
            mqProducerManager.pushUserStageLiveMessage(liveIds, 1, stage);

        }
        return new Long(count).intValue();
    }

    public int delete(long id) {
        return userStageLiveQueryHandler.getById(id).map(userStageLiveE -> {
            int delete = userStageLiveE.detete();
            if (delete > 0) {
                mqProducerManager.pushUserStageLiveMessage(Arrays.asList(userStageLiveE.getLiveId()), 0, userStageLiveE.getStage().getCode());
            }
            return delete;

        }).orElseThrow(() -> LiveException.failure("数据[id=" + id + "]不存在"));
    }
}
