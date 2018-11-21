package cn.idongjia.live.restructure.domain.entity;

import cn.idongjia.live.support.BaseEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lc
 * @create at 2018/7/7.
 */

@Getter
@Setter
public abstract class Entity {
    protected Long id;
    protected Long createTime;
    protected Long updateTime;

}
