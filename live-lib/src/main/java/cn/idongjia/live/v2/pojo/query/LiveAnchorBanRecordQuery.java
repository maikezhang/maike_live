package cn.idongjia.live.v2.pojo.query;

import cn.idongjia.common.query.BaseSearch;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * 主播禁播记录查询
 *
 * @author yuexiaodong@idongjia.cn
 * @date 2018/06/11
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LiveAnchorBanRecordQuery extends BaseSearch {

    public static final List<String> ORDERBY_CONDITIONS = Lists.newArrayList("create_time");
    public static final List<String> ORDER_CONTIDIONTS  = Lists.newArrayList("asc", "desc");

    /**
     * 用户id
     */
    @QueryParam("userId")
    private Long       userId;
    /**
     * 用户名
     */
    @QueryParam("userName")
    private String     userName;
    /**
     * 操作日期（13位时间戳），优先起始查询条件，接收某一天0点开始的时间戳
     */
    @QueryParam("operationDate")
    private Long       operationDate;
    /**
     * 操作时间（13位时间戳），起
     */
    @QueryParam("operationTimeFrom")
    private Long       operationTimeFrom;
    /**
     * 操作时间（13位时间戳），只
     */
    @QueryParam("operationTimeEnd")
    private Long       operationTimeEnd;
    /**
     * 主播状态
     */
    @QueryParam("anchorState")
    private Integer    anchorState;
    /**
     * 管理员id
     */
    @QueryParam("adminId")
    private Long       adminId;
    /**
     * 操作类型；2=禁播，1=解除禁播
     */
    @QueryParam("operation")
    private Integer    operation;
    /**
     * 匠人uid列表
     */
    @QueryParam("userIds")
    private List<Long> userIds;

    public LiveAnchorBanRecordQuery(){}
}
