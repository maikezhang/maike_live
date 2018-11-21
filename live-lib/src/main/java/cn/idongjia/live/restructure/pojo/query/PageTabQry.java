package cn.idongjia.live.restructure.pojo.query;

import cn.idongjia.live.restructure.pojo.Page;
import cn.idongjia.live.restructure.pojo.Query;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * 页面tab分页查询
 *
 * @author lc
 * @create at 2018/7/6.
 */
@Getter
@Setter
public class PageTabQry extends Page {
    /**
     * tab类型
     */
    @QueryParam("type")
    private Integer type;

    /**
     * tab id
     */
    @QueryParam("id")
    private Long id;
    /**
     * 状态
     */
    @QueryParam("status")
    private Integer status;

    /**
     * 上下线
     */
    @QueryParam("online")
    private Integer online;

    public PageTabQry(){
    }


}
