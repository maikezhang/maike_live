package cn.idongjia.live.restructure.cache.pojo;

import cn.idongjia.common.base.Base;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lc
 * @create at 2018/5/9.
 */
@Getter
@Setter
public class LiveUser extends Base {
    private Long uid;
    private String avatar;
    private String username;
    private String title;
}
