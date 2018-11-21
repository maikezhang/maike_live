package cn.idongjia.live.db.mybatis.po;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Created by zhang on 2017/3/9.
 * 订阅匠人数据表类
 */
@Getter
@Setter
public class AnchorBookPO extends BasePO {
    private static final long serialVersionUID = 1625603959045952488L;

    //唯一ID
    private Long      id;
    //用户ID
    private Long      userId;
    //匠人ID
    private Long      anchorId;
    //订阅状态-1、删除0、正常
    private Integer   status;
    //创建时间
    private Timestamp createTime;
    //最后修改时间
    private Timestamp modifiedTime;

}
