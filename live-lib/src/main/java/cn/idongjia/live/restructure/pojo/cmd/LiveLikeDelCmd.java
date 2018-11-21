package cn.idongjia.live.restructure.pojo.cmd;

import cn.idongjia.live.restructure.pojo.Command;
import cn.idongjia.live.restructure.pojo.co.live.LiveLikeCO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/3
 * Time: 上午9:46
 */
@Getter
@Setter
@NoArgsConstructor
public class LiveLikeDelCmd extends Command {

   private LiveLikeCO liveLikeCO;
}
