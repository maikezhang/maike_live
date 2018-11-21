package cn.idongjia.live.pojo.live;

import cn.idongjia.live.support.BaseEnum;

/**
 * @author yuexiaodong@idongjia.cn
 * @date 2018/06/11
 */
public final class LiveAnchorEnum {
    private LiveAnchorEnum() {

    }

    public enum BanOperationType implements BaseEnum {
        BANNED(2, "禁播"),
        RELEASE(1, "取消禁播");

        private int    code;
        private String msg;

        BanOperationType(int code, String msg) {
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


    public enum AnchorState implements BaseEnum {
        BANNED(2, "禁播"),
        RELEASE(1, "取消禁播");

        private int    code;
        private String msg;

        AnchorState(int code, String msg) {
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

    public enum AnchorType implements BaseEnum {
        BANNED(0, "匠人");

        private int    code;
        private String msg;

        AnchorType(int code, String msg) {
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
