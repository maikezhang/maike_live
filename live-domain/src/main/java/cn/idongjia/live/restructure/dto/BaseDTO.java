package cn.idongjia.live.restructure.dto;

import cn.idongjia.live.exception.LiveException;

import java.sql.Timestamp;

/**
 * DTO基类
 *
 * @author lc
 * @create at 2018/6/7.
 */
public abstract class BaseDTO<T> {
    protected T entity;

    public BaseDTO(T entity) {
        this.entity = entity;
    }

    public T toDO() {
        validate();
        return entity;
    }

    protected Long assembleTime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        } else {
            return timestamp.getTime();
        }
    }

    /**
     * 参数验证方法
     */
    protected void validate() {
        if (entity == null) {
            throw LiveException.failure("基础数据不能为空");
        }
    }


}
