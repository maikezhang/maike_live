package cn.idongjia.live.restructure.dto;

import cn.idongjia.outcry.pojo.AuctionSession;
import cn.idongjia.outcry.pojo.Session4Live;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author lc
 * @create at 2018/7/17.
 */
@Getter
@Setter
public class CraftsLiveDTO {
    private LiveShowDTO liveShowDTO;
    private LivePureDTO pureLiveDTO;
    private VideoCoverDTO videoCoverDTO;
    private List<LiveResourceDTO> resourceDTOS;
    private String templateJson;
    private Session4Live auctionSession;

    private String liveTypeName;
}
