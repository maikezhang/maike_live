package cn.idongjia.live.restructure.cloud;

import cn.idongjia.live.restructure.cloud.api.CloudClient;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.support.CloudClientConst;

public class CloudClientFactory {

    private static final String BASE_PACKAGE = "cn.idongjia.live.restructure.cloud";

    /**
     * 根据直播云类型选取对应的直播云客户端
     * @param cloudType 直播云类型
     * @return 直播云客户端实例
     */
    public static CloudClient getClientInstance(int cloudType){
        try {
            Class clientType = Class.forName(BASE_PACKAGE + "." + getCloudType(cloudType).getClientName());
            return (CloudClient) clientType.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            throw new LiveException(-12138, "获取直播云客户端失败");
        }
    }

    /**
     * 转化类型
     * @param type 类型
     * @return CloudType
     */
    private static CloudClientConst.CloudType getCloudType(int type){
        CloudClientConst.CloudType cloudType;
        switch (type){
            case 1:
                cloudType = CloudClientConst.CloudType.valueOf("QCLOUD");
                break;
            case 2:
                cloudType = CloudClientConst.CloudType.valueOf("VCLOUD");
                break;
            case 3:
                cloudType = CloudClientConst.CloudType.valueOf("DCLOUD");
                break;
            default:
                throw new LiveException(-12138, "暂时不支持该云类型");
        }
        return cloudType;
    }
}
