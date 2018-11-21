package cn.idongjia.live.restructure.convert;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.live.pojo.live.LiveShow4Article;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.dto.LiveShow4ArticleDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import org.springframework.stereotype.Component;

/**
 * @author lc
 * @create at 2018/7/17.
 */
@Component("liveShow4ArticleConvert")
public class LiveShow4ArticleConvert implements ConvertorI<LiveShow4Article, LiveEntity, LiveShow4ArticleDTO> {
    @Override
    public LiveShow4Article dataToClient(LiveShow4ArticleDTO liveShow4ArticleDTO) {
        LiveShowDTO liveShowDTO = liveShow4ArticleDTO.getLiveShowDTO();
        User user = liveShow4ArticleDTO.getUser();
        LiveShow4Article liveShow4Article = new LiveShow4Article();
        liveShow4Article.setzUid(liveShowDTO.getUserId());
        liveShow4Article.setzUserName(user.getUsername());
        liveShow4Article.setzAvatar(user.getAvatar());
        liveShow4Article.setAlState(liveShowDTO.getState());
        return liveShow4Article;
    }
}
