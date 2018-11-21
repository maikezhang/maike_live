package cn.idongjia.live.restructure.pojo.query;

import cn.idongjia.live.restructure.pojo.Page;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.ws.rs.QueryParam;

/**
 * tab 关联直播查询
 *
 * @author lc
 * @create at 2018/7/8.
 */
@Getter
@Setter
@ToString
public class PageTabLiveQry extends Page {

    /**
     * 直播标题
     */
    @QueryParam("title")
    private String title;

    /**
     * 专场id
     */
    @QueryParam("sessionId")
    private Long sessionId;
    /**
     * 直播id
     */
    @QueryParam("liveId")
    private Long liveId;


    /**
     * tab id
     */
    @QueryParam("tabId")
    private Long tabId;

    public PageTabLiveQry(){
    }





}
