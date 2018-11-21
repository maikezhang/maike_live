package cn.idongjia.live.db.mybatis.po;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Description: 直播主播订阅
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/13
 * Time: 上午9:51
 */
@Setter
@Getter
public class LiveAnchorBookPO extends BasePO {
    //唯一ID
    private Long id;
    //用户ID
    private Long uid;
    //匠人ID
    private Long anchorId;
    //订阅状态-1、删除0、正常
    private Integer status;
    //创建时间
    private Timestamp createTm;
    //最后修改时间
    private Timestamp modifiedTm;

}
