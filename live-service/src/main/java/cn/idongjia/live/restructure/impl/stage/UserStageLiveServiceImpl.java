package cn.idongjia.live.restructure.impl.stage;

import cn.idongjia.live.api.UserStageLiveServiceI;
import cn.idongjia.live.api.live.pojo.response.MultiResponse;
import cn.idongjia.live.api.live.pojo.response.SingleResponse;
import cn.idongjia.live.db.mybatis.query.DBUserStageLiveQuery;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.biz.UserStageBO;
import cn.idongjia.live.restructure.domain.entity.live.UserStageLiveE;
import cn.idongjia.live.restructure.enums.UserStageEnum;
import cn.idongjia.live.restructure.manager.MqProducerManager;
import cn.idongjia.live.restructure.pojo.cmd.UserStageAddCmd;
import cn.idongjia.live.restructure.pojo.cmd.UserStageDelCmd;
import cn.idongjia.live.restructure.pojo.cmd.UserStageUpdateCmd;
import cn.idongjia.live.restructure.pojo.co.UserStageLiveCO;
import cn.idongjia.live.restructure.pojo.co.UserStageLiveDetailCO;
import cn.idongjia.live.restructure.pojo.query.ESUserStageLiveQry;
import cn.idongjia.live.restructure.pojo.query.UserStageLiveQry;
import cn.idongjia.live.restructure.query.QueryFactory;
import cn.idongjia.live.restructure.query.UserStageLiveQueryHandler;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lc
 * @create at 2018/7/7.
 */
@Service("userStageLiveService")
public class UserStageLiveServiceImpl implements UserStageLiveServiceI {

    @Resource
    private UserStageLiveQueryHandler userStageLiveQueryHandler;


    @Resource
    private UserStageBO userStageBO;

    @Resource
    private MqProducerManager mqProducerManager;

    /**
     * 添加用户强运营直播数据
     *
     * @param userStageAddCmd
     * @return id
     * @see UserStageAddCmd
     */
    @Override
    public SingleResponse<Integer> add(UserStageAddCmd userStageAddCmd) {
        Integer batchAdd = userStageBO.batchAdd(userStageAddCmd.getLiveIds(), userStageAddCmd.getStage());
        return SingleResponse.of(batchAdd);
    }

    /**
     * 删除强运营数据
     *
     * @param userStageDelCmd
     * @return 是否删除成功
     */
    @Override
    public SingleResponse<Integer> delete(UserStageDelCmd userStageDelCmd) {
        long id = userStageDelCmd.getId();

        int deleteResult = userStageBO.delete(id);
        return SingleResponse.of(deleteResult);
    }

    /**
     * 获取新老用户强运营
     *
     * @param esUserStageLiveQry 当前页数 默认请传1
     * @return SingleResponse<UserStageLive>
     * @see UserStageLiveCO
     */
    @Override
    public SingleResponse<UserStageLiveCO> searchWithUserStage(ESUserStageLiveQry esUserStageLiveQry) {
        if (null == esUserStageLiveQry.getType()) {
            return SingleResponse.buildFailure(110, "新老用户类型不能为空");
        }
        List<UserStageLiveE> userStageLives = userStageLiveQueryHandler.list(esUserStageLiveQry.getPage(), BaseEnum.YesOrNo.YES.getCode());
        UserStageLiveCO userStageLiveCO = new UserStageLiveCO();
        List<Long> newStageLiveIds = new ArrayList<>();
        List<Long> oldStageLiveIds = new ArrayList<>();
        if (!Utils.isEmpty(userStageLives)) {
            userStageLives.stream().forEach(userStageLiveE -> {
                if (userStageLiveE.getStage().equals(UserStageEnum.Stage.NEW_STAGE)) {
                    newStageLiveIds.add(userStageLiveE.getLiveId());
                } else {
                    oldStageLiveIds.add(userStageLiveE.getLiveId());
                }
            });
        }
        userStageLiveCO.setNewStageLiveIds(newStageLiveIds);
        userStageLiveCO.setOldStageLiveIds(oldStageLiveIds);
        return SingleResponse.of(userStageLiveCO);
    }

    @Override
    public MultiResponse<UserStageLiveDetailCO> page(UserStageLiveQry userStageLiveQry) {
        userStageLiveQry.setStatus(BaseEnum.DataStatus.NORMAL_STATUS.getCode());
        DBUserStageLiveQuery dbUserStageLiveQuery = QueryFactory.getInstance(userStageLiveQry);
        Integer total = userStageLiveQueryHandler.count(dbUserStageLiveQuery);
        List<UserStageLiveDetailCO> userStageLiveDetailCOS = null;
        if (total > 0) {
            userStageLiveDetailCOS = userStageLiveQueryHandler.page(dbUserStageLiveQuery);
        }
        return MultiResponse.of(userStageLiveDetailCOS, total);
    }

    @Override
    public SingleResponse<Integer> update(UserStageUpdateCmd userStageUpdateCmd) {

        UserStageLiveE userStageLiveE = userStageLiveQueryHandler.getById(userStageUpdateCmd.getId())
                .orElseThrow(() -> LiveException.failure("获取推荐直播失败"));

        // TODO 统一修改魔法数字
        if (userStageUpdateCmd.getWeight() > 999) {
            throw LiveException.failure("超过全站最大值");
        }
        userStageLiveE.setWeight(userStageUpdateCmd.getWeight());
        int result = userStageLiveE.update();
        if(result>0){
            mqProducerManager.pushUserStageLiveMessage(Arrays.asList(userStageLiveE.getLiveId()),1,userStageLiveE.getStage().getCode());
        }
        return SingleResponse.of(result);
    }


}
