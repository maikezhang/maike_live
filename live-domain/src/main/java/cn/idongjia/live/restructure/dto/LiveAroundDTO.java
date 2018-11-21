package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.restructure.domain.entity.user.LiveAnchor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lc
 * @create at 2018/7/17.
 */
@Getter
@Setter
public class LiveAroundDTO {
    private LiveShowDTO liveShowDTO;
    private LiveAnchor liveAnchor;
}
