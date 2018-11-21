package cn.idongjia.live.support.redis;

import java.util.ArrayList;
import java.util.List;

import static cn.idongjia.live.support.RedisConst.DOT;
import static cn.idongjia.live.support.RedisConst.UNDER_LINE;
import static cn.idongjia.live.support.RedisConst.WORK_SPACE;


/**
 * @version 1.0
 */
public interface RedisKey {


    /**
     * 创建key
     * @param objs key数组
     * @return key
     */
    default String buildKey(Object... objs) {
        StringBuilder temp = getKeyPrefix();
        temp.append(objs[0]);
        if (objs.length > 1) {
            for (int i = 1; i < objs.length; i++) {
                temp.append(UNDER_LINE).append(objs[i]);
            }
        }
        return temp.toString();
    }

    /**
     * 获取命名空间前缀
     * @return 前缀
     */
    default StringBuilder getKeyPrefix() {
        StringBuilder temp = new StringBuilder(WORK_SPACE);
        temp.append(this.getClass().getSimpleName()).append(DOT);
        return temp;
    }

    /**
     * 批量创建key
     * @param objs key列表
     * @return List<String> key列表
     */
    default List<String> buildKeys(List<String> objs) {
        List<String> result = new ArrayList<>(objs.size());
        StringBuilder temp = getKeyPrefix();
        int start = temp.length();
        for (String key : objs) {
            result.add(temp.append(key).toString());
            temp.delete(start, temp.length());
        }
        return result;
    }
}
