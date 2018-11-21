package cn.idongjia.live.restructure.pojo.query;

import cn.idongjia.live.restructure.pojo.Page;
import lombok.Getter;
import lombok.Setter;

/**
 * tab 关联直播查询
 *
 * @author lc
 * @create at 2018/7/8.
 */
@Getter
@Setter
public class PageTabLiveApiQry extends Page {


    /**
     * tab id
     */
    private Long tabId;
    /**
     * 用户id
     */
    private Long uid;

    /**
     * 设备号
     */
    private String deviceId;

    public PageTabLiveApiQry() {

    }


}
