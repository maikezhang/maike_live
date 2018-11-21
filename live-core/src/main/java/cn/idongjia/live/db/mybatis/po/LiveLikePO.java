package cn.idongjia.live.db.mybatis.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/3
 * Time: 上午11:40
 */
@Getter
@Setter
@ToString
public class LiveLikePO extends BasePO{

    private Long id;

    private Long liveId;

    private Long userId;

    private Integer status;

    private Long createTime;

    private Long updateTime;
}
