package cn.idongjia.live.restructure.impl.purelive;

import cn.idongjia.common.pagination.Pagination;
import cn.idongjia.live.api.purelive.PureLiveBannerService;
import cn.idongjia.live.pojo.purelive.banner.PureLiveBannerDO;
import cn.idongjia.live.query.purelive.banner.PureLiveBannerSearch;
import cn.idongjia.live.restructure.biz.LiveBannerBO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static cn.idongjia.live.restructure.exception.PureLiveException.LIVE_BANNER_PRIMARY_MISS;
import static cn.idongjia.util.Asserts.assertNotNull;

/**
 * 直播业务banner服务
 * Created by dongjia_lj on 17/3/9.
 */
@Component("pureLiveBannerService")
public class PureLiveBannerServiceImpl implements PureLiveBannerService {


    @Resource
    private LiveBannerBO liveBannerBO;

    @Override
    public void addBanner(PureLiveBannerDO pureLiveBannerDO) {
//        bannerRepo.addBanner(pureLiveBannerDO);
    }

    @Override
    public void updateBanner(Long bid, PureLiveBannerDO pureLiveBannerDO) {
        assertNotNull(bid,LIVE_BANNER_PRIMARY_MISS);
        pureLiveBannerDO.setBid(bid);
//        bannerRepo.updateBanner(pureLiveBannerDO);
    }

    @Override
    public void deleteBanner(Long bid) {
        assertNotNull(bid,LIVE_BANNER_PRIMARY_MISS);
//        bannerRepo.delete(bid);
    }

    @Override
    public Pagination queryBannerByPage(PureLiveBannerSearch search) {
        return null;
    }

    @Override
    public List<PureLiveBannerDO> queryBannerList(PureLiveBannerSearch search) {
        return null;
    }

    @Override
    public int countBanner(PureLiveBannerSearch search) {

        return 0;
    }
}
