package cn.idongjia.live.restructure.rule;

import cn.idongjia.live.support.BaseEnum;

/**
 * @author lc on 2018/7/11.
 * @class cn.idongjia.live.restructure.rule.Rule
 */
public interface Rule<T> {
    public boolean validate(T t);
}
