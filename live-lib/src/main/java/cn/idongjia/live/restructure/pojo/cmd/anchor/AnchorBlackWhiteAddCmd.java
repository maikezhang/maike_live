package cn.idongjia.live.restructure.pojo.cmd.anchor;

import cn.idongjia.live.restructure.pojo.Command;
import cn.idongjia.live.restructure.pojo.co.live.AnchorBlackWhiteCO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/2
 * Time: 下午12:17
 */
@Getter
@Setter
@NoArgsConstructor
public class AnchorBlackWhiteAddCmd extends Command {

    private List<Long> anchorIds;

    private Integer type;



}
