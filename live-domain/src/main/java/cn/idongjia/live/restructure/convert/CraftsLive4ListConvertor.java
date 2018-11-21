package cn.idongjia.live.restructure.convert;

import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.dto.CraftsLive4ListDTO;
import cn.idongjia.live.restructure.dto.LivePureDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.v2.pojo.CraftsLive4List;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.outcry.pojo.AuctionSession;
import cn.idongjia.outcry.pojo.Session4Live;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author lc
 * @create at 2018/7/17.
 */
@Component("craftsLive4ListConvertor")
public class CraftsLive4ListConvertor implements ConvertorI<CraftsLive4List, LiveEntity, CraftsLive4ListDTO> {
    private static final Log logger = LogFactory.getLog(CraftsLive4ListConvertor.class);


    @Override
    public CraftsLive4List dataToClient(CraftsLive4ListDTO craftsLive4ListDTO) {
        Session4Live auctionSession = craftsLive4ListDTO.getAuctionSession();
        int debugMinutes = craftsLive4ListDTO.getDebugMinutes();
        LiveShowDTO liveShowDTO = craftsLive4ListDTO.getLiveShowDTO();
        LivePureDTO pureLiveDTO = craftsLive4ListDTO.getPureLiveDTO();
        CraftsLive4List craftsLive4List = new CraftsLive4List();
        craftsLive4List.setOnline(liveShowDTO.getOnline());
        craftsLive4List.setLid(liveShowDTO.getId());
        craftsLive4List.setPic(pureLiveDTO != null ? pureLiveDTO.getPic() : auctionSession != null ? auctionSession.getPic() : null);
        craftsLive4List.setTitle(liveShowDTO.getTitle());
        craftsLive4List.setCreateTime(liveShowDTO.getCreateTime());
        craftsLive4List.setPreEndTime(liveShowDTO.getEstimatedEndTime());
        craftsLive4List.setPreStartTime(liveShowDTO.getEstimatedStartTime());
        if (auctionSession != null) {
            craftsLive4List.setAsid(auctionSession.getId());
        }
        //LiveEntity主要是从索引中获取，其他需要从数据库获取的属性暂时不放在LiveEntity的convert中
        try {
            craftsLive4List.setScreenDirection(liveShowDTO.getScreenDirection());
            craftsLive4List.setZid(liveShowDTO.getZooId());
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        if (LiveEnum.LiveType.LIVE_AUCTION.getCode() == liveShowDTO.getType().intValue()) {
            craftsLive4List.setIsAuction(LiveEnum.IsAuction.IS_AUCTION.getCode());
        } else {
            craftsLive4List.setIsAuction(LiveEnum.IsAuction.NOT_AUCTION.getCode());
        }
        craftsLive4List.setStatus(liveShowDTO.getStatus());
        if (liveShowDTO.getEstimatedStartTime() - System.currentTimeMillis() >
                TimeUnit.MINUTES.toMillis(debugMinutes)) {
            craftsLive4List.setIsDebug(LiveEnum.Debug.IS_DEBUG.getCode());
        } else {
            craftsLive4List.setIsDebug(LiveEnum.Debug.NOT_DEBUG.getCode());
        }
        return craftsLive4List;
    }
}
