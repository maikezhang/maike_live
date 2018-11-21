package cn.idongjia.live.v2.pojo;

import cn.idongjia.common.base.Base;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 主播禁播操作记录
 *
 * @author yuexiaodong@idongjia.cn
 * @date 2018/06/11
 */
@Setter
@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LiveAnchorBanRecord extends Base {

    /**
     * 主键id
     */
    private Long    id;
    /**
     * 主播userId
     */
    private Long    userId;
    /**
     * 操作管理员id
     */
    private Long    adminId;
    /**
     * 禁播持续天数；-1=永久
     */
    private Integer durationDay;
    /**
     * 操作类型；2=禁播，1=解除禁播
     */
    private Integer operation;
    /**
     * 操作原因
     */
    private String  message;
    /**
     * 修改时间
     */
    private Long    updateTime;
    /**
     * 创建时间
     */
    private Long    createTime;
    /**
     * 主播名称
     */
    private String  userName;
    /**
     * 主播注册时间
     */
    private Long    userRegisterTime;
    /**
     * 管理员名称
     */
    private String  adminName;


}
