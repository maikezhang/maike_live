package cn.idongjia.live.db.mybatis.po;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/12
 * Time: 下午10:59
 */
@Getter
@Setter
public class LiveTagRelPO extends BasePO {
    //唯一ID
    private Long id;
    //标签ID
    private Long tagId;
    //关联直播ID
    private Long liveId;
    //关联状态
    private Integer status;
    //创建时间
    private Timestamp createTime;
    //最后修改时间
    private Timestamp modifiedTime;
}
