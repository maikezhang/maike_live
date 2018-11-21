package cn.idongjia.live.restructure.impl.purelive;

import cn.idongjia.live.api.purelive.PureLiveTagService;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.purelive.tag.PureLiveTagDO;
import cn.idongjia.live.pojo.purelive.tag.PureLiveTagRelDO;
import cn.idongjia.live.pojo.purelive.tag.TemplateTagRelDO;
import cn.idongjia.live.query.purelive.tag.PureLiveTagRelSearch;
import cn.idongjia.live.query.purelive.tag.PureLiveTagSearch;
import cn.idongjia.live.restructure.SpringJUnitNoRollbackTest;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhangmaike on 2018/6/23.
 */
public class PureLiveTagServiceImplTest extends SpringJUnitNoRollbackTest {

    private static final Log LOGGER = LogFactory.getLog(PureLiveTagServiceImplTest.class);
    @Resource
    private PureLiveTagService pureLiveTagService;


    private PureLiveTagDO pureLiveTagDO;

    private PureLiveTagRelDO pureLiveTagRelDO;

    @Before
    public void initData() {

        pureLiveTagDO = new PureLiveTagDO();
        pureLiveTagDO.setDesc("hahah");
        pureLiveTagDO.setName("麦克专属");
        pureLiveTagDO.setPic("sdfeef");
        pureLiveTagDO.setType(2);
        pureLiveTagDO.setWeight(2);
        pureLiveTagDO.setStatus(0);


        pureLiveTagRelDO = new PureLiveTagRelDO();
        pureLiveTagRelDO.setLid(3650L);
        pureLiveTagRelDO.setTagId(32L);
        pureLiveTagRelDO.setStatus(0);


    }


    @Test
    @Rollback(false)
    public void addPureLiveTag() throws Exception {

//        pureLiveTagService.addPureLiveTag(pureLiveTagDO);

    }

    @Test
    @Rollback(false)
    public void deletePureLiveTag() throws Exception {

//        pureLiveTagService.deletePureLiveTag(32L);
    }

    @Test
    @Rollback(false)
    public void updatePureLiveTag() throws Exception {
        pureLiveTagDO.setDesc("hahahdsdfwefsdf");
        pureLiveTagDO.setName("麦克专属dddd");
        pureLiveTagDO.setPic("sdfeefdfghgfhdfsg");
//        pureLiveTagService.updatePureLiveTag(32L, pureLiveTagDO);

    }

    @Test
    public void getPureLiveTag() throws Exception {
        PureLiveTagDO pureLiveTag = pureLiveTagService.getPureLiveTag(7L);
        LOGGER.info("pureLiveTag==>{}", pureLiveTag);
    }

    @Test
    public void listPureLiveTags() throws Exception {
        PureLiveTagSearch pureLiveTagSearch = new PureLiveTagSearch();
        pureLiveTagSearch.setPage(1);
        pureLiveTagSearch.setLimit(10);
        List<PureLiveTagDO> pureLiveTagDOS = pureLiveTagService.listPureLiveTags(pureLiveTagSearch);
        LOGGER.info("pureLiveTagDOS==>{}", pureLiveTagDOS);

    }

    @Test
    public void countPureLiveTags() throws Exception {
        PureLiveTagSearch pureLiveTagSearch = new PureLiveTagSearch();
        pureLiveTagSearch.setPage(1);
        pureLiveTagSearch.setLimit(10);
        Integer countPureLiveTags = pureLiveTagService.countPureLiveTags(pureLiveTagSearch);
        LOGGER.info("countPureLiveTags==>{}", countPureLiveTags);

    }

    @Test
    public void listPureLiveTagsWithCount() throws Exception {
        PureLiveTagSearch pureLiveTagSearch = new PureLiveTagSearch();
        pureLiveTagSearch.setPage(1);
        pureLiveTagSearch.setLimit(10);
        BaseList<PureLiveTagDO> baseList = pureLiveTagService.listPureLiveTagsWithCount(pureLiveTagSearch);
        LOGGER.info("baseList==>{}", baseList);

    }

    @Test
    public void listPureLiveTagWithRels() throws Exception {
        PureLiveTagSearch pureLiveTagSearch = new PureLiveTagSearch();
        pureLiveTagSearch.setPage(1);
        pureLiveTagSearch.setLimit(10);
        List<PureLiveTagDO> pureLiveTagDOS = pureLiveTagService.listPureLiveTagWithRels(pureLiveTagSearch);
        LOGGER.info("pureLiveTagDOS==>{}", pureLiveTagDOS);

    }

    @Test
    @Rollback(false)
    public void addPureLiveTagRel() throws Exception {

//        pureLiveTagService.addPureLiveTagRel(pureLiveTagRelDO);
    }

    @Test
    @Rollback(false)
    public void updatePureLiveTagRel() throws Exception {
        pureLiveTagRelDO.setLid(3649L);
//        pureLiveTagService.updatePureLiveTagRel(pureLiveTagRelDO);

    }

    @Test
    @Rollback(false)
    public void deletePureLiveTagRel() throws Exception {
//        pureLiveTagService.deletePureLiveTagRel(pureLiveTagRelDO);
    }

    @Test
    public void listPureLiveTagRels() throws Exception {
        PureLiveTagRelSearch pureLiveTagRelSearch = new PureLiveTagRelSearch();
        pureLiveTagRelSearch.setPage(1);
        pureLiveTagRelSearch.setLimit(10);
        List<PureLiveTagRelDO> pureLiveTagRelDOS = pureLiveTagService.listPureLiveTagRels(pureLiveTagRelSearch);
        LOGGER.info("pureLiveTagRelDOS==>{}", pureLiveTagRelDOS);

    }

    @Test
    public void countPureLiveTagRels() throws Exception {
        PureLiveTagRelSearch pureLiveTagRelSearch = new PureLiveTagRelSearch();
        pureLiveTagRelSearch.setPage(1);
        pureLiveTagRelSearch.setLimit(10);
        Integer countPureLiveTagRels = pureLiveTagService.countPureLiveTagRels(pureLiveTagRelSearch);
        LOGGER.info("countPureLiveTagRels==>{}", countPureLiveTagRels);

    }

    @Test
    public void listPureLiveTagRelsWithCount() throws Exception {
        PureLiveTagSearch pureLiveTagSearch = new PureLiveTagSearch();
        pureLiveTagSearch.setPage(1);
        pureLiveTagSearch.setLimit(10);
        BaseList<PureLiveTagDO> baseList = pureLiveTagService.listPureLiveTagsWithCount(pureLiveTagSearch);
        LOGGER.info("baseList==>{}", baseList);

    }


    @Test
    public void addTemplateTagRel() throws Exception {
    }

    @Test
    public void deleteTemplateTagRel() throws Exception {
    }

    @Test
    public void updateTemplateTagRel() throws Exception {
    }

    @Test
    public void getTemplateTagRel() throws Exception {
        TemplateTagRelDO templateTagRel = pureLiveTagService.getTemplateTagRel(7L);
        LOGGER.info("templateTagRel==>{}", templateTagRel);

    }

    @Test
    public void listPureLivesByTagId() throws Exception {
        PureLiveTagRelSearch pureLiveTagRelSearch = new PureLiveTagRelSearch();
        pureLiveTagRelSearch.setPage(1);
        pureLiveTagRelSearch.setLimit(10);
        List<Long> pureLivesByTagId = pureLiveTagService.listPureLivesByTagId(7L, pureLiveTagRelSearch);
        LOGGER.info("pureLivesByTagId==>{}", pureLivesByTagId);

    }

    @Test
    public void listTemplatesByTagId() throws Exception {
        PureLiveTagRelSearch pureLiveTagRelSearch = new PureLiveTagRelSearch();
        List<Long> pureLivesByTagId = pureLiveTagService.listPureLivesByTagId(7L, pureLiveTagRelSearch);
        LOGGER.info("pureLivesByTagId==>{}", pureLivesByTagId);

    }

    @Test
    public void listTypeTagRelsiByLid() throws Exception {
        List<PureLiveTagRelDO> pureLiveTagRelDOS = pureLiveTagService.listTypeTagRelsiByLid(3649L);
        LOGGER.info("pureLiveTagRelDOS==>{}", pureLiveTagRelDOS);

    }

    @Test
    public void getColumnTagById() throws Exception {
        TemplateTagRelDO templateTagRel = pureLiveTagService.getTemplateTagRel(7L);
        LOGGER.info("templateTagRel==>{}", templateTagRel);

    }

}
