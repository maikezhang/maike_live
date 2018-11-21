package cn.idongjia.live.restructure.manager;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.live.db.mybatis.po.LiveResourcePO;
import cn.idongjia.live.db.mybatis.po.LiveShowPO;
import cn.idongjia.live.db.mybatis.query.DBLiveResourceQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveShowQuery;
import cn.idongjia.live.pojo.live.LiveEnum;
import cn.idongjia.live.query.purelive.PureLiveDetailSearch;
import cn.idongjia.live.restructure.dto.LiveResourceDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.live.support.AssembleProto;
import cn.idongjia.live.support.ObjectUtils;
import cn.idongjia.live.support.enumeration.LiveResourceType;
import cn.idongjia.live.restructure.repo.LiveResourceRepo;
import cn.idongjia.live.restructure.repo.LiveShowRepo;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.zoo.proto.ZooProto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static cn.idongjia.live.support.LiveConst.STATUS_DETAIL_NORMAL;

/**
 * 如果用户加购……商品关联了直播间则发推送
 *
 * @author 岳晓东
 * @date 2018/04/12
 */
@Component
public class ItemOperationPushManager {

    private static final Log LOGGER = LogFactory.getLog(ItemOperationPushManager.class);


    @Resource
    private LiveShowRepo liveShowRepo;
    @Resource
    private LiveResourceRepo liveResourceRepo;
    @Resource
    private UserManager userManager;
    @Resource
    private MqProducerManager mqProducerManager;
    @Resource
    private TaskManager taskManager;

    public void pushPaidOperation(Long userId, Long itemId) {
        String info = "付款成功";
        pushUserItemOption(userId, LiveEnum.UserItemOperation.PAID, itemId, info);
        taskManager.setItemPaidRepush(itemId, userId);
    }

    public void pushCartAddedOperation(Long userId, Long itemId) {
        String info = "加入购物车";
        pushUserItemOption(userId, LiveEnum.UserItemOperation.CART_ADDED, itemId, info);
    }


    public void pushOrderAddedOperation(Long userId, Long itemId) {
        String info = "下单成功";
        pushUserItemOption(userId, LiveEnum.UserItemOperation.ORDER_ADDED, itemId, info);
    }

    public void repushPaidOperation(Long userId, Long itemId) {
        String info = "刚刚付款成功";
        pushUserItemOption(userId, LiveEnum.UserItemOperation.PAID, itemId, info);
    }

    /**
     * 推送到聊天室
     */
    public void pushUserItemOption(Long userId, LiveEnum.UserItemOperation operation, Long itemId,
                                   String info) {
        // 1. 判断商品是否关联直播
        PureLiveDetailSearch search = new PureLiveDetailSearch();
        search.setResourceId(itemId);
        search.setResourceType(LiveResourceType.ITEM.getCode());
        search.setStatus(LiveConst.STATUS_DETAIL_NORMAL);

        DBLiveResourceQuery dbLiveResourceQuery = DBLiveResourceQuery.builder()
                .status(STATUS_DETAIL_NORMAL)
                .resourceType(LiveResourceType.ITEM.getCode())
                .resourceId(itemId)
                .build();
        List<LiveResourcePO> liveResourcePOS=new ArrayList<>();
        try{
            liveResourcePOS= liveResourceRepo.list(dbLiveResourceQuery)
                    .stream().map(LiveResourceDTO::toDO).collect(Collectors.toList());

        }catch (Exception e){
            LOGGER.info("直播资源查询失败+{}",e);
            throw LiveException.failure("直播资源查询失败");
        }
//        List<PureLiveDetailDO> resources = pureLiveDetailRepo.listResources(search);
        if (ObjectUtils.isEmptyList(liveResourcePOS)) {
            LOGGER.info("加购提示 商品未关联直播 itemId={}", itemId);
            return;
        }
        List<Long> liveIds = liveResourcePOS.stream().map(x -> x.getLiveId()).collect(Collectors.toList());
        // 2. 获取zid
        //正在直播发推送

        DBLiveShowQuery query=DBLiveShowQuery.builder()
                .states(Arrays.asList(LiveEnum.LiveState.PLAYING.getCode())).ids(liveIds).build();
        List<LiveShowPO> liveShowPOS=new ArrayList<>();
        try {
            liveShowPOS=liveShowRepo.listLiveShows(query)
                    .stream().map(LiveShowDTO::toDO).collect(Collectors.toList());
        }catch(Exception e){
            LOGGER.info("直播资源查询失败+{}",e);
            throw LiveException.failure("直播资源查询失败");
        }

        if (ObjectUtils.isEmptyList(liveShowPOS)) {
            LOGGER.info("加购提示 未找到符合条件直播 liveIds={}", liveIds);
            return;
        }
        List<Long> zids = liveShowPOS.stream().map(x -> x.getZooId()).collect(Collectors.toList());
        // 3. 推送
        User user = userManager.getUserSafe(userId);
        if (user == null) {
            LOGGER.info("加购提示 用户不存在 userId={}", userId);
            return;
        }
        String fuzzyName = buildFuzzyName(user.getUsername());
        ZooProto.ZooPack pack = AssembleProto.assembleUserItemOperationPack(info, operation.getCode(),
                fuzzyName);
        zids.forEach(zid -> {
            mqProducerManager.broadCastMessage2Zoo(zid, pack);
        });
    }

    /**
     * 根据规则模糊姓名
     * <p>
     * 用户名显示：用户名为一个字时，显示为“用户名**”；
     * <p>
     * 用户名为两个字和三个字时，显示“用户名第一个字*用户名最后一个字”；
     * <p>
     * 用户名为三个字以上时，显示为“用户名第一个字***用户名最后一个字”；
     */
    private String buildFuzzyName(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        boolean isUnicode = false;
        if (name.length() != name.codePoints().count()) {
            isUnicode = true;
        }
        int length = Long.valueOf(name.codePoints().count()).intValue();
        StringBuilder builder = new StringBuilder();
        if (length == 1) {
            builder.appendCodePoint(name.codePointAt(0));
            builder.append("**");
            return builder.toString();
        }
        int code0 = name.codePointAt(0);
        builder.appendCodePoint(code0);

        if (name.length() > 1 && name.length() < 4) {
            builder.append("*");
        }

        if (name.length() >= 4) {
            builder.append("***");
        }
        int codeEnd;
        if (isUnicode) {
            codeEnd = name.codePointAt(length);
        } else {
            codeEnd = name.codePointAt(length - 1);
        }
        builder.appendCodePoint(codeEnd);
        return builder.toString();
    }

}
