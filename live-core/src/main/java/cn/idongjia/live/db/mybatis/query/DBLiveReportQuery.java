package cn.idongjia.live.db.mybatis.query;

import cn.idongjia.common.base.Base;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.QueryParam;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/26
 * Time: 下午7:01
 */
@Setter
@Getter
public class DBLiveReportQuery extends BaseQuery {
    private Long userId;
    private Long liveId;
    private Long reportUid;
    private String content;

    @Builder
    public DBLiveReportQuery(Integer offset,String orderBy, Integer limit, Integer page, Long beginTime, Long endTime,Long userId,Long liveId,Long reportUid,String content){
        super(orderBy, limit, page, beginTime, endTime, offset);
        this.userId=userId;
        this.liveId=liveId;
        this.reportUid=reportUid;
        this.content=content;

    }
}
