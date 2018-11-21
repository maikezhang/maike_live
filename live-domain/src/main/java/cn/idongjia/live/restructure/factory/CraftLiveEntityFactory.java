package cn.idongjia.live.restructure.factory;

import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.domain.entity.live.LiveShowResource;
import cn.idongjia.live.restructure.domain.entity.live.LiveVideoCover;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.v2.pojo.CraftsLive;
import cn.idongjia.live.v2.pojo.LiveResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author lc
 * @create at 2018/7/18.
 */
public class CraftLiveEntityFactory extends LiveAbstractFactory<CraftsLive> {
    /**
     * 获取直播
     *
     * @param craftsLive
     * @return
     */
    @Override
    public LiveEntity getEntity(CraftsLive craftsLive) {
        LiveEntity entity = new LiveEntity();
        entity.setId(craftsLive.getLid());
        entity.setHuid(craftsLive.getAnchorId());
        entity.setEstimatedStartTime(craftsLive.getPreStartTime());
        entity.setEstimatedEndTime(craftsLive.getPreEndTime());
        entity.setShowDesc(craftsLive.getShowDesc());
        entity.setZid(craftsLive.getZid());
        entity.setScreenDirection(craftsLive.getScreenDirection());
        entity.setPic(craftsLive.getPic());
        entity.setTitle(craftsLive.getTitle());
        entity.setLiveType(BaseEnum.parseInt2Enum(craftsLive.getLiveType(), LiveEnum.LiveType.values()).orElse(null));
        entity.setOnline(BaseEnum.parseInt2Enum(craftsLive.getOnline(), LiveEnum.LiveOnline.values()).orElse(null));
        entity.setAutoOnline(BaseEnum.parseInt2Enum(craftsLive.getAutoOnline(), BaseEnum.YesOrNo.values()).orElse(BaseEnum.YesOrNo.NO));

        if (!Objects.isNull(craftsLive.getResources())) {
            entity.setResource(assambleCraftsResource(craftsLive.getResources()));
        }

        if (!Objects.isNull(craftsLive.getVideoCoverUrl()) && !Objects.isNull(craftsLive.getVideoCoverPic())) {
            LiveVideoCover videoCover = new LiveVideoCover();

            videoCover.setId(craftsLive.getVideoCoverId());
            videoCover.setUrl(craftsLive.getVideoCoverUrl());
            videoCover.setPic(craftsLive.getVideoCoverPic());
            videoCover.setDuration(craftsLive.getVideoCoverDuration());
            entity.setLiveVideoCover(videoCover);
        }


        return entity;
    }

    private static List<LiveShowResource> assambleCraftsResource(List<LiveResource> liveResources) {
        List<LiveShowResource> resources = new ArrayList<>();
        liveResources.forEach(liveResource -> {
            LiveShowResource resource = new LiveShowResource();
            resource.setResourceId(liveResource.getResourceId());
            resource.setResourceType(liveResource.getResourceType());
            resources.add(resource);
        });
        return resources;
    }

}
