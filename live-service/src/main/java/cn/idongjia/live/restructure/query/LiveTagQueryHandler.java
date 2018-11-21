package cn.idongjia.live.restructure.query;

import cn.idongjia.live.db.mybatis.query.DBLiveTagQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveTagRelQuery;
import cn.idongjia.live.restructure.dto.LiveTagDTO;
import cn.idongjia.live.restructure.dto.LiveTagRelDTO;
import cn.idongjia.live.restructure.dto.TemplateTagRelDTO;
import cn.idongjia.live.restructure.repo.LiveTagRepo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/6/11.
 */
@Component
public class LiveTagQueryHandler {
    @Resource
    private LiveTagRepo liveTagRepo;


    @Async
    public Future<Map<Long, LiveTagRelDTO>> map(List<Long> liveIds) {
        DBLiveTagRelQuery dbLiveTagRelQuery = DBLiveTagRelQuery.builder().liveIds(liveIds).build();
        List<LiveTagRelDTO> pureLiveTagDTOS = liveTagRepo.list(dbLiveTagRelQuery);
        Map<Long, LiveTagRelDTO> liveRelTagMap = pureLiveTagDTOS.stream().collect(Collectors.toMap(LiveTagRelDTO::getId, v1 -> v1, (v1, v2) -> v1));
        return new AsyncResult<>(liveRelTagMap);
    }


    @Async
    public Future<Map<Long, LiveTagDTO>> map(DBLiveTagQuery dbLiveTagQuery) {
        List<LiveTagDTO> pureLiveTagDTOS = liveTagRepo.listTags(dbLiveTagQuery);
        Map<Long, LiveTagDTO> liveTagDTOMap = pureLiveTagDTOS.stream().collect(Collectors.toMap(LiveTagDTO::getId, v1 -> v1, (v1, v2) -> v1));
        return new AsyncResult<>(liveTagDTOMap);
    }

    @Async
    public Future<List<LiveTagDTO>> list(DBLiveTagQuery dbPureLiveTagQuery) {
        List<LiveTagDTO> pureLiveTagDTOS = liveTagRepo.listTags(dbPureLiveTagQuery);
        return new AsyncResult<>(pureLiveTagDTOS);
    }

    @Async
    public Future<List<LiveTagRelDTO>> list(DBLiveTagRelQuery dbLiveTagRelQuery) {
        List<LiveTagRelDTO> pureLiveTagDTOS = liveTagRepo.list(dbLiveTagRelQuery);
        return new AsyncResult<>(pureLiveTagDTOS);
    }

    public LiveTagDTO get(Long tagId) {
        return liveTagRepo.getTagById(tagId);
    }

    public Integer count(DBLiveTagQuery dbLiveTagQuery) {
        return liveTagRepo.count(dbLiveTagQuery);
    }

    public Integer count(DBLiveTagRelQuery dbLiveTagRelQuery) {
        return liveTagRepo.count(dbLiveTagRelQuery);
    }

    public TemplateTagRelDTO getTemplateTagRel(long tagId) {
        return liveTagRepo.getTemplateTagRel(tagId);
    }
}
