package cn.idongjia.live.query.live;

import cn.idongjia.common.query.BaseSearch;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.QueryParam;

@Getter
@Setter
public class LiveBannerSearch extends BaseSearch {
    //主键
    @QueryParam("bid")
    private Long id;
    //状态
    @QueryParam("status")
    private Integer status;
    //分类id
    @QueryParam("classificationId")
    private Long classificationId;
    //标题
    @QueryParam("title")
    private String title;

    public LiveBannerSearch(){}
}
