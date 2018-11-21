package cn.idongjia.live.db.mybatis.query;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class DBLiveModuleQuery extends BaseQuery {
    private Long id;
    //资源主标题
    private String title;
    //资源生效时间
    private Long startTime;
    //资源位置
    private Integer position;
    //资源进程
    private Integer state;
    //资源状态
    private Integer status;

    private List<Integer> positions;

    @Builder
    public DBLiveModuleQuery(String orderBy, Integer limit, Integer page, Long beginTime, Long endTime, Integer offset, Long id, String title,  Integer status,Integer state,Integer position,Long
            startTime,List<Integer> positions) {
        super(orderBy, limit, page, beginTime, endTime, offset);
        this.id = id;
        this.title = title;
        this.status = status;
        this.state=state;
        this.position=position;
        this.startTime=startTime;
        this.positions=positions;
    }
}
