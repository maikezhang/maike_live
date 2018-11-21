package cn.idongjia.live.restructure.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class LiveStopData {

    /**
     * 直播间id
     */
    private Long liveRoomId;

    /**
     * id
     */
    private Long id;


    /**
     * 聊天室id
     */
    private Long zid;

    /**
     * 标题
     */
    private String title;

    /**
     * 类型
     */
    private Integer type;
    /**
     * 修改类型
     */
    private Long    uid;
}
