package cn.idongjia.live.db.mybatis.po;

import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/13
 * Time: 上午9:50
 */
@Getter
@Setter
public class LiveBookCountPO extends BasePO {
    /**
     * 统计数量
     */
    private Integer count;
    /**
     * 直播ID
     */
    private Long liveId;


}
