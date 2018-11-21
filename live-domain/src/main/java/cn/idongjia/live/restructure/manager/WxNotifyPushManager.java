package cn.idongjia.live.restructure.manager;

import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.push.pojo.wx.MPRequest;
import cn.idongjia.push.service.WxPushService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/8
 * Time: 下午2:16
 */
@Component
public class WxNotifyPushManager {


    @Resource
    private WxPushService wxPushService;

    private static final Log LOGGER= LogFactory.getLog(WxNotifyPushManager.class);



    public void sendWxMpNotify(String appId, MPRequest mpRequest){
        try {
            wxPushService.mpNotify(appId,mpRequest);

            LOGGER.info("推送数据：appId:{},mqrequest:{}",appId,mpRequest);
        }catch (Exception e){
            LOGGER.error("小程序推送失败,推送消息：appId:{},mpRequest:{} ,exception:{}",appId,mpRequest,e);
        }

    }

}
