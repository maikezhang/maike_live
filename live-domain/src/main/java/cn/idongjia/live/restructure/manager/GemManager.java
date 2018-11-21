package cn.idongjia.live.restructure.manager;

import cn.idongjia.gem.lib.pojo.Item;
import cn.idongjia.gem.lib.pojo.ItemPext;
import cn.idongjia.gem.lib.query.ItemSearch;
import cn.idongjia.gem.lib.service.ItemService;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhang created on 2018/1/18 下午3:32
 * @version 1.0
 */
@Component
public class GemManager {

    private static final Log LOGGER = LogFactory.getLog(GemManager.class);

    @Resource
    private ItemService itemService;

    public Map<Long, ItemPext> takeBatchItemMap(List<Long> itemIds) {
        if (itemIds == null || itemIds.size() == 0) {
            return Collections.EMPTY_MAP;
        }
        Set<ItemPext> itemPexts = takeBatchItem(new HashSet<>(itemIds));
        Map<Long, ItemPext> result = itemPexts.stream()
                .collect(Collectors.toMap(x -> x.getIid(), x -> x, (x1, x2) -> x1));
        return result;
    }

    /**
     * 批量获取商品信息
     *
     * @param itemIds 商品id集合
     * @return 商品信息集合
     */
    public Set<ItemPext> takeBatchItem(Set<Long> itemIds) {
        if (Utils.isEmpty(itemIds)) {
            return new HashSet<>();
        }
        try {
            return new HashSet<>(itemService.batchGet(new ArrayList<>(itemIds)));
        } catch (Throwable t) {
            GemManager.LOGGER.warn("调用cn.idongjia.gem.lib.service.ItemService#batchGet失败: " + itemIds);
            return new HashSet<>();
        }
    }

    public List<ItemPext> takeBatchItem(List<Long> itemIds) {
        if (Utils.isEmpty(itemIds)) {
            return new ArrayList<>();
        }
        try {
            return new ArrayList<>(itemService.batchGet(new ArrayList<>(itemIds)));
        } catch (Throwable t) {
            GemManager.LOGGER.warn("调用cn.idongjia.gem.lib.service.ItemService#batchGet失败: " + itemIds);
            return new ArrayList<>();
        }
    }

    public Optional<ItemPext> takeItem(Long id) {
        if (Objects.isNull(id)) {
            return Optional.empty();
        }
        try {
            ItemPext itemPext = itemService.get(id);
            return Optional.ofNullable(itemPext);
        } catch (Exception e) {
            GemManager.LOGGER.warn("cn.idongjia.gem.lib.service.ItemService.get失败 {}", id, e);
            return Optional.empty();
        }
    }

    /**
     * 匠人店铺
     *
     * @param search
     * @return
     */
    public List<ItemPext> craftmans(ItemSearch search) {
        if (search == null) {
            return Arrays.asList();
        }
        try {
            search.setStatus(Item.Status.ITEM_ONSHELF.get());
            List<ItemPext> itemPexts = itemService.craftsmans(search);
            return itemPexts == null ? Arrays.asList() : itemPexts;
        } catch (Exception e) {
            GemManager.LOGGER.warn("cn.idongjia.gem.lib.service.ItemService.craftsmans调用失败", e);
            return Arrays.asList();
        }
    }

    public List<ItemPext> getItems(ItemSearch search) {
        if (search == null) {
            return Collections.EMPTY_LIST;
        }
        try {
            List<ItemPext> itemPexts = itemService.getAll(search);
            return itemPexts == null ? Collections.EMPTY_LIST : itemPexts;
        } catch (Exception e) {
            GemManager.LOGGER.warn("调用cn.idongjia.gem.lib.service.ItemService.getAll失败", e);
            return Collections.EMPTY_LIST;
        }
    }

}
