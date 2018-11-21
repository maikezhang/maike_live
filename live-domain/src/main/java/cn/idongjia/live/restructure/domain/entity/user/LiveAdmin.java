package cn.idongjia.live.restructure.domain.entity.user;

import lombok.Builder;
import lombok.Getter;

/**
 * @author yuexiaodong@idongjia.cn
 * @date 2018/06/21
 */
@Getter
@Builder
public class LiveAdmin {

    /**
     * 管理员id
     */
    private Long id;
    /**
     * 管理员名字
     */
    private String name;

}
