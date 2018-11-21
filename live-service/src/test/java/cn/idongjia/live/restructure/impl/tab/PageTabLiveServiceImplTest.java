package cn.idongjia.live.restructure.impl.tab;

import cn.idongjia.live.api.PageTabLiveServiceI;
import cn.idongjia.live.api.PageTabServiceI;
import cn.idongjia.live.api.live.pojo.response.MultiResponse;
import cn.idongjia.live.restructure.SpringJUnitNoRollbackTest;
import cn.idongjia.live.restructure.impl.live.LiveServiceImplTest;
import cn.idongjia.live.restructure.pojo.cmd.tab.*;
import cn.idongjia.live.restructure.pojo.co.CategoryCO;
import cn.idongjia.live.restructure.pojo.co.tab.LiveCO;
import cn.idongjia.live.restructure.pojo.co.tab.PageTabCO;
import cn.idongjia.live.restructure.pojo.co.tab.PageTabLiveCO;
import cn.idongjia.live.restructure.pojo.query.PageTabLiveApiQry;
import cn.idongjia.live.restructure.pojo.query.PageTabLiveQry;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import lombok.ToString;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author lc on 2018/7/10.
 * @class cn.idongjia.live.restructure.impl.tab.PageTabLiveServiceImplTest
 */
public class PageTabLiveServiceImplTest extends SpringJUnitNoRollbackTest {
    private static final Log logger = LogFactory.getLog(PageTabLiveServiceImplTest.class);

    @Resource
    private PageTabLiveServiceI pageTabLiveService;


    @Test
    public void apiPage(){
        PageTabLiveApiQry pageTabLiveApiQry = new PageTabLiveApiQry();
        pageTabLiveApiQry.setTabId(0L);
        pageTabLiveApiQry.setPage(1);
        pageTabLiveApiQry.setLimit(10);
        MultiResponse<LiveCO> response = pageTabLiveService.pageApi(pageTabLiveApiQry);
        logger.info("response===>{}",response);
    }

    @Test
    @Rollback(false)
    public void add(){
        PageTabLiveAddCmd cmd=new PageTabLiveAddCmd();
        List<Long> liveIds= Arrays.asList(3654L,3653L,1L);
        cmd.setLiveIds(liveIds);
        cmd.setTabId(4L);
        pageTabLiveService.add(cmd);
    }

    @Test
    @Rollback(false)
    public void updateWeight(){
        PageTabLiveUpdateWeightCmd cmd=new PageTabLiveUpdateWeightCmd();
        cmd.setId(1L);
        cmd.setWeight(100);
        pageTabLiveService.updateWeight(cmd);
    }

    @Test
    @Rollback(false)
    public void delete(){
        PageTabLiveDeleteCmd cmd=new PageTabLiveDeleteCmd();
        cmd.setId(1L);
        pageTabLiveService.delete(cmd);
    }

    @Test
    public void page(){
        PageTabLiveQry qry=new PageTabLiveQry();
        qry.setTabId(11L);
        qry.setTitle("盛听凤日常做壶");
        MultiResponse<PageTabLiveCO> page = pageTabLiveService.page(qry);
        logger.info("response==>{}",page.getData());

    }





}
