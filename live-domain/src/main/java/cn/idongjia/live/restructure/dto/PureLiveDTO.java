package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.pojo.live.LivePullUrl;
import cn.idongjia.live.restructure.domain.entity.zoo.LiveZoo;
import cn.idongjia.user.lib.pojo.vo.CustomerVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author lc
 * @create at 2018/7/17.
 */
@Getter
@Setter
public class PureLiveDTO {
    private LiveShowDTO liveShowDTO;
    private String shareDesc;
    private Long minDuration;
    private Long defaultTemplateId;
    private String detail;
    private LivePureDTO livePureDTO;
    private LiveRoomDTO liveRoomDTO;
    private VideoCoverDTO videoCoverDTO;
    private LiveZoo liveZoo;
    private LivePullUrlDTO livePullUrlDTO;
    private CustomerVo customerVo;
    private List<LiveResourceDTO> liveResourceDTOS;
    private TimeStrategyDTO timeStrategyDTO;
    private LiveTagDTO pureLiveTagRelDTO;
    private ItemPExtDTO mainItem;
    private List<PlayBackDTO> playBackDTOS;
    private boolean isBook;
    private String h5Prefix;
    private String h5Suffix;
    private boolean real;
    private Integer liveHot;
}
