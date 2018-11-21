package cn.idongjia.live.restructure.impl.live;

import cn.idongjia.live.api.live.LiveLikeServiceI;
import cn.idongjia.live.restructure.SpringJUnitNoRollbackTest;
import cn.idongjia.live.restructure.pojo.cmd.LiveLikeAddCmd;
import cn.idongjia.live.restructure.pojo.cmd.LiveLikeDelCmd;
import cn.idongjia.live.restructure.pojo.co.live.LiveLikeCO;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by zhangmaike on 2018/9/3.
 */
public class LiveLikeServiceImplTest extends SpringJUnitNoRollbackTest {

    @Resource
    private LiveLikeServiceI liveLikeServiceI;
    @Test
    @Rollback(false)
    public void addLike() throws Exception {

        LiveLikeCO co=new LiveLikeCO();
        co.setLiveId(3682L);
        co.setUserId(190639L);
        LiveLikeAddCmd cmd=new LiveLikeAddCmd();
        cmd.setLiveLikeCO(co);
        liveLikeServiceI.addLike(cmd);


    }

    @Test
    @Rollback(false)
    public void deleteLike() throws Exception {


        LiveLikeCO co=new LiveLikeCO();
        co.setLiveId(1L);
        co.setUserId(2L);
        LiveLikeDelCmd cmd=new LiveLikeDelCmd();
        cmd.setLiveLikeCO(co);
        liveLikeServiceI.deleteLike(cmd);
    }

}