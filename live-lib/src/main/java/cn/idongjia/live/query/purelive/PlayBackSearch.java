package cn.idongjia.live.query.purelive;

import cn.idongjia.common.query.BaseSearch;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PlayBackSearch extends BaseSearch {

    //ID
    private Long id;
    //直播ID
    private Long lid;
    //直播id列表
    private List<Long> liveIds;
    //回放时长
    private Long duration;
    //回放状态
    private Integer status;

    public PlayBackSearch(){}
}
