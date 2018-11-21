package cn.idongjia.live.restructure.v2.domain.cloud;

import cn.idongjia.live.restructure.v2.support.ValueObject;
import cn.idongjia.live.restructure.v2.support.enumeration.LiveCloudType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 直播云类
 * @author zhang created on 2018/1/17 下午1:55
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class LiveCloud extends ValueObject {

    /**
     * 直播云id
     */
    private String        cloudId;
    /**
     * 直播云类型
     */
    private LiveCloudType type;

}
