package cn.idongjia.live.restructure.query;

import cn.idongjia.gem.lib.pojo.ItemPext;
import cn.idongjia.gem.lib.query.ItemSearch;
import cn.idongjia.live.restructure.manager.GemManager;
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
public class ItemQueryHandler {
    @Resource
    private GemManager gemManager;

    public List<ItemPext> list(List<Long> itemIds) {
        return gemManager.takeBatchItem(itemIds);
    }

    public Map<Long, ItemPext> assembleMap(List<Long> itemIds) {
        List<ItemPext> itemPexts = gemManager.takeBatchItem(itemIds);
        return itemPexts.stream().collect(Collectors.toMap(ItemPext::getIid, v1 -> v1, (v1, v2) -> v1));
    }

    public List<ItemPext> list(ItemSearch itemSearch) {
        return gemManager.getItems(itemSearch);
    }


}
