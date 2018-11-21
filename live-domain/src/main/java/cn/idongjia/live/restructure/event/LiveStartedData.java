package cn.idongjia.live.restructure.event;

import cn.idongjia.live.restructure.domain.entity.live.LivePullUrlV;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class LiveStartedData {

    /**
     * 直播间id
     */
    private Long        liveRoomId;
    /**
     * 拉流地址
     */
    private LivePullUrlV livePullUrl;
    /**
     * 直播名称
     */
    private String      title;
    /**
     * 聊天室id
     */
    private Long        zid;

    /**
     * 类型
     */
    private Integer type;

    /**
     * id
     */
    private Long id;

    /**
     * 状态
     */

    private Integer online;
    /**
     * 描述信息
     */
    private String  showDesc;

    private Long uid;
}
