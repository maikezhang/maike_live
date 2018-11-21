package cn.idongjia.live.restructure.convert;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.live.pojo.purelive.PureLive4Article;
import cn.idongjia.live.restructure.domain.entity.live.LiveEntity;
import cn.idongjia.live.restructure.dto.LivePureDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.dto.PureLive4ArticleDTO;
import org.springframework.stereotype.Component;

/**
 * @author lc
 * @create at 2018/7/17.
 */
@Component("pureLie4ArticleConvertor")
public class PureLie4ArticleConvertor implements ConvertorI<PureLive4Article ,LiveEntity,PureLive4ArticleDTO> {
    @Override
    public PureLive4Article dataToClient(PureLive4ArticleDTO pureLive4ArticleDTO) {
        LivePureDTO livePureDTO = pureLive4ArticleDTO.getLivePureDTO();
        LiveShowDTO liveShowDTO = pureLive4ArticleDTO.getLiveShowDTO();
        User user = pureLive4ArticleDTO.getUser();
        PureLive4Article pureLive4Article = new PureLive4Article();
        pureLive4Article.setPlid(liveShowDTO.getId());
        pureLive4Article.setCover(livePureDTO == null ? null : livePureDTO.getPic());
        pureLive4Article.setTitle(liveShowDTO.getTitle());
        pureLive4Article.setAlState(liveShowDTO.getState());
        pureLive4Article.setzUserName(user.getUsername());
        pureLive4Article.setzAvatar(user.getAvatar());
        pureLive4Article.setZctf(liveShowDTO.getTitle());
        pureLive4Article.setzUid(user.getUid());
        pureLive4Article.setCreateTm(liveShowDTO.getCreateTime());
        return pureLive4Article;
    }
}
