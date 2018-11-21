package cn.idongjia.live.restructure.domain.entity.live;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/13
 * Time: 下午3:47
 */
@Setter
@Getter
@ToString
public class LivePureBook {

    //唯一ID
    private Long id;
    //用户ID
    private Long uid;
    //直播ID
    private Long lid;
    //订阅状态-1、删除0、正常
    private Integer status;
    //创建时间
    private Long createTime;
    //最后修改时间
    private Long modifiedTime;

}
