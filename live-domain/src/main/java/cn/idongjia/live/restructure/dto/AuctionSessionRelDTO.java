package cn.idongjia.live.restructure.dto;

import cn.idongjia.consts.TokenType;
import cn.idongjia.live.api.live.pojo.LiveItemResp;
import cn.idongjia.outcry.pojo.AuctionSessionRel;
import cn.idongjia.outcry.pojo.RelAuction4Live;
import cn.idongjia.util.Utils;

import java.util.List;

/**
 * @author lc
 * @create at 2018/6/12.
 */
public class AuctionSessionRelDTO extends BaseDTO<RelAuction4Live> {
    public AuctionSessionRelDTO(RelAuction4Live entity) {
        super(entity);
    }

    public LiveItemResp assembleLiveItemResp() {
        LiveItemResp liveItemRes = new LiveItemResp();
        liveItemRes.setId(entity.getItemId());
        liveItemRes.setIdx(entity.getRelWeight());
        liveItemRes.setPrice(entity.getCurrentPrice());
        liveItemRes.setState(entity.getState());
        liveItemRes.setPic(entity.getCover());
        return liveItemRes;
    }
}
