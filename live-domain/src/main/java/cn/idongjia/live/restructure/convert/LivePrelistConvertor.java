package cn.idongjia.live.restructure.convert;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.divine.lib.pojo.response.live.general.GeneralLiveCO;
import cn.idongjia.live.pojo.live.LivePre;
import cn.idongjia.live.support.DateTimeUtil;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/17
 * Time: 上午11:34
 */
public class LivePrelistConvertor{


    public static LivePre liveShow2LivePre(User user, Long asid, Boolean subscribed, GeneralLiveCO generalLiveCO){
        LivePre livePre =new LivePre();
        livePre.setAsid(asid);
        livePre.setDate(DateTimeUtil.getLong2Date(generalLiveCO.getPreStartTime()));
        livePre.setTime(DateTimeUtil.getLong2Time(generalLiveCO.getPreStartTime()));
        livePre.setHavatar(user.getAvatar());
        livePre.setHtitle(user.getTitle());
        livePre.setPreStartTime(generalLiveCO.getPreStartTime());
        livePre.setTitle(generalLiveCO.getTitle());
        livePre.setSubscribed(subscribed);
        livePre.setLiveId(generalLiveCO.getId());
        return livePre;

    }
}
