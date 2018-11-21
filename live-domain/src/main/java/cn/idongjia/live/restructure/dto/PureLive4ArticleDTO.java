package cn.idongjia.live.restructure.dto;

import cn.idongjia.clan.lib.pojo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author lc
 * @create at 2018/7/17.
 */
@Getter
@Setter
@ToString
public class PureLive4ArticleDTO {
    private LiveShowDTO liveShowDTO;
    private User user;
    private LivePureDTO livePureDTO;
}
