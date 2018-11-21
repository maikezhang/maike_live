package cn.idongjia.live.restructure.enums;

import cn.idongjia.live.support.BaseEnum;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/2
 * Time: 下午5:07
 */
public class AnchorBlackWhiteEnum {

    public enum AnchorBlackWhiteType implements BaseEnum{

        NO_PLAY_TYPE(0,"都不显示"),
        ONLY_APP_TYPE(1,"APP显示"),
        ONLY_MP_TYPE(2,"小程序显示"),
        MP_APP_TYPE(3,"小程序、app显示");

        AnchorBlackWhiteType(int code,String msg){
            this.code=code;
            this.msg=msg;
        }

        private int code;
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


    public enum BlackWhite implements BaseEnum{

        BLACK(1,"黑名单"),
        WHITE(2,"白名单");

        BlackWhite(int code,String msg){
            this.code=code;
            this.msg=msg;
        }

        private int code;
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



    public enum AnchorBlackWhiteStatus implements BaseEnum{

        DELETE_STATUS(-1,"删除"),
        NORMAL_STATUS(0,"正常");

        private int code;
        private String msg;

        AnchorBlackWhiteStatus(int code,String msg){
            this.code=code;
            this.msg=msg;
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
