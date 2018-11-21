package cn.idongjia.live.message;

import cn.idongjia.mq.message.body.MessageHeader;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author lc
 * @create at 2018/7/9.
 */
@Getter
@Setter
public class UserStageLiveMessage extends MessageHeader {
    /**
     * 直播id
     */

    private List<Long> liveIds;
    /**
     * 用户分级 0 新用户 1老用户
     */
    private Integer userStage;
    /**
     * 操作 0 删除 1 添加
     */
    private int action;
}
