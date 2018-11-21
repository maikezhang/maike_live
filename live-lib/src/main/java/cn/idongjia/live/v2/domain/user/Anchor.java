package cn.idongjia.live.v2.domain.user;

import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.v2.support.ValueObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * 直播主播类
 * @author zhang created on 2018/1/17 下午1:42
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class Anchor extends ValueObject{

    /**
     * 主播id
     */
    private Long userId;

    public Anchor(Long userId){
        this.setUserId(userId);
    }

    /**
     * 主播设置id 必填
     * @param userId 主播id
     */
    private void setUserId(Long userId){
        if (Objects.isNull(userId)){
            throw LiveException.failure("主播id必填");
        }
        if (userId < 1){
            throw LiveException.failure("主播id不合理");
        }
        this.userId = userId;
    }

}
