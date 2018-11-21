package cn.idongjia.live.restructure.pojo.cmd;

import cn.idongjia.live.restructure.pojo.Command;
import cn.idongjia.live.restructure.pojo.co.UserStageLiveCO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 添加直播强运营数据
 *
 * @author lc
 * @create at 2018/7/7.
 */
@Getter
@Setter
@NoArgsConstructor

public class UserStageDelCmd extends Command {
    private long id;
    private int userStage;
}
