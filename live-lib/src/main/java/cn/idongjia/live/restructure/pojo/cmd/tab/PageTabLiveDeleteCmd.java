package cn.idongjia.live.restructure.pojo.cmd.tab;

import cn.idongjia.live.restructure.pojo.Command;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * tab关联直播
 *
 * @author lc
 * @create at 2018/7/8.
 */
@Getter
@Setter
@NoArgsConstructor

public class PageTabLiveDeleteCmd extends Command {
    private Long id;
}
