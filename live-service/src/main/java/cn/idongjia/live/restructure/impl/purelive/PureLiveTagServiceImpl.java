package cn.idongjia.live.restructure.impl.purelive;

import cn.idongjia.live.api.purelive.PureLiveTagService;
import cn.idongjia.live.pojo.BaseList;
import cn.idongjia.live.pojo.purelive.tag.ColumnTag;
import cn.idongjia.live.pojo.purelive.tag.PureLiveTagDO;
import cn.idongjia.live.pojo.purelive.tag.PureLiveTagRelDO;
import cn.idongjia.live.pojo.purelive.tag.TemplateTagRelDO;
import cn.idongjia.live.query.purelive.tag.PureLiveTagRelSearch;
import cn.idongjia.live.query.purelive.tag.PureLiveTagSearch;
import cn.idongjia.live.restructure.biz.LiveTagBO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.BeanParam;
import java.util.List;
import java.util.Map;

import static cn.idongjia.live.restructure.exception.PureLiveException.LIVE_TAG_NAME_MISS;
import static cn.idongjia.live.restructure.exception.PureLiveException.LIVE_TAG_TYPE_MISS;
import static cn.idongjia.util.Asserts.assertNotNull;

@Component("pureLiveTagServiceImpl")
public class PureLiveTagServiceImpl implements PureLiveTagService {

    @Resource
    private LiveTagBO liveTagBO;

    @Override
    public Long addPureLiveTag(PureLiveTagDO pureLiveTagDO) {
        assertNotNull(pureLiveTagDO.getName(), LIVE_TAG_NAME_MISS);
        assertNotNull(pureLiveTagDO.getType(), LIVE_TAG_TYPE_MISS);
        return liveTagBO.addTag(pureLiveTagDO);
    }

    @Override
    public boolean deletePureLiveTag(Long tagId) {
        return tagId != null && liveTagBO.deleteLiveTag(tagId);
    }

    @Override
    public boolean updatePureLiveTag(Long tagId, PureLiveTagDO pureLiveTagDO) {
        return tagId != null && liveTagBO.updateLiveTag(tagId, pureLiveTagDO);
    }

    @Override
    public PureLiveTagDO getPureLiveTag(Long tagId) {
        return liveTagBO.get(tagId);
    }

    @Override
    public List<PureLiveTagDO> listPureLiveTags(PureLiveTagSearch pureLiveTagSearch) {
        return liveTagBO.list(pureLiveTagSearch);
    }

    @Override
    public Integer countPureLiveTags(PureLiveTagSearch pureLiveTagSearch) {
        return liveTagBO.count(pureLiveTagSearch);
    }

    @Override
    public BaseList<PureLiveTagDO> listPureLiveTagsWithCount(PureLiveTagSearch tagSearch) {
        return liveTagBO.page(tagSearch);
    }

    @Override
    public List<PureLiveTagDO> listPureLiveTagWithRels(@BeanParam PureLiveTagSearch tagSearch) {
        return liveTagBO.listWithoutFinished(tagSearch);
    }

    @Override
    public Long addPureLiveTagRel(PureLiveTagRelDO pureLiveTagRelDO) {
        return liveTagBO.addPureLiveTagRel(pureLiveTagRelDO);
    }

    @Override
    public boolean updatePureLiveTagRel(PureLiveTagRelDO pureLiveTagRelDO) {
        return !(pureLiveTagRelDO.getTagId() == null || pureLiveTagRelDO.getLid() == null)
                && liveTagBO.updateLiveTagRel(pureLiveTagRelDO);
    }

    @Override
    public boolean deletePureLiveTagRel(PureLiveTagRelDO pureLiveTagRelDO) {
        return !(pureLiveTagRelDO.getTagId() == null || pureLiveTagRelDO.getLid() == null)
                && liveTagBO.deleteLiveTagRel(pureLiveTagRelDO);
    }

    @Override
    public List<PureLiveTagRelDO> listPureLiveTagRels(PureLiveTagRelSearch pureLiveTagRelSearch) {
        return liveTagBO.list(pureLiveTagRelSearch);
    }

    @Override
    public Integer countPureLiveTagRels(PureLiveTagRelSearch pureLiveTagRelSearch) {
        return liveTagBO.count(pureLiveTagRelSearch);
    }

    @Override
    public BaseList<PureLiveTagRelDO> listPureLiveTagRelsWithCount(@BeanParam PureLiveTagRelSearch tagRelSearch) {
        return liveTagBO.page(tagRelSearch);
    }

    @Override
    public Long addTemplateTagRel(TemplateTagRelDO tagRelDO) {
//        return pureLiveTagRepo.addTemplateTagRel(tagRelDO);
        return null;
    }

    @Override
    public boolean deleteTemplateTagRel(TemplateTagRelDO tagRelDO) {
//        return pureLiveTagRepo.deleteTemplateTagRel(tagRelDO);
        return false;
    }

    @Override
    public boolean updateTemplateTagRel(TemplateTagRelDO tagRelDO) {
//        return !(tagRelDO.getTagId() == null || tagRelDO.getUrl() == null || tagRelDO.getUrl().equals(""))
//                && pureLiveTagRepo.replaceTemplateTagRel(tagRelDO);
        return false;
    }

    @Override
    public TemplateTagRelDO getTemplateTagRel(Long tagId) {
        return liveTagBO.getTemplateTagRel(tagId);
    }

    @Override
    public List<Long> listPureLivesByTagId(Long tagId, PureLiveTagRelSearch pureLiveTagRelSearch) {
        return liveTagBO.list(tagId, pureLiveTagRelSearch);
    }

    @Override
    public Map<String, String> listTemplatesByTagId(Long tagId) {
        return liveTagBO.mapTemplatesById(tagId);
    }

    @Override
    public List<PureLiveTagRelDO> listTypeTagRelsiByLid(Long lid) {
        return liveTagBO.listByLiveId(lid);
    }

    @Override
    public ColumnTag getColumnTagById(Long tid) {
        return liveTagBO.getColumnTag(tid);
    }


}
