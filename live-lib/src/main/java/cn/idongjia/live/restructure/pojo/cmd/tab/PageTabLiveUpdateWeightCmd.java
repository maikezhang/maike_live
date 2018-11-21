package cn.idongjia.live.restructure.pojo.cmd.tab;

import cn.idongjia.live.restructure.pojo.Command;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * tab关联直播
 *
 * @author lc
 * @create at 2018/7/8.
 */
@Getter
@Setter
@NoArgsConstructor

public class PageTabLiveUpdateWeightCmd extends Command {
    /**
     * id
     */
    private Long id;
    /**
     * 权重
     */
    private int weight;
}
