package cn.idongjia.live.restructure.impl.live;

import cn.idongjia.live.api.live.ModuleService;
import cn.idongjia.live.pojo.live.LiveModule;
import cn.idongjia.live.query.live.LiveModuleSearch;
import cn.idongjia.live.restructure.SpringJUnitNoRollbackTest;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.Assert.*;

public class ModuleServiceImplTest extends SpringJUnitNoRollbackTest {
private static final Log LOG= LogFactory.getLog(ModuleServiceImplTest.class);

    @Autowired
    ModuleService moduleService;

    private LiveModule liveModule;

    @Before
    public void initModule(){
        liveModule=new LiveModule();
        liveModule.setPosition(1);
        liveModule.setUpdateTime(Utils.getCurrentMillis());
        liveModule.setCreateTime(Utils.getCurrentMillis());
        liveModule.setTitle("单元测试1");
        liveModule.setSubTitle("单元测试1");
        liveModule.setStartTime(Utils.getCurrentMillis());
        liveModule.setPic("133r");
        liveModule.setJumpType(1);
        liveModule.setJumpAddr("1");
        liveModule.setDesc("wecwevwv");
    }

    @Test
    @Rollback(false)
    public void createModule() throws Exception {
       Long id= moduleService.createModule(liveModule);
        Assert.assertTrue(id !=null);
    }

    @Test
    @Rollback(false)
    public void modifyModule() throws Exception {
        liveModule.setTitle("单元测试修改");
        Assert.assertTrue(moduleService.modifyModule(1L,liveModule));
    }

    @Test
    public void deleteModule() throws Exception {
        moduleService.deleteModule(1L);
        Assert.assertTrue(true);
    }

    @Test
    @Rollback(false)
    public void getModuleById() throws Exception {
       LiveModule liveModule= moduleService.getModuleById(1L);
       Assert.assertTrue(null !=liveModule);
    }

    @Test
    @Rollback(false)
    public void listModuleGroupByStyle() throws Exception {
        List<LiveModule> liveModules=moduleService.listModuleGroupByStyle(3);
        LOG.info("style在线数据：{}",liveModules);
        Assert.assertTrue(true);
    }

    @Test
    @Rollback(false)
    public void onShelfModuleGroup() throws Exception {
        LOG.info("style数据上线：{}",moduleService.onShelfModuleGroup(7));
    }

    @Test
    @Rollback(false)
    public void listLiveModuleByPosition() throws Exception {

        LOG.info("某个位置数据：{}",moduleService.listLiveModuleByPosition(1,new LiveModuleSearch()));
    }

    @Test
    @Rollback(false)
    public void listModuleGroup() throws Exception {
       LOG.info("group数据：{}",moduleService.listModuleGroup());
    }

    @Test
    @Rollback(false)
    public void isDuplicateStartTime() throws Exception {
       boolean b= moduleService.isDuplicateStartTime(1,1531562400000L);
        LOG.info("冲突了没？{}",b);
       Assert.assertTrue(true);
    }

}