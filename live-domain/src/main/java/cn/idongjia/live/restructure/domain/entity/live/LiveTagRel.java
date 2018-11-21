package cn.idongjia.live.restructure.domain.entity.live;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/6/17
 * Time: 上午9:06
 */
@Setter
@Getter
@Builder
@ToString
public class LiveTagRel {


    //唯一ID
    private Long id;
    //标签ID
    private Long tagId;
    //关联直播ID
    private Long lid;
    //关联状态
    private Integer status;
    //创建时间
    private Timestamp createTm;
    //最后修改时间
    private Timestamp modifiedTm;
}
