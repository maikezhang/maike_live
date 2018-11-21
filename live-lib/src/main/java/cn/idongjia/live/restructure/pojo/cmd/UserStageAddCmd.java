package cn.idongjia.live.restructure.pojo.cmd;

import cn.idongjia.live.restructure.pojo.Command;
import cn.idongjia.live.restructure.pojo.co.UserStageLiveCO;
import cn.idongjia.live.restructure.pojo.co.UserStageLiveDetailCO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 添加直播强运营数据
 *
 * @author lc
 * @create at 2018/7/7.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserStageAddCmd extends Command {
    private List<Long> liveIds;
    private int stage;
}
