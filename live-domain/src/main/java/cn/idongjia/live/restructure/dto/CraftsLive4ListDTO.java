package cn.idongjia.live.restructure.dto;

import cn.idongjia.outcry.pojo.AuctionSession;
import cn.idongjia.outcry.pojo.Session4Live;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lc
 * @create at 2018/7/17.
 */
@Getter
@Setter
public class CraftsLive4ListDTO {
    private LiveShowDTO liveShowDTO;
    private LivePureDTO pureLiveDTO;
    private int debugMinutes;
    private Session4Live auctionSession;
}
