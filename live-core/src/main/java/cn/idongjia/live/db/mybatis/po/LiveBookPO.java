package cn.idongjia.live.db.mybatis.po;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/13
 * Time: 上午9:50
 */
@Getter
@Setter
public class LiveBookPO extends BasePO {
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
