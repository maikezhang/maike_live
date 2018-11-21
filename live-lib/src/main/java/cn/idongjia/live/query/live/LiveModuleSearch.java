package cn.idongjia.live.query.live;

import cn.idongjia.common.query.BaseSearch;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.QueryParam;
import java.util.List;

@Setter
@Getter
public class LiveModuleSearch extends BaseSearch {
    //主键
    @QueryParam("mid")
    private Long id;
    //主标题
    @QueryParam("title")
    private String title;
    //开始时间
    @QueryParam("startTime")
    private Long startTime;
    //位置
    @QueryParam("position")
    private Integer position;
    //状态
    @QueryParam("status")
    private Integer status;
    //进程
    @QueryParam("state")
    private Integer state;
    @QueryParam("positions")
    private List<Integer> positions;

    public LiveModuleSearch(){}
}
