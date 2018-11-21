package cn.idongjia.live.restructure.impl.purelive;

import cn.idongjia.live.api.purelive.CraftsPureLiveService;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.purelive.PureLive;
import cn.idongjia.live.pojo.purelive.PureLiveDetailDO;
import cn.idongjia.live.query.purelive.PureLiveSearch;
import cn.idongjia.live.restructure.SpringJUnitNoRollbackTest;
import cn.idongjia.live.v2.pojo.ItemResource;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangmaike on 2018/6/22.
 */
public class CraftsLiveServiceImplTest extends SpringJUnitNoRollbackTest {

    private static final Log LOGGER = LogFactory.getLog(CraftsLiveServiceImplTest.class);

    @Resource
    private CraftsPureLiveService craftsPureLiveService;

    private PureLive pureLive;

    @Before
    public void initData() {
        pureLive = new PureLive();
        pureLive.setZid(null);
        pureLive.setDetail(null);
        pureLive.setZrc(null);
        pureLive.setDetails(null);
        pureLive.setAutoOnline(1);
        pureLive.setHuid(416905L);
        pureLive.setTitle("maike盛听凤日常做壶");
        pureLive.setPic("T3HgxTB4ET1RXrhCrK.jpg");
        pureLive.setPreviewtm(null);
        pureLive.setPrestarttm(Utils.getCurrentMillis() + 200000);
        pureLive.setPreendtm(Utils.getCurrentMillis() + 500000);
        pureLive.setLiveType(0);
        pureLive.setScreenDirection(1);
        pureLive.setShowDesc("maikenizuishuai");


    }

    @Test
    @Rollback(false)
    public void createCraftsPureLive() throws Exception {

        craftsPureLiveService.createCraftsPureLive(pureLive);

    }

    @Test
    @Rollback(false)
    public void updateCraftsPureLive() throws Exception {

        pureLive.setTitle("你是不是傻");
        pureLive.setScreenDirection(0);
        pureLive.setZrc(1000);
        pureLive.setLiveType(0);
        craftsPureLiveService.updateCraftsPureLive(3649L, pureLive);

    }


    @Test
    public void listCraftsPureLives() {
        PureLiveSearch pureLiveSearch = new PureLiveSearch();
        pureLiveSearch.setPage(1);
        pureLiveSearch.setLimit(10);
        BaseList<PureLive> baseList = craftsPureLiveService.listCraftsPureLives(190639L, pureLiveSearch);
        LOGGER.info("baseList==>{}", baseList);

    }

    @Test
    public void getCraftsPureLive() {


        PureLive craftsPureLive = craftsPureLiveService.getCraftsPureLive(3631L);
        LOGGER.info("craftsPureLive==>{}", craftsPureLive);
    }


    @Test
    public void countCraftsPureLives() {
        PureLiveSearch pureLiveSearch = new PureLiveSearch();
        pureLiveSearch.setPage(1);
        pureLiveSearch.setLimit(10);
        Integer pureLiveCount = craftsPureLiveService.countCraftsPureLives(190639L, pureLiveSearch);
        LOGGER.info("pureLiveCount==>{}", pureLiveCount);

    }


    @Test
    @Rollback(false)
    public void addItemResource() throws Exception {
        List<PureLiveDetailDO> resources = new ArrayList<>();
        PureLiveDetailDO detailDO = new PureLiveDetailDO();
        detailDO.setResourceId(320810L);
        detailDO.setResourceType(1);
        resources.add(detailDO);
        PureLiveDetailDO detailDO1 = new PureLiveDetailDO();
        detailDO1.setResourceId(337461L);
        detailDO1.setResourceType(1);
        resources.add(detailDO1);


        craftsPureLiveService.addItemResource(3649L, resources);

    }

    public void listItemResource() {

        List<ItemResource> itemResources = craftsPureLiveService.listItemResource(3631L);
        LOGGER.info("itemResources==>{}", itemResources);

    }

    @Test
    @Rollback(false)
    public void deleteItemResource() throws Exception {

        List<PureLiveDetailDO> resources = new ArrayList<>();
        PureLiveDetailDO detailDO = new PureLiveDetailDO();
        detailDO.setResourceId(320810L);
        detailDO.setResourceType(1);
        resources.add(detailDO);
//        PureLiveDetailDO detailDO1=new PureLiveDetailDO();
//        detailDO1.setResourceId(337461L);
//        detailDO1.setResourceType(1);
//        resources.persist(detailDO1);
        craftsPureLiveService.deleteItemResource(3649L, resources);

    }

    @Test
    @Rollback(false)
    public void manageItemResource() throws Exception {


        List<PureLiveDetailDO> resources = new ArrayList<>();
        PureLiveDetailDO detailDO = new PureLiveDetailDO();
        detailDO.setResourceId(337461L);
        detailDO.setResourceType(1);
        resources.add(detailDO);
        PureLiveDetailDO detailDO1 = new PureLiveDetailDO();
        detailDO1.setResourceId(320810L);
        detailDO1.setResourceType(1);
        resources.add(detailDO1);
        craftsPureLiveService.manageItemResource(3649L, resources);
    }




}
