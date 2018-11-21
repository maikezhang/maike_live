package cn.idongjia.live.db.mybatis.query;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author lc
 * @create at 2018/6/13.
 */
@Getter
@Setter
public class DBLiveBannerQuery extends BaseQuery {

    /**
     * 主键
     */
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 分类id 0 首页
     */
    private Long  classificationId;
    /**
     * 记录状态
     */
    private Integer status;
    /**
     * 生效时间点(查询某个时间点生效的记录)
     */

    private Timestamp effectiveTimePoint;

    @Builder
    public DBLiveBannerQuery(String orderBy, Integer limit, Integer page, Long beginTime, Long endTime, Integer offset, Long id, String title,  Integer status,Long classificationId) {
        super(orderBy, limit, page, beginTime, endTime, offset);
        this.id = id;
        this.title = title;
        this.status = status;
        this.classificationId=classificationId;
    }

}
