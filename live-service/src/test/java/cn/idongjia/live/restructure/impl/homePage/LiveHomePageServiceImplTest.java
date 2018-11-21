package cn.idongjia.live.restructure.impl.homePage;

import cn.idongjia.live.api.homePage.LiveHomePageServiceI;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.restructure.SpringJUnitNoRollbackTest;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by zhangmaike on 2018/7/20.
 */
public class LiveHomePageServiceImplTest extends SpringJUnitNoRollbackTest {
    private static final Log LOGGER = LogFactory.getLog(LiveHomePageServiceImplTest.class);
    @Resource
    private LiveHomePageServiceI liveHomePageServiceI;

    @Test
    public void getHomePageBase() throws Exception {
        LOGGER.info(liveHomePageServiceI.getHomePageBase(false));
    }

    @Test
    public void searchLive() throws Exception {
        BaseList baseList=  liveHomePageServiceI.searchLive("哈哈哈",1,10);
        LOGGER.info("baseList=>{}", baseList);
    }

}