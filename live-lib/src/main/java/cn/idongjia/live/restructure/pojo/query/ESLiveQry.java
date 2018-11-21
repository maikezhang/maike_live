package cn.idongjia.live.restructure.pojo.query;

import cn.idongjia.live.restructure.pojo.Page;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * 索引查询直播数据
 *
 * @author lc
 * @create at 2018/7/7.
 */

@Getter
@Setter
@ToString(callSuper = true)
public class ESLiveQry extends Page {

    public  ESLiveQry(){

    }
    //查询id
    @QueryParam("ids")
    private List<Long> ids;

    //更新时间
    @QueryParam("updateTime")
    private Long updateTime;

}
