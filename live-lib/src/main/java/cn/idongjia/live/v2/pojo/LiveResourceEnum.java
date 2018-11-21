package cn.idongjia.live.v2.pojo;

import cn.idongjia.live.support.BaseEnum;

public class LiveResourceEnum  {

    public enum Status implements BaseEnum {
        /**
         * 纯直播
         */
        MAIN(1, "主推"),
        /**
         * 直播拍
         */
        MAINNOT(2, "非主推");

        Status(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        private int    code;
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
