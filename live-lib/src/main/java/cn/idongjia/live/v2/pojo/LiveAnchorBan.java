package cn.idongjia.live.v2.pojo;

import cn.idongjia.common.base.Base;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.QueryParam;

/**
 * @author yuexiaodong@idongjia.cn
 * @date 2018/06/11
 */
@Setter
@Getter
public class LiveAnchorBan extends Base {
    /**
     * 主播userId
     */
    @QueryParam("userId")
    private Long    userId;
    /**
     * 操作管理员id
     */
    @QueryParam("adminId")
    private Long    adminId;
    /**
     * 操作类型；2=禁播，1=解除禁播
     */
    @QueryParam("operation")
    private Integer operation;
    /**
     * 禁播持续天数；-1=永久
     */
    @QueryParam("durationDay")
    private Integer durationDay;
    /**
     * 操作原因
     */
    @QueryParam("message")
    private String  message;
    public LiveAnchorBan(){}
}
