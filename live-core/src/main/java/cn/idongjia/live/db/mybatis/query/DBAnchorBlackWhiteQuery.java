package cn.idongjia.live.db.mybatis.query;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/2
 * Time: 下午4:41
 */
@Getter
@Setter
public class DBAnchorBlackWhiteQuery extends BaseQuery {

    /**
     * 直播主播id
     */
    private List<Long> anchorIds;

    private Integer type;


    @Builder
    public DBAnchorBlackWhiteQuery(String orderBy, Integer limit,Integer offset, Integer page, Long beginTime, Long endTime,List<Long> anchorIds,Integer type){
        super(orderBy, limit, page, beginTime, endTime, offset);
        this.anchorIds=anchorIds;
        this.type=type;
    }
}
