package cn.idongjia.live.query.live;

import cn.idongjia.common.query.BaseSearch;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.QueryParam;
import java.util.List;

@Setter
@Getter
public class LiveShowSearch extends BaseSearch {
    private static final long serialVersionUID = -8114841342916302446L;

    @QueryParam("id")
    private Long id;
    @QueryParam("title")

    private String title;
    @QueryParam("userId")

    private Long uid;
    @QueryParam("type")

    private Integer type;

    @QueryParam("state")
    private Integer state;
    @QueryParam("createTmBegin")
    private Long createTmBegin;
    @QueryParam("createTmEnd")
    private Long createTmEnd;
    @QueryParam("preStartTmBegin")

    private Long preStartTmBegin;
    @QueryParam("preStartTmEnd")
    private Long preStartTmEnd;
    @QueryParam("zooId")

    private Long zid;
    @QueryParam("exemption")
    private Integer exemption;
    @QueryParam("preEndTimeFrom")
    private Long preEndTimeFrom;
    @QueryParam("preEndTimeEnd")

    private Long preEndTimeEnd;
    @QueryParam("liveIds")
    private List<Long> liveIds;
    @QueryParam("stateList")
    private List<Integer> stateList;

    public LiveShowSearch(){}
}
