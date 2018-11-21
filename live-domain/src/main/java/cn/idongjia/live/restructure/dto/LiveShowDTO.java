package cn.idongjia.live.restructure.dto;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.live.db.mybatis.po.LiveShowPO;
import cn.idongjia.live.pojo.live.LivePre;
import cn.idongjia.live.pojo.live.LivePullUrl;
import cn.idongjia.live.pojo.live.LiveShow;
import cn.idongjia.live.restructure.domain.entity.zoo.LiveZoo;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.support.DateTimeUtil;
import cn.idongjia.live.v2.pojo.CraftsLive4List;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.outcry.pojo.AuctionSession;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

/**
 * @author lc
 * @create at 2018/6/7.
 */
public class LiveShowDTO extends BaseDTO<LiveShowPO> {

    private static final Log LOGGER = LogFactory.getLog(LiveShowDTO.class);

    public LiveShowDTO(LiveShowPO liveShowPO) {
        super(liveShowPO);
    }


    public void buildFromReq(LiveShow liveShow) {
        entity = new LiveShowPO();
        entity.setModifiedTime(liveShow.getModifiedTm());
        entity.setAutoOnline(liveShow.getAutoOnline());
        entity.setCreateTime(liveShow.getCreateTm());
        entity.setEndTime(liveShow.getEndTm());
        entity.setPreviewTime(liveShow.getPreViewTm());
        entity.setEstimatedStartTime(liveShow.getPreStartTm());
        entity.setEstimatedEndTime(liveShow.getPreEndTm());
        entity.setStartTime(liveShow.getStartTm());
        entity.setGeneralWeight(liveShow.getGeneralWeight());
        entity.setId(liveShow.getId());
        entity.setStatus(liveShow.getLiveStatus());
        entity.setOnline(liveShow.getOnline());
        entity.setRoomId(liveShow.getRoomId());
        entity.setScreenDirection(liveShow.getScreenDirection());
        entity.setShowDesc(liveShow.getShowDesc());
        entity.setState(liveShow.getState());
        entity.setTitle(liveShow.getTitle());
        entity.setType(liveShow.getType());
        entity.setUserId(liveShow.getUid());
        entity.setVideoCoverId(liveShow.getVideoCoverId());
        entity.setZooId(liveShow.getZid());

    }

    public LiveShow toLiveShow(LiveZoo liveZoo, LivePullUrl livePullUrl, int cloudType) {
        LiveShow liveShow = new LiveShow();
        if (entity == null || liveZoo == null) {
            return null;
        }
        // 组装PureLive数据
        liveShow.setId(entity.getId());
        liveShow.setTitle(entity.getTitle());
        liveShow.setType(entity.getType());
        liveShow.setState(entity.getState());
        liveShow.setPreViewTm(entity.getPreviewTime());
        liveShow.setPreStartTm(entity.getEstimatedStartTime());
        liveShow.setPreEndTm(entity.getEstimatedEndTime());
        liveShow.setEndTm(entity.getEndTime());
        liveShow.setCreateTm(entity.getCreateTime());
        liveShow.setModifiedTm(entity.getModifiedTime());
        liveShow.setUid(entity.getUserId());
        liveShow.setRoomId(entity.getRoomId());
        liveShow.setScreenDirection(entity.getScreenDirection());
        liveShow.setOnline(entity.getOnline());
        liveShow.setCloudType(cloudType);
        if (liveZoo != null) {
            liveShow.setZid(liveZoo.getZid());
            liveShow.setZrc(liveZoo.getZrc());
            liveShow.setSuid(liveZoo.getUid());
        }
        if (livePullUrl != null) {
            liveShow.setRtmpUrl(livePullUrl.getRtmpUrl());
            liveShow.setHlsUrl(livePullUrl.getHlsUrl());
            liveShow.setFlvUrl(livePullUrl.getFlvUrl());
        }
        liveShow.setGeneralWeight(entity.getGeneralWeight());
        liveShow.setOnline(entity.getOnline());
        liveShow.setShowDesc(entity.getShowDesc());
        liveShow.setAutoOnline(entity.getAutoOnline());
        return liveShow;
    }

    public CraftsLive4List toCraftsLiveList(LivePureDTO pureLiveDTO, int debugMinutes, AuctionSession auctionSession) {
        CraftsLive4List craftsLive4List = new CraftsLive4List();
        craftsLive4List.setOnline(entity.getOnline());
        craftsLive4List.setLid(entity.getId());
        craftsLive4List.setPic(pureLiveDTO != null ? pureLiveDTO.getPic() : auctionSession != null ? auctionSession.getPic() : null);
        craftsLive4List.setTitle(entity.getTitle());
        craftsLive4List.setCreateTime(entity.getCreateTime().getTime());
        craftsLive4List.setPreEndTime(entity.getEstimatedEndTime().getTime());
        craftsLive4List.setPreStartTime(entity.getEstimatedStartTime().getTime());
        if (auctionSession != null) {
            craftsLive4List.setAsid(auctionSession.getAsid());
        }
        //LiveEntity主要是从索引中获取，其他需要从数据库获取的属性暂时不放在LiveEntity的convert中
        try {
            craftsLive4List.setScreenDirection(entity.getScreenDirection());
            craftsLive4List.setZid(entity.getZooId());
        } catch (Exception e) {
            LiveShowDTO.LOGGER.warn(e.getMessage());
        }
        if (LiveEnum.LiveType.LIVE_AUCTION.getCode() == entity.getType().intValue()) {
            craftsLive4List.setIsAuction(LiveEnum.IsAuction.IS_AUCTION.getCode());
        } else {
            craftsLive4List.setIsAuction(LiveEnum.IsAuction.NOT_AUCTION.getCode());
        }
        craftsLive4List.setStatus(entity.getStatus());
        if (entity.getEstimatedStartTime().getTime() - System.currentTimeMillis() >
                TimeUnit.MINUTES.toMillis(debugMinutes)) {
            craftsLive4List.setIsDebug(LiveEnum.Debug.IS_DEBUG.getCode());
        } else {
            craftsLive4List.setIsDebug(LiveEnum.Debug.NOT_DEBUG.getCode());
        }
        return craftsLive4List;
    }


    public Long getRoomId() {
        return entity.getRoomId();
    }

    public String getTitle() {
        return entity.getTitle();
    }

    public Long getEstimatedStartTime() {
        return entity.getEstimatedStartTime() == null ? null : entity.getEstimatedStartTime().getTime();
    }

    public Long getZooId() {
        return entity.getZooId();
    }

    public Long getPreviewTime() {
        return entity.getPreviewTime() == null ? null : entity.getPreviewTime().getTime();
    }

    public Long getEstimatedEndTime() {
        return entity.getEstimatedEndTime() == null ? null : entity.getEstimatedEndTime().getTime();

    }

    public Long getUserId() {
        return entity.getUserId();
    }

    public Integer getState() {
        return entity.getState();
    }

    public void setState(int stateLiveInProgress) {
        entity.setState(stateLiveInProgress);
    }

    public Long getCreateTime() {
        return assembleTime(entity.getCreateTime());
    }

    public Long getId() {
        return entity.getId();
    }

    public void setId(Long id) {
        entity.setId(id);
    }

    public Integer getType() {
        return entity.getType();
    }

    public Long getStartTime() {
        return assembleTime(entity.getStartTime());
    }

    public void setStartTime(Timestamp timestamp) {
        entity.setStartTime(timestamp);
    }

    public Long getEndTime() {
        return assembleTime(entity.getEndTime());
    }

    public void setEndTime(Timestamp timestamp) {
        entity.setEndTime(timestamp);
    }

    public Long getModifiedTime() {
        return assembleTime(entity.getModifiedTime());
    }

    public void setModifiedTime(Timestamp timestamp) {
        entity.setModifiedTime(timestamp);
    }

    public Integer getLiveStatus() {
        return entity.getStatus();
    }


    public Integer getGeneralWeight() {
        return entity.getGeneralWeight();
    }

    public Integer getOnline() {
        return entity.getOnline();
    }

    public void setOnline(int online) {
        entity.setOnline(online);
    }

    public String getShowDesc() {
        return entity.getShowDesc();
    }

    public Integer getScreenDirection() {
        return entity.getScreenDirection();
    }

    public Integer getAutoOnline() {
        return entity.getAutoOnline();
    }





    public LivePre liveShow2LivePre(User user,Long asid,Boolean subscribed){
        LivePre livePre =new LivePre();
        livePre.setAsid(asid);
        livePre.setDate(DateTimeUtil.getLong2Date(entity.getEstimatedStartTime().getTime()));
        livePre.setTime(DateTimeUtil.getLong2Time(entity.getEstimatedStartTime().getTime()));
        livePre.setHavatar(user.getAvatar());
        livePre.setHtitle(user.getTitle());
        livePre.setPreStartTime(entity.getEstimatedStartTime().getTime());
        livePre.setTitle(entity.getTitle());
        livePre.setSubscribed(subscribed);
        livePre.setLiveId(entity.getId());
        return livePre;

    }


    public Integer getStatus() {
        return entity.getStatus();
    }
}
