package cn.idongjia.live.restructure.repo;

import cn.idongjia.live.db.mybatis.mapper.LiveBannerMapper;
import cn.idongjia.live.db.mybatis.po.LiveBannerPO;
import cn.idongjia.live.db.mybatis.query.DBLiveBannerQuery;
import cn.idongjia.live.restructure.dto.LiveBannerDTO;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.restructure.manager.ZooManager;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * banner业务逻辑处理
 * Created by dongjia_lj on 17/3/10.
 */
@Repository
public class LiveBannerRepo {

    @Resource
    private LiveBannerMapper bannerMapper;


    public Long addBanner(LiveBannerDTO liveBannerDTO) {
        bannerMapper.insert(liveBannerDTO.toDO());
        return liveBannerDTO.getId();
    }

    public void updateBanner(LiveBannerDTO liveBannerDTO) {
        bannerMapper.update(liveBannerDTO.toDO());
    }

    public List<LiveBannerDTO> list(DBLiveBannerQuery dbLiveBannerQuery) {
        List<LiveBannerPO> liveBannerPOS = bannerMapper.list(dbLiveBannerQuery);
        return liveBannerPOS.stream().map(LiveBannerDTO::new).collect(Collectors.toList());
    }


    public List<LiveBannerDTO> getBanner(DBLiveBannerQuery dbLiveBannerQuery) {
        List<LiveBannerPO> banners = bannerMapper.list(dbLiveBannerQuery);
        return banners.stream().map(LiveBannerDTO::new).collect(Collectors.toList());
    }

    public boolean delete(Long bid) {
        if (null != bid) {
            LiveBannerPO liveBannerPO = new LiveBannerPO();
            liveBannerPO.setId(bid);
            liveBannerPO.setStatus(LiveConst.STATUS_BANNER_DEL);
            liveBannerPO.setUpdateTime(Utils.getCurrentMillis());
           return bannerMapper.update(liveBannerPO)>0;
        }
        return false;
    }


    public Integer count(DBLiveBannerQuery dbLiveBannerQuery) {
        return bannerMapper.count(dbLiveBannerQuery);
    }
}
