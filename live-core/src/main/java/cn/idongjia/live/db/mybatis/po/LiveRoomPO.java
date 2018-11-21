package cn.idongjia.live.db.mybatis.po;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author lc
 * @create at 2018/6/7.
 */
@Getter
@Setter
public class LiveRoomPO extends BasePO {
    //直播间ID
    private Long      id;
    //直播间云类型
    private Integer   cloudType;
    //直播间云ID
    private String    cloudId;
    //直播间状态
    private Integer   status;
    //直播间直播ID
    private Long      userId;
    //直播间创建时间
    private Timestamp createTime;
    //直播间更新时间
    private Timestamp modifiedTime;
}
