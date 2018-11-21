package cn.idongjia.live.restructure.dto;

import cn.idongjia.clan.lib.pojo.User;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lc
 * @create at 2018/7/17.
 */
@Getter
@Setter
public class ForeShowDTO {
    private LiveShowDTO liveShowDTO;
    private User user;
    private LivePureDTO livePureDTO;
    private boolean isBook;
}
