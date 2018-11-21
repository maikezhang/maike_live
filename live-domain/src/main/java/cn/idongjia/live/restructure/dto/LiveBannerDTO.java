package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.db.mybatis.po.LiveBannerPO;
import cn.idongjia.live.pojo.homePage.LiveBannerCO;
import cn.idongjia.live.pojo.live.LiveBanner;
import cn.idongjia.util.Utils;

/**
 * @author lc
 * @create at 2018/6/13.
 */
public class LiveBannerDTO extends BaseDTO<LiveBannerPO> {
    public LiveBannerDTO(LiveBannerPO entity) {
        super(entity);
    }

    public Long getId() {
        return entity.getId();
    }

    public Long getClassificationId() {
        return entity.getClassificationId();
    }

    public void buildFromReq(LiveBanner liveBanner) {
        entity = new LiveBannerPO();
        entity.setId(liveBanner.getId());
        entity.setClassificationId(liveBanner.getClassificationId());
        entity.setCreateTime(Utils.getCurrentMillis());
        entity.setUpdateTime(Utils.getCurrentMillis());
        entity.setJumpAddr(liveBanner.getJumpAddr());
        entity.setJumpType(liveBanner.getJumpType());
        entity.setPic(liveBanner.getPic());
        entity.setStatus(liveBanner.getStatus());
        entity.setWeight(liveBanner.getWeight());
        entity.setNewVersionPic(liveBanner.getNewVersionPic());
    }

    public LiveBanner po2LiveBanner(LiveBannerPO liveBannerPO) {
        LiveBanner liveBanner = new LiveBanner();
        liveBanner.setClassificationId(liveBannerPO.getClassificationId());
        liveBanner.setId(liveBannerPO.getId());
        liveBanner.setJumpAddr(liveBannerPO.getJumpAddr());
        liveBanner.setJumpType(liveBannerPO.getJumpType());
        liveBanner.setPic(liveBannerPO.getPic());
        liveBanner.setStatus(liveBannerPO.getStatus());
        liveBanner.setWeight(liveBannerPO.getWeight());
        liveBanner.setNewVersionPic(liveBannerPO.getNewVersionPic());
        return liveBanner;
    }

    public LiveBannerCO po2LiveBannerCO(LiveBannerPO liveBannerPO,boolean isNewVersion){
        LiveBannerCO liveBanner = new LiveBannerCO();
        liveBanner.setClassificationId(liveBannerPO.getClassificationId());
        liveBanner.setBid(liveBannerPO.getId());
        liveBanner.setAddr(liveBannerPO.getJumpAddr());
        liveBanner.setType(liveBannerPO.getJumpType());
        if (isNewVersion){
            liveBanner.setCover(liveBannerPO.getNewVersionPic());
        }else {
            liveBanner.setCover(liveBannerPO.getPic());
        }
        liveBanner.setStatus(liveBannerPO.getStatus());
        liveBanner.setWeight(liveBannerPO.getWeight());
        return liveBanner;

    }
}

