package cn.idongjia.live.restructure.dto;

import cn.idongjia.divine.lib.pojo.response.auction.AuctionItemCO;
import cn.idongjia.divine.lib.pojo.response.live.general.GeneralLiveCO;
import cn.idongjia.live.api.live.pojo.LiveItemResp;
import cn.idongjia.live.pojo.live.LivePullUrl;
import cn.idongjia.live.restructure.domain.entity.zoo.LiveZoo;
import cn.idongjia.user.lib.pojo.vo.CustomerVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 直播对应tab数据dto
 *
 * @author lc
 * @create at 2018/7/9.
 */
@Getter
@Setter
public class LiveForTabDTO {
    private SearchIndexRespDTO searchIndexRespDTO;
    private CustomerVo customerVo;
    private VideoCoverDTO videoCoverDTO;
    private List<LiveItemResp> liveItemResps;
    private int bookState;
    private LivePullUrlDTO livePullUrlDTO;
    private LiveZoo liveZoo;


    //迁移龙川的索引所需要的字段
    private GeneralLiveCO generalLiveCO;

    private List<AuctionItemCO> liveAuctionItems;

}
