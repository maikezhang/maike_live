package cn.idongjia.live.query.purelive.banner;

import cn.idongjia.common.query.BaseSearch;

import javax.ws.rs.QueryParam;
import java.sql.Timestamp;

/**
 * 直播banner查询
 * Created by dongjia_lj on 17/3/9.
 */
public class PureLiveBannerSearch extends BaseSearch {

    @QueryParam("bid")
    private Long bid;                   //主键
    @QueryParam("title")
    private String title;               //标题
    @QueryParam("type")
    private Integer type;               //跳转类型
    @QueryParam("addr")
    private String addr;                //跳转地址
    @QueryParam("status")
    private Integer status;             //记录状态

    @QueryParam("effectiveTimePoint")
    private Timestamp effectiveTimePoint;//生效时间点(查询某个时间点生效的记录)

    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getEffectiveTimePoint() {
        return effectiveTimePoint;
    }

    public void setEffectiveTimePoint(Timestamp effectiveTimePoint) {
        this.effectiveTimePoint = effectiveTimePoint;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public PureLiveBannerSearch(){}
}
