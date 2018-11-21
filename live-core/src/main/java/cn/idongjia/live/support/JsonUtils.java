package cn.idongjia.live.support;

import cn.idongjia.util.Utils;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author lc
 */
public final class JsonUtils {

    public static final Gson GSON = new Gson();

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    private static final String _callback = "_callback";


    private static  final String EMPTY="";
    /**
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        if (object == null) {
            return EMPTY;
        }
        return JSON.toJSONString(object);
    }

    public static String toJson(Object object, String callback) {
        callback = StringUtils.isBlank(callback) ? _callback : callback;
        if (object == null) {
            return callback;
        }
        return new StringBuilder(callback).append("(").append(JSON.toJSONString(object))
            .append(")").toString();
    }

    /**
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json) || clazz == null) {
            return null;
        }
        try {
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            LOGGER.error("---parse json fail:" + json, e);
            return null;
        }
    }


    public static <T> List<T> toList(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json) || clazz == null) {
            return Collections.emptyList();
        }
        try {
            return JSON.parseArray(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }






}
