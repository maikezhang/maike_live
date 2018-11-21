package cn.idongjia.live.restructure.convert;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.live.pojo.purelive.ForeShow;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.dto.ForeShowDTO;
import cn.idongjia.live.restructure.dto.LivePureDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.support.LiveConst;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lc
 * @create at 2018/7/17.
 */
@Component("foreShowConvertor")
public class ForeShowConvertor implements ConvertorI<ForeShow, LiveEntity, ForeShowDTO> {
    @Override
    public ForeShow dataToClient(ForeShowDTO foreShowDTO) {
        LivePureDTO livePureDTO = foreShowDTO.getLivePureDTO();
        LiveShowDTO liveShowDTO = foreShowDTO.getLiveShowDTO();
        boolean isBook = foreShowDTO.isBook();
        User user = foreShowDTO.getUser();
        ForeShow foreShow = new ForeShow();

        //组装主播信息
        foreShow.setHavatar(user.getAvatar());
        foreShow.setHtitle(user.getTitle());
        foreShow.setHusername(user.getUsername());

        //组装直播信息
        foreShow.setLid(liveShowDTO.getId());
        foreShow.setTitle(liveShowDTO.getTitle());
        foreShow.setDesc(livePureDTO.getDesc());
        foreShow.setStarttm(liveShowDTO.getEstimatedStartTime());
        foreShow.setIsBooked(isBook ? LiveConst.STATUS_ANCHOR_BOOKED : LiveConst.STATUS_ANCHOR_UNBOOKED);


        //组装时间信息
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        foreShow.setStartDate(sdf.format(new Date(foreShow.getStarttm())));
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
        foreShow.setStartTime(sdfTime.format(new Date(foreShow.getStarttm())));
        return foreShow;
    }

}
