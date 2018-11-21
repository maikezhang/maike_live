package cn.idongjia.live.query.live;

import cn.idongjia.common.query.BaseSearch;
import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/7/16
 * Time: 下午5:18
 */
@Getter
@Setter
public class LiveListApiSearch extends BaseSearch {

    private Integer liveType;

    private Long userId;

}
