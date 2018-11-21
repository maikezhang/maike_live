package cn.idongjia.live.restructure.v2.domain.show;

import cn.idongjia.live.restructure.v2.domain.cloud.LiveCloud;
import cn.idongjia.live.restructure.v2.domain.user.Anchor;
import cn.idongjia.live.restructure.v2.support.Entity;
import cn.idongjia.live.restructure.v2.support.enumeration.LiveRoomStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * 直播房间类
 * @author zhang created on 2018/1/17 下午1:53
 * @version 1.0
 */
@Getter
@Setter
public class LiveRoom extends Entity {

    /**
     * 直播房间id
     */
    private Long           id;
    /**
     * 主播
     */
    private Anchor         anchor;
    /**
     * 直播云
     */
    private LiveCloud      liveCloud;
    /**
     * 直播房间状态
     */
    private LiveRoomStatus status;
}
