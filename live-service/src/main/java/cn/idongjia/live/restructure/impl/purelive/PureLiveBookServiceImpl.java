package cn.idongjia.live.restructure.impl.purelive;

import cn.idongjia.live.api.purelive.PureLiveBookService;
import cn.idongjia.live.pojo.purelive.book.AnchorsBookDO;
import cn.idongjia.live.pojo.purelive.book.PureLiveBookDO;
import cn.idongjia.live.pojo.user.Anchor;
import cn.idongjia.live.query.purelive.book.AnchorsBookSearch;
import cn.idongjia.live.query.purelive.book.PureLiveBookSearch;
import cn.idongjia.live.restructure.biz.AnchorBookBO;
import cn.idongjia.live.restructure.biz.LiveBookBO;
import cn.idongjia.live.restructure.exception.PureLiveException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static cn.idongjia.live.restructure.exception.PureLiveException.LIVE_BOOK_LID_MISS;
import static cn.idongjia.live.restructure.exception.PureLiveException.LIVE_BOOK_UID_MISS;
import static cn.idongjia.util.Asserts.assertNotNull;

@Component("pureLiveBookServiceImpl")
public class PureLiveBookServiceImpl implements PureLiveBookService {

    @Resource
    private LiveBookBO liveBookBO;

    @Resource
    private AnchorBookBO anchorsBookBO;

    @Override
    public Long addPureLiveBook(PureLiveBookDO pureLiveBookDO){
        assertNotNull(pureLiveBookDO.getUid(), LIVE_BOOK_UID_MISS);
        assertNotNull(pureLiveBookDO.getLid(), LIVE_BOOK_LID_MISS);
        return liveBookBO.addPureLiveBook(pureLiveBookDO);
    }

    @Override
    public boolean deletePureLiveBook(PureLiveBookDO pureLiveBookDO){
        if(pureLiveBookDO.getUid() == null || pureLiveBookDO.getLid() == null){
            return false;
        }
        return liveBookBO.delete(pureLiveBookDO);


    }

    @Override
    public boolean updatePureLiveBook(PureLiveBookDO pureLiveBookDO){
//        if(pureLiveBookDO.getUserId() == null || pureLiveBookDO.getLid() == null){
//            return false;
//        }
//        return liveBookBO.PureLiveBook(pureLiveBookDO);
        return true;
    }

    @Override
    public List<PureLiveBookDO> listPureLiveBooks(PureLiveBookSearch pureLiveBookSearch){
        return liveBookBO.list(pureLiveBookSearch);
    }

    @Override
    public Integer countPureLiveBooks(PureLiveBookSearch pureLiveBookSearch){
        return liveBookBO.count(pureLiveBookSearch);
    }

    @Override
    public Long addAnchorsBook(AnchorsBookDO anchorsBookDO){
        assertNotNull(anchorsBookDO.getUid(), LIVE_BOOK_UID_MISS);
        assertNotNull(anchorsBookDO.getAnchorId(), PureLiveException.LIVE_BOOK_CUID_MISS);
        return anchorsBookBO.addAnchorBook(anchorsBookDO);
    }

    @Override
    public boolean deleteAnchorsBook(AnchorsBookDO anchorsBookDO){
        if(anchorsBookDO.getUid() == null || anchorsBookDO.getAnchorId() == null){
            return false;
        }
        return anchorsBookBO.delAnchorBook(anchorsBookDO);
    }

    @Override
    public boolean updateAnchorsBook(AnchorsBookDO anchorsBookDO){
//        return !(anchorsBookDO.getUserId() == null || anchorsBookDO.getAnchorId() == null)
//                && pureLiveBookRepo.updateAnchorsBook(anchorsBookDO);
        return false;
    }

    @Override
    public List<AnchorsBookDO> listAnchorsBooks(AnchorsBookSearch anchorsBookSearch){
        return anchorsBookBO.list(anchorsBookSearch);
    }

    @Override
    public Integer countAnchorsBooks(AnchorsBookSearch anchorsBookSearch){
        return anchorsBookBO.count(anchorsBookSearch);
    }

    @Override
    public boolean batchAddPureLiveBook(Long cuid, Long lid){
//        return !(cuid == null || lid == null) && pureLiveBookRepo.batchAddPureLiveBook(cuid, lid);
        return false;
    }

    @Override
    public boolean batchRemovePureLiveBook(Long cuid, Long lid){
//        return !(cuid == null || lid == null) && pureLiveBookRepo.batchRemovePureLiveBook(cuid, lid);
        return false;
    }

    @Override
    public List<Long> listBookUidsByLid(Long lid){
        assertNotNull(lid, LIVE_BOOK_LID_MISS);
        return liveBookBO.listUidsByLid(lid);
    }

    //SELECT u.userId,u.avatar,u.username,f.createtm FROM kp_fans f LEFT JOIN kp_user u ON f.fansid=u.userId WHERE u.status=4 AND f.userId=:userId
    @Override
    public List<Anchor> listAnchorsByUid(Long uid, AnchorsBookSearch anchorsBookSearch){
        return anchorsBookBO.listAnchorsByUid(uid, anchorsBookSearch);
    }

    @Override
    public List<Long> listPureLivesByUid(Long uid, PureLiveBookSearch pureLiveBookSearch){
        assertNotNull(uid, LIVE_BOOK_UID_MISS);
//        return liveBookBO.listPureLivesByUid(uid, pureLiveBookSearch);
        return null;
    }

    @Override
    public Integer countPureLivesByUid(Long uid) {
        assertNotNull(uid, LIVE_BOOK_UID_MISS);
//        return liveBookBO.countPureLiveByUid(uid);
        return null;
    }

    @Override
    public Integer countAnchorsByUid(Long uid) {
        assertNotNull(uid, LIVE_BOOK_UID_MISS);
//        return liveBookBO.countAnchorByUid(uid);
        return null;
    }

    @Override
    public Map<String, Object> countBooksByUid(Long uid) {
        assertNotNull(uid, LIVE_BOOK_UID_MISS);
//        return liveBookBO.countBooksByUid(uid);
        return null;
    }

    @Override
    public Map<String, String> getLiveRemindTxt(Long uid, Long anchorId) {
        return liveBookBO.acquireRemindTxt(uid, anchorId);
    }
}
