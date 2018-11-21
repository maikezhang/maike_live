package cn.idongjia.live.db.mybatis.po;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Created by zhang on 2017/3/9.
 * 纯直播订阅数据表类
 */
@Getter
@Setter
public class PureLiveBookPO extends BasePO {

    /**
     * 唯一ID
     */
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 直播ID
     */
    private Long liveId;
    /**
     * 订阅状态-1、删除0、正常
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 最后修改时间
     */
    private Timestamp modifiedTime;

}
