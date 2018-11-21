package cn.idongjia.live.restructure.pojo;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * 命令
 *
 * @author lc
 * @create at 2018/7/7.
 */
public class Command  implements Serializable ,Cloneable {
    protected static final Gson GSON = new Gson();

    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}
