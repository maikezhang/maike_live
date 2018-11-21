package cn.idongjia.live.support;

import cn.idongjia.live.exception.LiveException;

import java.util.List;


/**
 * 一些对象相关的工具类
 * <p>
 * Created by 岳晓东 on 2018/01/30.
 */
public class ObjectUtils {

    private ObjectUtils() {

    }

    public static <T> T checkNotNull(T reference, Object errorMessage) {
        if (reference == null) {
            throw LiveException.failure(String.valueOf(errorMessage));
        }
        return reference;
    }

    public static void checkArgument(boolean expression, Object errorMessage) {
        if (!expression) {
            throw LiveException.failure(String.valueOf(errorMessage));
        }
    }

    public static boolean isEmptyList(List list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

}
