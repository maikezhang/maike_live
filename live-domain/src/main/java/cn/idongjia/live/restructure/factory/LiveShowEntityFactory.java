package cn.idongjia.live.restructure.factory;


import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.live.LiveShow;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.domain.entity.live.LiveVideoCover;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.support.BaseEnum;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

/**
 * @author lc
 * @create at 2018/7/17.
 */
public class LiveShowEntityFactory extends LiveAbstractFactory<LiveShow> {
    /**
     * 获取直播
     *
     * @param liveShow
     * @return
     */
    @Override
    public LiveEntity getEntity(LiveShow liveShow) {
        if (Objects.isNull(liveShow)) {
            throw LiveException.failure("获取直播失败");
        }
        LiveEntity entity = new LiveEntity();
        entity.setLiveType(BaseEnum.parseInt2Enum(liveShow.getType(), LiveEnum.LiveType.values())
                .orElse(LiveEnum.LiveType.LIVE_AUCTION));
        entity.setTitle(liveShow.getTitle());
        Timestamp preViewTm = liveShow.getPreViewTm();
        entity.setPreviewTime(preViewTm == null ? null : preViewTm.getTime());
        Timestamp preStartTm = liveShow.getPreStartTm();
        entity.setEstimatedStartTime(preStartTm == null ? null : preStartTm.getTime());
        Timestamp preEndTm = liveShow.getPreEndTm();
        entity.setEstimatedEndTime(preEndTm == null ? null : preEndTm.getTime());
        entity.setScreenDirection(liveShow.getScreenDirection());
        entity.setOnline(BaseEnum.parseInt2Enum(liveShow.getOnline(), LiveEnum.LiveOnline.values()).orElse(null));
        entity.setAutoOnline(BaseEnum.parseInt2Enum(liveShow.getAutoOnline(), BaseEnum.YesOrNo.values()).orElse(BaseEnum.YesOrNo.NO));
        entity.setShowDesc(liveShow.getShowDesc());
        entity.setHuid(liveShow.getUid());
        entity.setZid(liveShow.getZid());
        if(Objects.nonNull(liveShow.getVideoCoverId())){
            LiveVideoCover cover=new LiveVideoCover();
            cover.setId(liveShow.getVideoCoverId());
            entity.setLiveVideoCover(cover);
        }
        return entity;
    }
}
