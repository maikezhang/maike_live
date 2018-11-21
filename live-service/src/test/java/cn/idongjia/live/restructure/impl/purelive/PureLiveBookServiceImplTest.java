package cn.idongjia.live.restructure.impl.purelive;

import cn.idongjia.live.api.purelive.PureLiveBookService;
import cn.idongjia.live.pojo.purelive.book.AnchorsBookDO;
import cn.idongjia.live.pojo.purelive.book.PureLiveBookDO;
import cn.idongjia.live.restructure.SpringJUnitNoRollbackTest;
import org.junit.Before;
import cn.idongjia.live.pojo.user.Anchor;
import cn.idongjia.live.query.purelive.book.AnchorsBookSearch;
import cn.idongjia.live.query.purelive.book.PureLiveBookSearch;
import cn.idongjia.live.restructure.SpringJUnitNoRollbackTest;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangmaike on 2018/6/22.
 */
public class PureLiveBookServiceImplTest extends SpringJUnitNoRollbackTest {


    private static final Log LOGGER = LogFactory.getLog(PureLiveBookServiceImplTest.class);


    @Resource
    private PureLiveBookService pureLiveBookService;

    private PureLiveBookDO pureLiveBookDO;

    private AnchorsBookDO anchorsBookDO;

    @Before
    public void initData(){
        pureLiveBookDO=new PureLiveBookDO();
        pureLiveBookDO.setLid(3650L);
        pureLiveBookDO.setUid(526L);


        anchorsBookDO=new AnchorsBookDO();
        anchorsBookDO.setAnchorId(416905L);
        anchorsBookDO.setUid(526L);

    }


    @Test
    @Rollback(false)
    public void addPureLiveBook() throws Exception {
        pureLiveBookService.addPureLiveBook(pureLiveBookDO);

    }

    @Test
    @Rollback(false)
    public void deletePureLiveBook() throws Exception {

        pureLiveBookDO.setId(10352L);
        pureLiveBookService.deletePureLiveBook(pureLiveBookDO);
    }

    @Test
    public void updatePureLiveBook() throws Exception {
    }

    @Test
    public void listPureLiveBooks() throws Exception {
        PureLiveBookSearch pureLiveBookSearch = new PureLiveBookSearch();
        pureLiveBookSearch.setLid(3631L);
        List<PureLiveBookDO> pureLiveBookDOS = pureLiveBookService.listPureLiveBooks(pureLiveBookSearch);
        LOGGER.info("pureLiveBookDOS==>{}", pureLiveBookDOS);
    }

    @Test
    public void countPureLiveBooks() throws Exception {

        PureLiveBookSearch pureLiveBookSearch = new PureLiveBookSearch();
        pureLiveBookSearch.setLid(3631L);
        Integer count = pureLiveBookService.countPureLiveBooks(pureLiveBookSearch);
        LOGGER.info("count==>{}", count);
    }

    @Test
    @Rollback(false)
    public void addAnchorsBook() throws Exception {




        pureLiveBookService.addAnchorsBook(anchorsBookDO);



    }

    @Test
    @Rollback(false)
    public void deleteAnchorsBook() throws Exception {


        pureLiveBookService.deleteAnchorsBook(anchorsBookDO);
    }

    @Test
    public void updateAnchorsBook() throws Exception {
    }

    @Test
    public void listAnchorsBooks() throws Exception {
        AnchorsBookSearch anchorsBookSearch = new AnchorsBookSearch();
        anchorsBookSearch.setUid(95050L);
        List<AnchorsBookDO> anchorsBookDOS = pureLiveBookService.listAnchorsBooks(anchorsBookSearch);
        LOGGER.info("anchorsBookDOS==>{}", anchorsBookDOS);
    }

    @Test
    public void countAnchorsBooks() throws Exception {
        AnchorsBookSearch anchorsBookSearch = new AnchorsBookSearch();
        anchorsBookSearch.setUid(95050L);
        Integer count = pureLiveBookService.countAnchorsBooks(anchorsBookSearch);
        LOGGER.info("count==>{}", count);
    }

    @Test
    public void batchAddPureLiveBook() throws Exception {


    }

    @Test
    public void batchRemovePureLiveBook() throws Exception {


    }

    @Test
    public void listBookUidsByLid() throws Exception {

        List<Long> uids = pureLiveBookService.listBookUidsByLid(3631L);
        LOGGER.info("uids==>{}", uids);
    }

    @Test
    public void listAnchorsByUid() throws Exception {
        AnchorsBookSearch anchorsBookSearch = new AnchorsBookSearch();
        anchorsBookSearch.setUid(95050L);
        List<Anchor> anchors = pureLiveBookService.listAnchorsByUid(95050L, anchorsBookSearch);
        LOGGER.info("anchors==>{}", anchors);
    }

    @Test
    public void listPureLivesByUid() throws Exception {
        PureLiveBookSearch pureLiveBookSearch = new PureLiveBookSearch();
        pureLiveBookSearch.setLid(3631L);
        List<Long> liveIds = pureLiveBookService.listPureLivesByUid(95050L, pureLiveBookSearch);
        LOGGER.info("liveIds==>{}", liveIds);

    }

    @Test
    public void countPureLivesByUid() throws Exception {
        Integer count = pureLiveBookService.countPureLivesByUid(95050L);
        LOGGER.info("count==>{}", count);

    }

    @Test
    public void countAnchorsByUid() throws Exception {
        Integer count = pureLiveBookService.countAnchorsByUid(95050L);
        LOGGER.info("count==>{}", count);

    }

    @Test
    public void countBooksByUid() throws Exception {
        Map<String, Object> countMap = pureLiveBookService.countBooksByUid(95050L);
        LOGGER.info("countMap==>{}", countMap);

    }

    @Test
    public void getLiveRemindTxt() throws Exception {
        Map<String, String> liveRemindTxt = pureLiveBookService.getLiveRemindTxt(95050L, 190639L);
        LOGGER.info("liveRemindTxt==>{}", liveRemindTxt);
    }

}
