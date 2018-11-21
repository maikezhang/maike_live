package cn.idongjia.live.support;

import cn.idongjia.consts.TokenType;

import java.util.Collections;
import java.util.List;

/**
 * Created by 岳晓东 on 2018/02/02.
 */
public class GsonUtil {

    public static List<String> parseJsonArrayString(String str) {
        if (str == null || str.equals("")) {
            return Collections.EMPTY_LIST;
        }
        //json数组字符串
        List<String> pics = cn.idongjia.util.GsonUtil.parse(str, TokenType.LIST_STRING_TYPE);
        return pics;
    }

    public static String getFirstStringInJsonArray(String str) {
        List<String> strs = parseJsonArrayString(str);
        if (strs.size() == 0) {
            return null;
        }
        return strs.get(0);
    }

}
