package cn.idongjia.live.restructure.manager;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.push.api.NotifyService;
import cn.idongjia.push.pojo.NotifyBase;
import cn.idongjia.push.pojo.NotifyExtra;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Component
public class NotifyManager {

    @Resource
    private NotifyService notifyService;
    @Resource
    private ConfigManager configManager;
    @Resource
    private UserManager userManager;

    private static final Log LOGGER = LogFactory.getLog(NotifyManager.class);

    /**
     * 发送推送消息
     * @param uids 被发送人ID
     * @param Huid 主播ID
     * @param liveTitle 直播标题
     * @param liveId 直播ID
     * @param livePic 直播图片
     */
    public void sendNotify(List<Long> uids, Long Huid, String liveTitle, Long liveId, String livePic,Integer liveType){
        if (uids.size() == 0){
            return;
        }
        //构建推送基本信息
        NotifyBase notifyBase = new NotifyBase();
        notifyBase.setUids(uids);
        User user = userManager.getUser(Huid);
        String pushContent = String.format(configManager.getPushContent(), liveTitle);
        notifyBase.setContent(pushContent);
        String infoContent = String.format(configManager.getInfoContent(), user.getUsername(), liveTitle);
        notifyBase.setInfoContent(infoContent);
        notifyBase.setIcon(livePic);
        //构建推送额外信息
        NotifyExtra notifyExtra = new NotifyExtra();
        notifyExtra.set_tp(NotifyExtra.MsgTp.NOTIFICATION.getStrValue());
        notifyExtra.setDjaddr(liveId.toString());
        if(Objects.equals(LiveEnum.LiveType.LIVE_AUCTION.getCode(),liveType.intValue())){
            notifyExtra.setDjtype(String.valueOf(LiveConst.TYPE_JUMP_LIVE_AUCTION));
        }else {
            notifyExtra.setDjtype(String.valueOf(LiveConst.TYPE_JUMP_LIVE));
        }

        try {
            notifyService.notifyWithExtra(notifyBase, notifyExtra, true, false);
        }catch (Exception e){
            LOGGER.warn("推送服务失败" + e.getMessage());
            throw new LiveException(-12138, "调用推送服务失败");
        }
    }

}
