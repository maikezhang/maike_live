package cn.idongjia.live.support;

public final class CloudClientConst {

    private CloudClientConst(){}

    public static final String RTMP_PREFIX = "rtmp://";
    public static final String HTTP_PREFIX = "http://";
    public static final String FLV_SUFFIX = ".flv";
    public static final String HLS_SUFFIX = ".m3u8";

    public enum CloudType{
    		/**
    		 * QCloudClient
    		 */
        QCLOUD(1, "qcloud.QCloudClient"),
        /**
         * VCloudClient
         */
        VCLOUD(2, "vcloud.VCloudClient"),
        /**
         * DCloudClient
         */
        DCLOUD(3, "dcloud.DCloudClient");


        private String clientName;
        private int clientCode;
        CloudType(int clientCode, String clientName) {
            this.clientName = clientName;
            this.clientCode = clientCode;
        }
        public String getClientName(){
            return this.clientName;
        }
        public int getClientCode(){return this.clientCode;}
    }

    public static final int FLV_FORMAT = 1;
    public static final int MP4_FORMAT = 0;
}
