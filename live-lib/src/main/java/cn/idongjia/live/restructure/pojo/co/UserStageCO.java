package cn.idongjia.live.restructure.pojo.co;

import cn.idongjia.live.restructure.pojo.ClientObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author lc
 * @create at 2018/7/10.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor

public class UserStageCO extends ClientObject {
    /**
     * 用户区分
     */
    private Integer stage;
    /**
     * 推荐权重
     */
    private Integer weight;
}
