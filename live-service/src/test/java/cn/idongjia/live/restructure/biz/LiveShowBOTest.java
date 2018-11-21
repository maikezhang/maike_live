package cn.idongjia.live.restructure.biz;

import cn.idongjia.live.restructure.SpringJUnitNoRollbackTest;
import org.junit.Test;

import javax.annotation.Resource;


/**
 * Created by zhangmaike on 2018/6/21.
 */
public class LiveShowBOTest extends SpringJUnitNoRollbackTest {


    @Resource
    private LiveShowBO liveShowBO;


    @Test
    public void updateVideoCoverLid(){
        liveShowBO.updateVideoCoverLiveId();
    }

}