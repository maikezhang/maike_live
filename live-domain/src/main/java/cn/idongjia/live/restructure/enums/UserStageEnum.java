package cn.idongjia.live.restructure.enums;

import cn.idongjia.live.support.BaseEnum;

/**
 * @author lc
 * @create at 2018/7/7.
 */
public class UserStageEnum {

    public enum Stage implements BaseEnum {
        /**
         * 未开始
         */
        NEW_STAGE(1, "新用户"),
        /**
         * 直播中
         */
        OLD_STAGE(2, "老用户");

        Stage(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        private int code;
        private String msg;

        /**
         * 获取对应code
         *
         * @return code
         */
        @Override
        public int getCode() {
            return this.code;
        }

        /**
         * 获取对应msg
         *
         * @return msg
         */
        @Override
        public String getMsg() {
            return this.msg;
        }
    }
}
