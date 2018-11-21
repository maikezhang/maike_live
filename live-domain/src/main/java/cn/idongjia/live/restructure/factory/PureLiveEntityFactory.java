package cn.idongjia.live.restructure.factory;

import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.purelive.PureLive;
import cn.idongjia.live.pojo.purelive.PureLiveDetailDO;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.domain.entity.live.LiveShowResource;
import cn.idongjia.live.restructure.domain.entity.live.LiveVideoCover;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.enumeration.LivePlayType;
import cn.idongjia.live.support.enumeration.LiveStatus;
import com.alibaba.dubbo.common.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author lc
 * @create at 2018/7/18.
 */
public class PureLiveEntityFactory extends LiveAbstractFactory<PureLive> {
    /**
     * 获取直播
     *
     * @param pureLive
     * @return
     */
    @Override
    public LiveEntity getEntity(PureLive pureLive) {
        if (Objects.isNull(pureLive)) {
            throw LiveException.failure("获取直播失败");
        }

        LiveEntity entity = new LiveEntity();

        entity.setOnline(BaseEnum.parseInt2Enum(pureLive.getOnline(), LiveEnum.LiveOnline.values()).orElse(null));
        entity.setPreviewTime(pureLive.getPreviewtm());
        entity.setLiveType(BaseEnum.parseInt2Enum(pureLive.getLiveType(), LiveEnum.LiveType.values()).orElse(null));
        entity.setDesc(pureLive.getDesc());
        entity.setShowDesc(pureLive.getShowDesc());
        entity.setCloudType(pureLive.getCloudType());
        entity.setHuid(pureLive.getHuid());
        entity.setTitle(pureLive.getTitle());
        entity.setEstimatedEndTime(pureLive.getPreendtm());
        entity.setEstimatedStartTime(pureLive.getPrestarttm());
        entity.setDetail(pureLive.getDetail());
        entity.setLivePlayType(BaseEnum.parseInt2Enum(pureLive.getLiveType(), LivePlayType.values()).orElse(null));
        entity.setPic(pureLive.getPic());
        entity.setScreenDirection(pureLive.getScreenDirection());
        entity.setState(BaseEnum.parseInt2Enum(pureLive.getState(), LiveEnum.LiveState.values()).orElse(null));
        entity.setStatus(BaseEnum.parseInt2Enum(pureLive.getStatus(), LiveStatus.values()).orElse(null));
        entity.setUrl(pureLive.getUrl());
        entity.setWeight(pureLive.getWeight());
        entity.setGeneralWeight(pureLive.getWeight());
        entity.setAutoOnline(BaseEnum.parseInt2Enum(pureLive.getAutoOnline(), BaseEnum.YesOrNo.values()).orElse(null));
        if (!CollectionUtils.isEmpty(pureLive.getDetails())) {
            entity.setResource(assambleResource(pureLive.getDetails()));
        }
        entity.setZid(pureLive.getZid());
        if (!Objects.isNull(pureLive.getVideoCoverUrl()) && !Objects.isNull(pureLive.getVideoCoverPic())) {
            LiveVideoCover liveVideoCover = new LiveVideoCover();

            liveVideoCover.setId(pureLive.getVideoCoverId());
            liveVideoCover.setUrl(pureLive.getVideoCoverUrl());
            liveVideoCover.setPic(pureLive.getVideoCoverPic());
            liveVideoCover.setDuration(pureLive.getVideoCoverDuration());
            entity.setLiveVideoCover(liveVideoCover);
        }


        return entity;
    }

    private static List<LiveShowResource> assambleResource(List<PureLiveDetailDO> detailDOS) {
        List<LiveShowResource> resources = new ArrayList<>();
        detailDOS.forEach(detail -> {
            LiveShowResource resource = new LiveShowResource();
            resource.setResourceId(detail.getResourceId());
            resource.setResourceType(detail.getResourceType());
            resource.setWeight(detail.getWeight() == null ? null : detail.getWeight());
            resources.add(resource);

        });
        return resources;
    }
}
