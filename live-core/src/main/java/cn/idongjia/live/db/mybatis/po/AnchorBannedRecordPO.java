package cn.idongjia.live.db.mybatis.po;

import cn.idongjia.common.base.Base;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.annotations.Param;

/**
 * 描述：主播禁播记录表 Database Object
 *
 * @author YueXiaodong, yuexiaodong@idongjia.cn
 * @date 2018/06/11
 */
@Builder
@Setter
@Getter
public class AnchorBannedRecordPO extends Base {

    /**
     *
     */
    private Long    id;
    /**
     * 匠人uid
     */
    private Long    userId;
    /**
     * 操作管理员id
     */
    private Long    adminId;
    /**
     * 操作类型；2=禁播，1=解除禁播
     */
    private Integer operation;
    /**
     * 禁播持续天数；-1=永久
     */
    private Integer durationDay;
    /**
     * 操作原因
     */
    private String  message;
    /**
     * 更新时间，13位时间戳
     */
    private Long    updateTime;
    /**
     * 创建时间，13位时间戳
     */
    private Long    createTime;

}
