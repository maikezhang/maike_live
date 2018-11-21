package cn.idongjia.live.restructure.pojo.query;

import cn.idongjia.live.restructure.pojo.Page;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/2
 * Time: 下午12:27
 */
@Getter
@Setter
public class LiveMPQry extends Page {


    @QueryParam("liveId")
    private Long liveId;

    @QueryParam("title")
    private String title;

    @QueryParam("state")
    private Integer state;

    @QueryParam("maxPreStartTime")
    private Long maxPreStartTime;

    @QueryParam("minPreStartTime")
    private Long minPreStartTime;

    @QueryParam("craftsmanUserId")
    private Long craftsmanUserId;




}
