package cn.idongjia.live.db.mybatis.po;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lc
 * @create at 2018/6/12.
 */
@Getter
@Setter
public class LiveResourceCountPO extends BasePO {
    private Long liveId;
    private int count;
}
