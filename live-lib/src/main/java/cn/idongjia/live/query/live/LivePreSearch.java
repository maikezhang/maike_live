package cn.idongjia.live.query.live;

import cn.idongjia.common.query.BaseSearch;
import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/7/13
 * Time: 上午11:39
 */
@Setter
@Getter
public class LivePreSearch extends BaseSearch {


    /**
     * 直播类型
     */
    private Integer liveType;

    /**
     * 上下线
     */
    private Integer online;

    /**
     * 直播进程
     */
    private Integer state;

    /**
     * 用户id
     */
    private Long userId;

}
