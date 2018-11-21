package cn.idongjia.live.db.mybatis.po;

import cn.idongjia.common.base.Base;
import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/7/27
 * Time: 下午3:23
 */
@Setter
@Getter
public class LiveAuctionSessionPO extends Base {

    private Long sessionId;

    private Long liveId;

    private Long videoCoverId;
}
