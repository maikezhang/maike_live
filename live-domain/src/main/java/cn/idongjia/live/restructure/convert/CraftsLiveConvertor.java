package cn.idongjia.live.restructure.convert;

import cn.idongjia.live.db.mybatis.po.LiveShowPO;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.dto.CraftsLiveDTO;
import cn.idongjia.live.restructure.dto.LivePureDTO;
import cn.idongjia.live.restructure.dto.LiveResourceDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.dto.VideoCoverDTO;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.live.v2.pojo.CraftsLive;
import cn.idongjia.live.v2.pojo.LiveResource;
import cn.idongjia.outcry.pojo.AuctionSession;
import cn.idongjia.outcry.pojo.Session4Live;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/7/17.
 */
@Component("craftsLiveConvertor")
public class CraftsLiveConvertor implements ConvertorI<CraftsLive, LiveEntity, CraftsLiveDTO> {

    @Override
    public CraftsLive dataToClient(CraftsLiveDTO craftsLiveDTO) {
        Session4Live auctionSession = craftsLiveDTO.getAuctionSession();
        LiveShowDTO liveShowDTO = craftsLiveDTO.getLiveShowDTO();
        LivePureDTO pureLiveDTO = craftsLiveDTO.getPureLiveDTO();
        List<LiveResourceDTO> resourceDTOS = craftsLiveDTO.getResourceDTOS();
        String templateJson = craftsLiveDTO.getTemplateJson();
        VideoCoverDTO videoCoverDTO = craftsLiveDTO.getVideoCoverDTO();
        CraftsLive craftsLive = new CraftsLive();
        craftsLive.setLid(liveShowDTO.getId());
        craftsLive.setAnchorId(liveShowDTO.getUserId());
        craftsLive.setPic(pureLiveDTO != null ? pureLiveDTO.getPic() : auctionSession == null ? null : auctionSession.getPic());
        if (auctionSession != null) {
            craftsLive.setAsid(auctionSession.getId());
        }
        craftsLive.setTitle(liveShowDTO.getTitle());
        craftsLive.setShowDesc(liveShowDTO.getShowDesc());
        craftsLive.setPreStartTime(liveShowDTO.getPreviewTime());
        craftsLive.setPreEndTime(liveShowDTO.getEstimatedEndTime());
        craftsLive.setAutoOnline(liveShowDTO.getAutoOnline());
        craftsLive.setScreenDirection(liveShowDTO.getScreenDirection());
        if (Objects.nonNull(videoCoverDTO)) {
            craftsLive.setVideoCoverId(videoCoverDTO.getId());
            craftsLive.setVideoCoverUrl(videoCoverDTO.getUrl());
            craftsLive.setVideoCoverDuration(videoCoverDTO.getDuration());
            craftsLive.setVideoCoverPic(videoCoverDTO.getPic());
        }
        craftsLive.setTemplateJsonStr(templateJson);
        craftsLive.setOnline(liveShowDTO.getOnline());
        List<LiveResource> liveResources = resourceDTOS.stream().map(liveResourceDTO -> {
            return liveResourceDTO.toLiveResource(liveShowDTO.getTitle());
        }).collect(Collectors.toList());
        craftsLive.setResources(liveResources);
        if (liveShowDTO.getType() == LiveConst.TYPE_LIVE_AUCTION) {
            craftsLive.setIsAuction(LiveEnum.IsAuction.IS_AUCTION.getCode());
        } else {
            craftsLive.setIsAuction(LiveEnum.IsAuction.NOT_AUCTION.getCode());
        }
        craftsLive.setOnline(liveShowDTO.getOnline());
        craftsLive.setLiveType(liveShowDTO.getType());
        craftsLive.setLiveTypeName(craftsLiveDTO.getLiveTypeName());
        return craftsLive;
    }


}
