package cn.idongjia.live.restructure.enums;

import cn.idongjia.live.support.BaseEnum;

/**
 * 直播枚举
 *
 * @author lc
 * @create at 2017/12/21.
 */
public class LiveEnum {

    public enum LiveType implements BaseEnum {
        /**
         * 纯直播
         */
        PURE_LIVE(1, "纯直播"),
        /**
         * 直播拍
         */
        LIVE_AUCTION(2, "直播拍"),

        /**
         * 匠购
         */
        CRAFTS_PURCHASE_TYPE(3,"匠购"),

        TREASURE_TYPE(4,"探宝"),

        CRAFTS_TALK_TYPE(5,"匠说"),

        ELSE_TYPE(6,"其他"),

        /**
         * 开料
         */
        OPEN_MATERIAL_TYPE(7,"开料");




        LiveType(int code, String msg) {
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

    /**
     * 直播状态
     */
    public enum LiveState implements BaseEnum {
        /**
         * 未开始
         */
        UNSTART(1, "未开始"),
        /**
         * 直播中
         */
        PLAYING(2, "直播中"),
        /**
         * 已结束
         */
        FINISHED(3, "已结束");

        LiveState(int code, String msg) {
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

    public enum LiveStatus implements BaseEnum {
        /**
         * 未上线
         */
        OFFLINE(0, "未上线"),
        /**
         * 已上线
         */
        ONLINE(1, "已上线");

        LiveStatus(int code, String msg) {
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

    //所有直播通用的上下线状态
    public enum LiveOnline implements BaseEnum {
        ONLINE(1, "上线"), OFFLINE(0, "下线");
        private int    code;
        private String msg;

        LiveOnline(int code, String msg) {
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

    public enum HasPlayback implements BaseEnum {
        NO(0, "没有回放"), YES(1, "有回放");

        private Integer code;
        private String  msg;

        HasPlayback(Integer code, String msg) {
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

    public enum ScreenDirection implements BaseEnum {
        Vertical(1, "竖屏"), Horizontal(0, "横屏");

        private Integer code;
        private String  msg;

        ScreenDirection(Integer code, String msg) {
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

    public enum IsAuction implements BaseEnum {
        IS_AUCTION(1, "直播拍"), NOT_AUCTION(0, "非直播拍");

        private int    code;
        private String msg;

        IsAuction(int code, String msg) {
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

    public enum Debug implements BaseEnum {
        IS_DEBUG(1, "调试"), NOT_DEBUG(0, "非调试");
        private int    code;
        private String msg;

        Debug(int code, String msg) {
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

    /**
     * 直播关联商品的一些操作
     */
    public enum UserItemOperation implements BaseEnum {
        CART_ADDED(1, "添加购物车"), ORDER_ADDED(2, "下单"), PAID(3, "付款");

        UserItemOperation(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        private int    code;
        private String msg;

        @Override
        public int getCode() {
            return this.code;
        }

        @Override
        public String getMsg() {
            return this.msg;
        }

    }

    public enum LiveUpdateType implements BaseEnum{
        LIVE_UPDATE(1,"直播更新")
        ,LIVE_ONLINE_UPDATE(1,"直播上下线更新")
        ,LIVE_GENERALWEIGHT_UPDATE(2,"直播通用权中更新")
        ,LIVE_AUTOONLINE_UPDATE(3,"自动上下线");

        LiveUpdateType(int code, String msg){
            this.code = code;
            this.msg = msg;
        }
        private int    code;
        private String msg;

        @Override
        public int getCode() {
            return this.code;
        }

        @Override
        public String getMsg() {
            return this.msg;
        }
    }

    public enum LiveCreateType implements BaseEnum{
        LIVE_CREATE(1,"直播创建"),LIVE_PLAYBACK_CREATE(2,"直播回放创建");

        LiveCreateType(int code,String msg){
            this.code = code;
            this.msg = msg;
        }
        private int    code;
        private String msg;

        @Override
        public int getCode() {
            return this.code;
        }

        @Override
        public String getMsg() {
            return this.msg;
        }
    }

    public enum TabType implements BaseEnum{
        NORAMAL(1,"普通类型"),USER_DEFINED(2,"自定义");

        TabType(int code,String msg){
            this.code = code;
            this.msg = msg;
        }
        private int    code;
        private String msg;

        @Override
        public int getCode() {
            return this.code;
        }

        @Override
        public String getMsg() {
            return this.msg;
        }
    }

    public enum TabStatus implements BaseEnum {
        DELETE_STATUS(-1, "已删除"), NORMAL_STATUS(0, "正常");

        int    code;
        String msg;

        TabStatus(int code, String msg) {
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

