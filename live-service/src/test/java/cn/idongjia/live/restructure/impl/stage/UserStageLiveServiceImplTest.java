package cn.idongjia.live.restructure.impl.stage;

import cn.idongjia.live.api.UserStageLiveServiceI;
import cn.idongjia.live.api.live.pojo.response.MultiResponse;
import cn.idongjia.live.api.live.pojo.response.SingleResponse;
import cn.idongjia.live.restructure.SpringJUnitNoRollbackTest;
import cn.idongjia.live.restructure.enums.UserStageEnum;
import cn.idongjia.live.restructure.pojo.cmd.UserStageAddCmd;
import cn.idongjia.live.restructure.pojo.cmd.UserStageDelCmd;
import cn.idongjia.live.restructure.pojo.cmd.UserStageUpdateCmd;
import cn.idongjia.live.restructure.pojo.co.UserStageLiveDetailCO;
import cn.idongjia.live.restructure.pojo.query.UserStageLiveQry;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Created by zhangmaike on 2018/7/11.
 */
public class UserStageLiveServiceImplTest extends SpringJUnitNoRollbackTest {

    private static  final Log LOGGER=LogFactory.getLog(UserStageLiveServiceImplTest.class);

    @Resource
    private UserStageLiveServiceI userStageLiveServiceI;

    @Test
    @Rollback(false)
    public void add() throws Exception {

        UserStageAddCmd cmd = new UserStageAddCmd();
        cmd.setLiveIds(Arrays.asList(3654L, 3653L));
        cmd.setStage(UserStageEnum.Stage.NEW_STAGE.getCode());
        SingleResponse<Integer> response = userStageLiveServiceI.add(cmd);
        LOGGER.info("response==>{}", response);

    }

    @Test
    @Rollback(false)
    public void delete() throws Exception {
        UserStageDelCmd cmd = new UserStageDelCmd();
        cmd.setId(1);
        userStageLiveServiceI.delete(cmd);
    }

    @Test
    public void searchWithUserStage() throws Exception {

    }

    @Test
    public void page() throws Exception {
        UserStageLiveQry userStageLiveQry=new UserStageLiveQry();
        userStageLiveQry.setLimit(10);
        userStageLiveQry.setPage(1);
        userStageLiveQry.setType(1);
        MultiResponse<UserStageLiveDetailCO> multiResponse = userStageLiveServiceI.page(userStageLiveQry);
        LOGGER.info("multiResponse==>{}", multiResponse);

    }

    @Test
    @Rollback(false)
    public void update() throws Exception {

        UserStageUpdateCmd cmd = new UserStageUpdateCmd();
        cmd.setId(4L);
        cmd.setWeight(100);
        userStageLiveServiceI.update(cmd);

    }

}
