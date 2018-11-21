package cn.idongjia.live.restructure.impl.live;

import cn.idongjia.live.api.live.BannerService;
import cn.idongjia.live.pojo.live.LiveBanner;
import cn.idongjia.live.query.live.LiveBannerSearch;
import cn.idongjia.live.restructure.biz.LiveBannerBO;
import cn.idongjia.live.restructure.dto.LiveBannerDTO;
import cn.idongjia.live.restructure.query.LiveBannerQueryHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component("bannerServiceImpl")
public class BannerServiceImpl implements BannerService {

    @Resource
    LiveBannerBO liveBannerBO;

    @Resource
    LiveBannerQueryHandler liveBannerQueryHandler;

    @Override
    public Long createBanner(LiveBanner liveBanner) {

        return liveBannerBO.addLiveBanner(liveBanner);
    }

    @Override
    public void updateBanner(long bid, LiveBanner liveBanner) {

        liveBannerBO.updateLiveBanner(bid,liveBanner);
    }

    @Override
    public boolean deleteBanner(long bid) {

        return liveBannerBO.removeLiveBanner(bid);
    }

    @Override
    public List<LiveBanner> list(LiveBannerSearch search) {

        List<LiveBannerDTO> liveBannerDTOS = liveBannerQueryHandler.getBanner(search);

        return liveBannerDTOS.stream().map(liveBannerDTO -> {
            return liveBannerDTO.po2LiveBanner(liveBannerDTO.toDO());
        }).collect(Collectors.toList());
    }

    @Override
    public int count(LiveBannerSearch search) {
        return liveBannerBO.count(search);
    }
}
