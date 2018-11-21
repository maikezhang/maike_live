package cn.idongjia.live.restructure.pojo.query;

import cn.idongjia.live.restructure.pojo.Page;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.ws.rs.QueryParam;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/2
 * Time: 下午12:27
 */
@Getter
@Setter
@ToString
public class AnchorBlackWhiteQry extends Page {


    @QueryParam("craftsmanUserId")
    private Long craftsmanUserId;

    @QueryParam("type")
    private Integer type;

    @QueryParam("status")
    private Integer status;

    @QueryParam("craftsmanName")
    private String craftsmanName;



}
