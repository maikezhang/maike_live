package cn.idongjia.live.restructure.pojo.cmd.tab;

import cn.idongjia.live.restructure.pojo.Command;
import cn.idongjia.live.restructure.pojo.co.tab.PageTabCO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author lc
 * @create at 2018/7/7.
 */
@Getter
@Setter
@NoArgsConstructor
public class PageTabAddCmd extends Command {
    private PageTabCO pageTabCO;
}
