package cn.idongjia.live.restructure.dto;

import cn.idongjia.divine.lib.pojo.response.live.general.GeneralLiveCO;
import cn.idongjia.live.restructure.domain.entity.zoo.LiveZoo;
import cn.idongjia.outcry.pojo.Session4Live;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lc
 * @create at 2018/7/17.
 */
@Getter
@Setter
public class LiveMPListDTO {

    private GeneralLiveCO generalLiveCO;

    private Boolean isLike;

    private LivePullUrlDTO livePullUrlDTO;

    private String templateJson;

    private String shareDesc;

    private LiveZoo liveZoo;

//    private LiveShowDTO liveShowDTO;


}
