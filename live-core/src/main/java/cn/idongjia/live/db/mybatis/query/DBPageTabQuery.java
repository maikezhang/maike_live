package cn.idongjia.live.db.mybatis.query;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 页面tab查询条件
 *
 * @author lc
 * @create at 2018/7/6.
 */
@Getter
@Setter
public class DBPageTabQuery extends BaseQuery {
    /**
     * 查询类型
     */
    private Integer type;


    /**
     * id
     */
    private List<Long> ids;


    /**
     * 数据状态
     */
    private Integer status;


    /**
     * 名称
     */
    private String name;

    /**
     * 上架下架状态
     */
    private Integer online;

    @Builder
    public DBPageTabQuery(String orderBy, Integer limit, Integer page, Long beginTime, Long endTime, Integer offset,
            Integer type, List<Long> ids, Integer status, String name,Integer online) {
        super(orderBy, limit, page, beginTime, endTime, offset);
        this.type = type;
        this.ids = ids;
        this.status = status;
        this.online=online;
        this.name = name;
    }
}
