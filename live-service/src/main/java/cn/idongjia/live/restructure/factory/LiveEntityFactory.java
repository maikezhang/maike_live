package cn.idongjia.live.restructure.factory;


import cn.idongjia.live.pojo.live.LiveShow;
import cn.idongjia.live.pojo.purelive.PureLive;
import cn.idongjia.live.pojo.purelive.PureLiveDetailDO;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.domain.entity.live.LiveShowResource;
import cn.idongjia.live.restructure.domain.entity.live.LiveVideoCover;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.live.support.enumeration.LivePlayType;
import cn.idongjia.live.support.enumeration.LiveStatus;
import cn.idongjia.live.v2.pojo.CraftsLive;
import cn.idongjia.live.v2.pojo.LiveResource;
import com.alibaba.dubbo.common.utils.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Description: 直播实体工厂 对外部数据的基本组装
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/8
 * Time: 上午11:22
 */
public class LiveEntityFactory {

    private LiveEntityFactory(){

    }

    public static Optional<LiveEntity> getInstance(LiveShow liveShow){
        return liveShow2LiveEntity(liveShow);

    }


    public static Optional<LiveEntity> getInstance(PureLive pureLive){
        return pureLive2LiveEntity(pureLive);
    }

    public static Optional<LiveEntity> getInstance(CraftsLive craftsLive){
        return craftLive2LiveEntity(craftsLive);
    }



    private static Optional<LiveEntity> liveShow2LiveEntity(LiveShow liveShow){
        if(Objects.isNull(liveShow)){
            return Optional.empty();
        }
        LiveEntity entity=new LiveEntity();
        entity.setLiveType(BaseEnum.parseInt2Enum(liveShow.getType(), LiveEnum.LiveType.values())
                .orElse(LiveEnum.LiveType.LIVE_AUCTION));
        entity.setTitle(liveShow.getTitle());
        Timestamp preViewTm = liveShow.getPreViewTm();
        entity.setPreviewTime(preViewTm==null?null:preViewTm.getTime());
        Timestamp preStartTm = liveShow.getPreStartTm();
        entity.setEstimatedStartTime(preStartTm==null?null:preStartTm.getTime());
        Timestamp preEndTm = liveShow.getPreEndTm();
        entity.setEstimatedEndTime(preEndTm==null?null:preEndTm.getTime());
        entity.setScreenDirection(liveShow.getScreenDirection());
        entity.setOnline(BaseEnum.parseInt2Enum(liveShow.getOnline(), LiveEnum.LiveOnline.values()).orElse(null));
        entity.setAutoOnline(BaseEnum.parseInt2Enum(liveShow.getAutoOnline(),BaseEnum.YesOrNo.values()).orElse(BaseEnum.YesOrNo.NO));
        entity.setShowDesc(liveShow.getShowDesc());
        entity.setHuid(liveShow.getUid());
        entity.setZid(liveShow.getZid());
        return Optional.of(entity);
    }

    private static Optional<LiveEntity> pureLive2LiveEntity(PureLive pureLive){
        if(Objects.isNull(pureLive)){
            return Optional.empty();
        }

        LiveEntity entity=new LiveEntity();

        entity.setOnline(BaseEnum.parseInt2Enum(pureLive.getOnline(),LiveEnum.LiveOnline.values()).orElse(null));
        entity.setPreviewTime(pureLive.getPreviewtm());
        entity.setLiveType(BaseEnum.parseInt2Enum(pureLive.getType(),LiveEnum.LiveType.values()).orElse(LiveEnum.LiveType.PURE_LIVE));
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
        entity.setState(BaseEnum.parseInt2Enum(pureLive.getState(),LiveEnum.LiveState.values()).orElse(null));
        entity.setStatus(BaseEnum.parseInt2Enum(pureLive.getStatus(),LiveStatus.values()).orElse(null));
        entity.setUrl(pureLive.getUrl());
        entity.setWeight(pureLive.getWeight());
        entity.setGeneralWeight(pureLive.getWeight());
        entity.setAutoOnline(BaseEnum.parseInt2Enum(pureLive.getAutoOnline(),BaseEnum.YesOrNo.values()).orElse(null));
        if(!CollectionUtils.isEmpty(pureLive.getDetails())){
            entity.setResource(assambleResource(pureLive.getDetails()));
        }
        entity.setZid(pureLive.getZid());
        if(Objects.nonNull(pureLive.getVideoCoverUrl())&& Objects.nonNull(pureLive.getVideoCoverPic())){
            LiveVideoCover liveVideoCover=new LiveVideoCover();
            liveVideoCover.setDuration(pureLive.getVideoCoverDuration());
            liveVideoCover.setPic(pureLive.getVideoCoverPic());
            liveVideoCover.setId(pureLive.getVideoCoverId());
            liveVideoCover.setUrl(pureLive.getVideoCoverUrl());
            entity.setLiveVideoCover(liveVideoCover);
        }
        return Optional.of(entity);
    }

    private static List<LiveShowResource> assambleResource(List<PureLiveDetailDO> detailDOS){
        List<LiveShowResource> resources=new ArrayList<>();
        detailDOS.forEach(detail -> {
            LiveShowResource resource=new LiveShowResource();
            resource.setResourceId(detail.getResourceId());
            resource.setResourceType(detail.getResourceType());
            resource.setWeight(detail.getWeight()==null?null:detail.getWeight());
            resources.add(resource);

        });

        return resources;
    }


    private static  Optional<LiveEntity> craftLive2LiveEntity(CraftsLive craftsLive){
        LiveEntity entity=new LiveEntity();
        entity.setId(craftsLive.getLid());
        entity.setHuid(craftsLive.getAnchorId());
        entity.setEstimatedStartTime(craftsLive.getPreStartTime());
        entity.setEstimatedEndTime(craftsLive.getPreEndTime());
        entity.setShowDesc(craftsLive.getShowDesc());
        entity.setZid(craftsLive.getZid());
        entity.setScreenDirection(craftsLive.getScreenDirection());
        entity.setPic(craftsLive.getPic());
        entity.setTitle(craftsLive.getTitle());
        entity.setLiveType(LiveEnum.LiveType.PURE_LIVE);
        entity.setOnline(BaseEnum.parseInt2Enum(craftsLive.getOnline(),LiveEnum.LiveOnline.values()).orElse(null));
        entity.setAutoOnline(BaseEnum.parseInt2Enum(craftsLive.getAutoOnline(),BaseEnum.YesOrNo.values()).orElse(BaseEnum.YesOrNo.NO));

        if(!Objects.isNull(craftsLive.getResources())){
            entity.setResource(assambleCraftsResource(craftsLive.getResources()));
        }

        if(!Objects.isNull(craftsLive.getVideoCoverUrl())&&!Objects.isNull(craftsLive.getVideoCoverPic())) {
            LiveVideoCover videoCover = new LiveVideoCover();

            videoCover.setId(craftsLive.getVideoCoverId());
            videoCover.setUrl(craftsLive.getVideoCoverUrl());
            videoCover.setPic(craftsLive.getVideoCoverPic());
            videoCover.setDuration(craftsLive.getVideoCoverDuration());
            entity.setLiveVideoCover(videoCover);
        }


        return Optional.of(entity);
    }
    private static List<LiveShowResource> assambleCraftsResource(List<LiveResource> liveResources){
        List<LiveShowResource> resources=new ArrayList<>();
        liveResources.forEach(liveResource -> {
            LiveShowResource resource=new LiveShowResource();
            resource.setResourceId(liveResource.getResourceId());
            resource.setResourceType(liveResource.getResourceType());
            resources.add(resource);
        });
        return resources;
    }


}
