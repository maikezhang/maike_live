package cn.idongjia.live.restructure.pojo.query;

import cn.idongjia.live.restructure.pojo.Page;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lc
 * @create at 2018/7/7.
 */
@Getter
@Setter
public class UserStageLiveQry extends Page {

    private Integer type;

    private Integer status;

    public UserStageLiveQry() {
    }

}
