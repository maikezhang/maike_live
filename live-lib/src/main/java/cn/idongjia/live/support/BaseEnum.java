package cn.idongjia.live.support;

import java.util.Objects;
import java.util.Optional;

/**
 * enum 基础接口
 *
 * @version 1.0
 */
public interface BaseEnum {

    /**
     * 获取对应code
     * @return code
     */
    int getCode();

    /**
     * 获取对应msg
     * @return msg
     */
    String getMsg();

    public  static <T extends BaseEnum> Optional<T> parseInt2Enum(Integer code, T[] list) {
        if (Objects.isNull(code)) {
            return Optional.empty();
        }
        for (T t : list) {
            if (t.getCode() == code) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    public enum YesOrNo implements BaseEnum {
        NO(0, "否"), YES(1, "是");

        int    code;
        String msg;

        YesOrNo(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        @Override
        public int getCode() {
            return this.code;
        }

        @Override
        public String getMsg() {
            return this.msg;
        }
    }

    public enum DataStatus implements BaseEnum {
        DELETE_STATUS(-1, "已删除"), NORMAL_STATUS(1, "正常");

        int    code;
        String msg;

        DataStatus(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        @Override
        public int getCode() {
            return this.code;
        }

        @Override
        public String getMsg() {
            return this.msg;
        }
    }

}

