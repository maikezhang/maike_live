package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.restructure.pojo.co.tab.LiveCO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author lc
 * @create at 2018/7/17.
 */
@Getter
@Setter
@ToString
public class LiveListDTO {
    private LiveCO liveCO;
    private List<PlayBackDTO> playBackDTOS;
    private Long leastDuration;
}
