package cn.idongjia.live.restructure.impl.live;

import cn.idongjia.live.api.live.LiveService;
import cn.idongjia.live.api.live.pojo.LiveApiResp;
import cn.idongjia.live.api.live.pojo.LiveIndexResp;
import cn.idongjia.live.api.live.pojo.LiveIndexSearch;
import cn.idongjia.live.api.live.pojo.LiveResp;
import cn.idongjia.live.api.live.pojo.PreLiveResp;
import cn.idongjia.live.api.live.pojo.response.MultiResponse;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.live.*;
import cn.idongjia.live.query.LiveSearch;
import cn.idongjia.live.query.live.LiveListApiSearch;
import cn.idongjia.live.query.live.LivePreSearch;
import cn.idongjia.live.query.live.LiveShowSearch;
import cn.idongjia.live.restructure.SpringJUnitNoRollbackTest;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.pojo.co.LiveWithCategoryCO;
import cn.idongjia.live.restructure.pojo.query.ESLiveQry;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author lc on 2018/6/22.
 * @class cn.idongjia.live.restructure.impl.live.LiveServiceImplTest
 */
public class LiveServiceImplTest extends SpringJUnitNoRollbackTest {
    private static final Log LOGGER = LogFactory.getLog(LiveServiceImplTest.class);

    @Resource
    private LiveService liveService;


    @After
    public void tearDown() throws Exception {
    }

    private LiveShow liveShow;

    @Before
    public void initData() {
        liveShow = new LiveShow();
        liveShow.setType(2);
        liveShow.setSuid(81936L);
        liveShow.setTitle("接口测试直播拍会场");
        liveShow.setZrc(20);
        liveShow.setPreViewTm(new Timestamp(1534231203000L));
        liveShow.setPreStartTm(new Timestamp(1534231203000L));
        liveShow.setPreEndTm(new Timestamp(1534231803000L));
        liveShow.setUid(416905L);
        liveShow.setScreenDirection(0);


    }

    @Test
    @Rollback(false)
    public void createLiveShow() {


        Long id = liveService.createLiveShow(liveShow);
        LOGGER.info("{}的直播id为{}", liveShow.getTitle(), id);


    }

    @Test
    @Rollback(false)
    public void updateLiveShowById() {
        liveShow.setTitle("麦克麦克");
        liveShow.setState(1);
        liveShow.setScreenDirection(1);
        liveShow.setZrc(500);
        liveShow.setPreStartTm(new Timestamp(1534231203000L));
        liveShow.setPreEndTm(new Timestamp(1534231803000L));
        liveShow.setShowDesc("你是我的小丫小苹果，就像天边最美的火龙果，哈哈");

        liveService.updateLiveShowById(3684L, liveShow);


    }

    @Test
    @Rollback(false)
    public void deleteLiveShowById() {

        liveService.deleteLiveShowById(3684L);


    }

    @Test
    public void getLiveShowById() {
        LiveShow liveShow = liveService.getLiveShowById(3643L);
        LOGGER.info("liveShow=>{}", liveShow);
    }

    @Test
    public void listLiveShows() {
        LiveShowSearch liveShowSearch = new LiveShowSearch();
        liveShowSearch.setPage(1);
        liveShowSearch.setLimit(100);
        List<LiveShow> liveShows = liveService.listLiveShows(liveShowSearch);
        LOGGER.info("liveshows=>{}", liveShows);
    }

    @Test
    @Rollback(false)
    public void resumeLiveShowById() {
        liveService.resumeLiveShowById(3650L);
    }

    @Test
    @Rollback(false)
    public void startLiveShowById() {


        liveService.startLiveShowById(3692L);
    }

    @Test
    @Rollback(false)
    public void stopLiveShowById() {
        liveService.stopLiveShowById(3650L);
    }

    @Test
    public void getLiveShowByZid() {

        LiveShow liveShow = liveService.getLiveShowByZid(101009L);
        LOGGER.info("liveShow==>{}", liveShow);
    }

    @Test
    public void getPullUrl() {
        LivePullUrl pullUrl = liveService.getPullUrl(3631L);
        LOGGER.info("pullUrl==>{}", pullUrl);

    }

    @Test
    public void getPushUrl() {
        String pushUrl = liveService.getPushUrl(3631L);
        LOGGER.info("pushUrl==>{}", pushUrl);

    }

    @Test
    @Rollback(false)
    public void modifyLiveShowZrc() {
        liveService.modifyLiveShowZrc(3642L, 3);
    }

    @Test
    public void getLiveShowPushUrlByUid() {
        Map<String, String> pushUrlByUid = liveService.getLiveShowPushUrlByUid(190639L);
        LOGGER.info("pushUrlByUid==>{}", pushUrlByUid);

    }

    @Test
    public void getLiveShowPushUrlByMobile() {
        Map<String, String> liveShowPushUrlByMobile = liveService.getLiveShowPushUrlByMobile("15268133260");
        LOGGER.info("liveShowPushUrlByMobile==>{}", liveShowPushUrlByMobile);

    }

    @Test
    public void listAllRecordsByUid() {
        List<LiveRecord> liveRecords = liveService.listAllRecordsByUid(190639L);
        LOGGER.info("liveRecords==>{}", liveRecords);

    }

    @Test
    public void acquireLiveInfo() {
        LiveShow4Article liveShow4Article = liveService.acquireLiveInfo(3631L);
        LOGGER.info("liveShow4Article==>{}", liveShow4Article);

    }

    @Test
    public void acquireRealTimeUserCount() {
        Integer realTimeUserCount = liveService.acquireRealTimeUserCount(3631L);
        LOGGER.info("realTimeUserCount==>{}", realTimeUserCount);

    }

    @Test
    public void getCloudTypeByLid() {
        Integer cloudType = liveService.getCloudTypeByLid(3631L);
        LOGGER.info("cloudType==>{}", cloudType);

    }

    @Test
    @Rollback(false)
    public void resetStartTmAndEndTm() {
        liveService.resetStartTmAndEndTm(3630L);
    }

    @Test
    public void listLiveShowByUid() {
        List<LiveShow> liveShows = liveService.listLiveShowByUid(190639L);
        LOGGER.info("liveShows==>{}", liveShows);
    }

    @Test
    public void searchLiveForIndex() {
        LiveIndexSearch liveIndexSearch = new LiveIndexSearch();
        liveIndexSearch.setPage(1);
        liveIndexSearch.setLimit(10);
        BaseList<LiveIndexResp> liveIndexRespBaseList = liveService.searchLiveForIndex(liveIndexSearch);
        LOGGER.info("liveIndexRespBaseList==>{}", liveIndexRespBaseList);

    }

    @Test
    public void getLiveList() {
        List<LiveApiResp> liveList = liveService.getLiveList(1, 100, 1);
        LOGGER.info("liveList==>{}", liveList);

    }

    @Test
    public void getPreLivList() {
        List<PreLiveResp> preLivList = liveService.getPreLivList(1);
        LOGGER.info("preLivList==>{}", preLivList);

    }

    @Test
    @Rollback(false)
    public void updateLiveOnline() {


        LiveShow liveShow = new LiveShow();
        liveShow.setId(3642L);
        liveShow.setOnline(1);
        liveShow.setType(1);
        liveService.updateLiveOnline(liveShow);

    }

    @Test
    public void getGeneralLiveList4Admin() {
        LiveSearch search = new LiveSearch();
        search.setPage(1);
        search.setLimit(100);
        BaseList<LiveResp> generalLiveList4Admin = liveService.getGeneralLiveList4Admin(search);
        LOGGER.info("generalLiveList4Admin==>{}", generalLiveList4Admin);

    }

    @Test
    @Rollback(false)
    public void modifyGeneralWeight() {
        liveService.modifyGeneralWeight(3642L, 100);
    }

    @Test
    public void getLiveListShareUrl() {
        String liveListShareUrl = liveService.getLiveListShareUrl();
        LOGGER.info("liveListShareUrl==>{}", liveListShareUrl);

    }

    @Test
    public void getNextLive() {
        // TODO: 2018/6/22 这个接口可能有问题呢
        LiveShowSearch search = new LiveShowSearch();
        search.setPage(1);
        search.setLimit(10);
        search.setUid(190639L);
        search.setId(3631L);
        LiveShow nextLive = liveService.getNextLive(search);
        LOGGER.info("nextLive==>{}", nextLive);

    }

    @Test
    public void getLivePushUrl() {
        String livePushUrl = liveService.getLivePushUrl(3631L, Utils.getCurrentMillis());
        LOGGER.info("livePushUrl==>{}", livePushUrl);

    }

    @Test
    public void getLiveMixTabContent() {
        List<Map<String, Object>> liveMixTabContent = liveService.getLiveMixTabContent();
        LOGGER.info("liveMixTabContent==>{}", liveMixTabContent);

    }

    @Test
    public void getBatchLive() {
        List<LiveApiResp> batchLive = liveService.getBatchLive(Arrays.asList(3644l, 3643l, 3642l, 3641l));
        LOGGER.info("batchLive==>{}", batchLive);
    }

    @Test
    public void getWithCategory() {
        MultiResponse<LiveWithCategoryCO> multiResponse = null;
        int page=0;
        do {
            page++;
            ESLiveQry esLiveQry = new ESLiveQry();
            esLiveQry.setPage(page);
            esLiveQry.setLimit(100);
            multiResponse = liveService.searchLiveWithCategroy(esLiveQry);
            LOGGER.info("multiResponse=>{}", multiResponse);
        } while (multiResponse != null && !Utils.isEmpty(multiResponse.getData()));


    }

    @Test
    public void getliveTypeConfig(){
        LOGGER.info(liveService.getliveTypeConfig());
    }

    @Test
    public void getLivePreDataGroup(){

        LivePreSearch search=new LivePreSearch();
        search.setLiveType(LiveEnum.LiveType.PURE_LIVE.getCode());
        search.setOnline(LiveEnum.LiveOnline.ONLINE.getCode());
        search.setState(LiveEnum.LiveState.UNSTART.getCode());
        search.setLimit(15);
        search.setPage(1);
        List<LivePreDateGroup> livePreDataGroup = liveService.getLivePreDataGroup(search);
        LOGGER.info("LivePreData======>>{}",livePreDataGroup);
    }

    @Test
    public void getliveListApi(){
        LiveListApiSearch search=new LiveListApiSearch();
//        search.setLimit(10);
        search.setLiveType(5);
        search.setPage(2);


        LiveResponseApi api = liveService.getliveListApi(search);
        LOGGER.info("LiveData======>>{}",api);



    }
    @Test
    @Rollback(false)
    public void modifyLiveAuctionVideo(){
        liveService.modifyLiveAuctionVideo();
    }




}
