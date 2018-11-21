package cn.idongjia.live.restructure.impl.purelive;

import cn.idongjia.live.api.purelive.PureLiveService;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.purelive.PureLive;
import cn.idongjia.live.pojo.purelive.PureLive4Article;
import cn.idongjia.live.pojo.purelive.PureLiveDO;
import cn.idongjia.live.pojo.purelive.PureLiveDetailDO;
import cn.idongjia.live.query.purelive.PureLiveSearch;
import cn.idongjia.live.restructure.SpringJUnitNoRollbackTest;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lc on 2018/6/23.
 * @class cn.idongjia.live.restructure.impl.purelive.PureLiveServiceImplTest
 */
public class PureLiveServiceImplTest extends SpringJUnitNoRollbackTest {

    private static final Log LOGGER = LogFactory.getLog(PureLiveBookServiceImplTest.class);

    PureLive pureLive;
    @Before
    public void initData(){
        pureLive=new PureLive();
        pureLive.setZid(null);
        pureLive.setDetail(null);
        pureLive.setZrc(null);
        pureLive.setDetails(null);
        pureLive.setAutoOnline(0);
        pureLive.setHuid(416905L);
        pureLive.setTitle("盛听凤日常做壶");
        pureLive.setPic("T3HgxTB4ET1RXrhCrK.jpg");
        pureLive.setPreviewtm(null);
        pureLive.setPrestarttm(Utils.getCurrentMillis()+200000);
        pureLive.setPreendtm(Utils.getCurrentMillis()+500000);
        pureLive.setLiveType(0);
        pureLive.setMuid(81936L);
        pureLive.setScreenDirection(1);
        pureLive.setShowDesc("maikenizuishuai");

    }
    @Resource
    private PureLiveService pureLiveService;

    @Test
    public void createPureLive() {


        pureLiveService.createPureLive(pureLive);

    }

    @Test
    @Rollback(false)
    public void updatePureLiveById() {


        List<PureLiveDetailDO> details=new ArrayList<>();
        PureLiveDetailDO detailDO=new PureLiveDetailDO();
        detailDO.setResourceId(7619L);
        detailDO.setLid(3653L);
        detailDO.setResourceType(0);
        details.add(detailDO);

        pureLive.setDetails(details);

        pureLiveService.updatePureLiveById(3653L,pureLive);
    }

    @Test
    public void deletePureLiveById() {
    }

    @Test
    public void getPureLiveById() {
        PureLive pureLive = pureLiveService.getPureLiveById(3631L);
        LOGGER.info("pureLive==>{}",pureLive);
    }

    @Test
    public void listPureLive() {
        PureLiveSearch pureLiveSearch = new PureLiveSearch();
        pureLiveSearch.setLimit(100);
        pureLiveSearch.setPage(1);
        List<PureLive> pureLives = pureLiveService.listPureLive(pureLiveSearch);
        LOGGER.info("pureLives==>{}",pureLives);

    }

    @Test
    public void getPureLiveCount() {
        PureLiveSearch pureLiveSearch = new PureLiveSearch();
        pureLiveSearch.setLimit(100);
        pureLiveSearch.setPage(1);
        Integer pureLiveCount = pureLiveService.getPureLiveCount(pureLiveSearch);
        LOGGER.info("pureLiveCount==>{}",pureLiveCount);
    }

    @Test
    public void listPureLiveWithCount() {
        PureLiveSearch pureLiveSearch = new PureLiveSearch();
        pureLiveSearch.setLimit(100);
        pureLiveSearch.setPage(1);
        BaseList<PureLive> baseList = pureLiveService.listPureLiveWithCount(pureLiveSearch);
        LOGGER.info("baseList==>{}",baseList);

    }

    @Test
    public void getPureLiveUserCount() {
        Map<String, Object> userCountMap = pureLiveService.getPureLiveUserCount(3631L);
        LOGGER.info("userCountMap==>{}",userCountMap);

    }




    @Test
    @Rollback(false)
    public void addResource() throws Exception {

        List<PureLiveDetailDO> resources=new ArrayList<>();
        PureLiveDetailDO detailDO=new PureLiveDetailDO();
        detailDO.setResourceId(320810L);
        detailDO.setResourceType(1);
        resources.add(detailDO);
        PureLiveDetailDO detailDO1=new PureLiveDetailDO();
        detailDO1.setResourceId(337461L);
        detailDO1.setResourceType(1);
        resources.add(detailDO1);


        pureLiveService.addResource(3650L,resources);
    }

    @Test
    @Rollback(false)
    public void reSortResource() throws Exception {
        List<PureLiveDetailDO> resources=new ArrayList<>();
        PureLiveDetailDO detailDO=new PureLiveDetailDO();
        detailDO.setResourceId(337461L);
        detailDO.setResourceType(1);
        resources.add(detailDO);
        PureLiveDetailDO detailDO1=new PureLiveDetailDO();
        detailDO1.setResourceId(320810L);
        detailDO1.setResourceType(1);
        resources.add(detailDO1);
        pureLiveService.reSortResource(3650L,resources);

    }

    @Test
    @Rollback(false)
    public void deleteResource() throws Exception {

        List<PureLiveDetailDO> resources=new ArrayList<>();
        PureLiveDetailDO detailDO=new PureLiveDetailDO();
        detailDO.setResourceId(337461L);
        detailDO.setResourceType(1);
        resources.add(detailDO);
        pureLiveService.deleteResource(3650L,resources);
    }

    @Test
    @Rollback(false)
    public void liveAutoOnline() throws Exception {


        pureLiveService.liveAutoOnline(3650L,1);

    }

    @Test
    public void pushUserItemOperation() throws Exception {
    }


    public void modifyPureLiveZrc() {
    }

    @Test
    public void getPureLiveResources() {
    }

    @Test
    public void acquirePureLiveData() {
        PureLive4Article pureLive4Article = pureLiveService.acquirePureLiveData(3631L);
        LOGGER.info("pureLive4Article==>{}",pureLive4Article);

    }

    @Test
    public void listPureLivesByUid() {
        List<Long> liveIds = pureLiveService.listPureLivesByUid(190639L);
        LOGGER.info("liveIds==>{}",liveIds);

    }

    @Test
    public void getTemplateByLid() {
        Long templateByLid = pureLiveService.getTemplateByLid(3631L);
        LOGGER.info("templateByLid==>{}",templateByLid);

    }

    @Test
    public void getFeeds() {
        PureLiveSearch pureLiveSearch = new PureLiveSearch();
        pureLiveSearch.setPage(2);
        pureLiveSearch.setLimit(10);
        Map<String, Object> feeds = pureLiveService.getFeeds(95050L, pureLiveSearch);
        LOGGER.info("feeds==>{}",feeds);

    }

    @Test
    public void getClassifiedFeeds() {
        PureLiveSearch pureLiveSearch = new PureLiveSearch();
        pureLiveSearch.setPage(1);
        pureLiveSearch.setLimit(10);
        List<Object> classifiedFeeds = pureLiveService.getClassifiedFeeds(7L, pureLiveSearch);
        LOGGER.info("classifiedFeeds==>{}",classifiedFeeds);

    }

    @Test
    public void getBookCenterFeeds() {
        PureLiveSearch pureLiveSearch = new PureLiveSearch();
        pureLiveSearch.setPage(1);
        pureLiveSearch.setLimit(10);
        List<Object> bookCenterFeeds = pureLiveService.getBookCenterFeeds(95050L, pureLiveSearch);
        LOGGER.info("bookCenterFeeds==>{}",bookCenterFeeds);

    }

    @Test
    public void getForeShows() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String, Object>> foreShows = pureLiveService.getForeShows(95050L, sdf.format(new Date(Utils.getCurrentMillis())));
        LOGGER.info("foreShows==>{}",foreShows);

    }

    @Test
    public void getBasePureLive() {
        PureLiveDO basePureLive = pureLiveService.getBasePureLive(3631L);
        LOGGER.info("basePureLive==>{}",basePureLive);

    }

    @Test
    public void listPureLiveByCraftsUid() {
        PureLiveSearch pureLiveSearch = new PureLiveSearch();
        pureLiveSearch.setPage(1);
        pureLiveSearch.setLimit(10);
        List<Object> objects = pureLiveService.listPureLiveByCraftsUid(95050L, pureLiveSearch, false);
        LOGGER.info("objects==>{}",objects);

    }

    @Test
    public void getPureLiveByLidWithLock() {
        PureLive pureLiveByLidWithLock = pureLiveService.getPureLiveByLidWithLock(3631L);
        LOGGER.info("pureLiveByLidWithLock==>{}",pureLiveByLidWithLock);

    }

    @Test
    public void getPureLiveByLidWithUid() {
        PureLive pureLiveByLidWithUid = pureLiveService.getPureLiveByLidWithUid(95050L, 3631L);
        LOGGER.info("pureLiveByLidWithUid==>{}",pureLiveByLidWithUid);

    }

    @Test
    public void replicateZooMessage() {
    }

    @Test
    public void listItemResource() {
        Map<String, Object> stringObjectMap = pureLiveService.listItemResource(3631L);
        LOGGER.info("stringObjectMap==>{}",stringObjectMap);

    }


}
