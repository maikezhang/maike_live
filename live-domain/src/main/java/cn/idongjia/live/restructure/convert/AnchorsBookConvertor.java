package cn.idongjia.live.restructure.convert;

import cn.idongjia.live.db.mybatis.po.AnchorBookPO;
import cn.idongjia.live.pojo.purelive.book.AnchorsBookDO;
import cn.idongjia.live.restructure.domain.entity.user.LiveAnchor;
import org.springframework.stereotype.Component;

/**
 * @author lc
 * @create at 2018/7/19.
 */
@Component("anchorsBookConvertor")
public class AnchorsBookConvertor implements ConvertorI<AnchorsBookDO,LiveAnchor,AnchorBookPO> {
    @Override
    public AnchorsBookDO dataToClient(AnchorBookPO anchorBookPO) {
        AnchorsBookDO anchorsBookDO = new AnchorsBookDO();
        anchorsBookDO.setAnchorId(anchorBookPO.getAnchorId());
        anchorsBookDO.setCreateTm(anchorBookPO.getCreateTime());
        anchorsBookDO.setId(anchorBookPO.getId());
        anchorsBookDO.setModifiedTm(anchorBookPO.getModifiedTime());
        anchorsBookDO.setStatus(anchorBookPO.getStatus());
        anchorsBookDO.setUid(anchorBookPO.getUserId());
        return anchorsBookDO;
    }
}
