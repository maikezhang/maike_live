package cn.idongjia.live.query.live;


import cn.idongjia.common.query.BaseSearch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.QueryParam;

@Setter
@Getter
public class LiveReportSearch extends BaseSearch {

    @QueryParam("hostUid")
    private Long uid;
    @QueryParam("lid")
    private Long lid;
    @QueryParam("reportUid")
    private Long reportUid;
    @QueryParam("content")
    private String content;

    public LiveReportSearch(){}
}
