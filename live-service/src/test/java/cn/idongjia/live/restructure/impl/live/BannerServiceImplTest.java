package cn.idongjia.live.restructure.impl.live;

import cn.idongjia.live.api.live.BannerService;
import cn.idongjia.live.pojo.live.LiveBanner;
import cn.idongjia.live.query.live.LiveBannerSearch;
import cn.idongjia.live.restructure.SpringJUnitNoRollbackTest;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

public class BannerServiceImplTest extends SpringJUnitNoRollbackTest{

    @Resource
    private BannerService bannerService;

    private LiveBanner liveBanner;
    private LiveBannerSearch search;

    private static final Log LOGGER= LogFactory.getLog(BannerServiceImplTest.class);


    @Before
    public void setUp() throws Exception {
        liveBanner=new LiveBanner();
        liveBanner.setWeight(10);
//        liveBanner.setPic("图片地址");
        liveBanner.setJumpType(2);
        liveBanner.setJumpAddr("跳转地址");
        liveBanner.setClassificationId(0L);
        liveBanner.setNewVersionPic("新版图片地址");
    }

    @Test
    @Rollback(false)
    public void createBanner() throws Exception {
        Long id=bannerService.createBanner(liveBanner);
        Assert.assertTrue(id !=null);
    }

    @Test
    @Rollback(false)
    public void updateBanner() throws Exception {
        liveBanner.setId(6L);
        liveBanner.setWeight(11);
        liveBanner.setNewVersionPic("新版图片地址11");
        bannerService.updateBanner(6L,liveBanner);
    }

    @Test
    public void deleteBanner() throws Exception {
        boolean result=bannerService.deleteBanner(1L);
        Assert.assertTrue(result);
    }

    @Test
    public void list() throws Exception {
        search=new LiveBannerSearch();
        search.setClassificationId(0L);
        List<LiveBanner> banners=bannerService.list(search);
        LOGGER.info(banners);
    }

    @Test
    public void count() throws Exception {
        search=new LiveBannerSearch();
        search.setClassificationId(0L);
        int count=bannerService.count(search);
    }

}