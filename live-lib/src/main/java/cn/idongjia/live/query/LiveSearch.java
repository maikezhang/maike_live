package cn.idongjia.live.query;

import cn.idongjia.common.query.BaseSearch;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.QueryParam;

@Setter
@Getter
public class LiveSearch extends BaseSearch {
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

    @QueryParam("createTimeFrom")
    private Long createTimeFrom;

    @QueryParam("createTimeEnd")
    private Long createTimeEnd;

    @QueryParam("preStartTimeFrom")
    private Long preStartTimeFrom;

    @QueryParam("preStartTimeEnd")
    private Long preStartTimeEnd;

    @QueryParam("preEndTimeFrom")
    private Long preEndTimeFrom;

    @QueryParam("preEndTimeEnd")
    private Long preEndTimeEnd;

    @QueryParam("online")
    private Integer online;

    @QueryParam("startTimeFrom")
    private Long startTimeFrom;

    @QueryParam("startTimeEnd")
    private Long startTimeEnd;

    @QueryParam("endTimeFrom")
    private Long endTimeFrom;

    @QueryParam("endTimeEnd")
    private Long endTimeEnd;

}
