package cn.idongjia.live.db.mybatis.po;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/27
 * Time: 下午5:06
 */
@Setter
@Getter
@Builder
public class LiveAnchorPO {

    /**
     *
     */
    private Long    id;
    /**
     * 主播的用户id，用户表的uid
     */
    private Long    userId;
    /**
     * 当前主播状态；1=可以直播，2=禁播
     */
    private Integer anchorState;
    /**
     * 更新时间，13位时间戳
     */
    private Long    updateTime;
    /**
     * 创建时间，13位时间戳
     */
    private Long    createTime;
}
