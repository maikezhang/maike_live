package cn.idongjia.live.restructure.biz;

import cn.idongjia.divine.lib.pojo.request.live.CraftsmanQry;
import cn.idongjia.live.api.live.pojo.response.MultiResponse;
import cn.idongjia.divine.lib.pojo.response.live.special.LiveSpecialCraftsmanCO;
import cn.idongjia.divine.lib.service.LiveSpecialCraftsmanService;
import cn.idongjia.live.db.mybatis.po.AnchorBlackWhitePO;
import cn.idongjia.live.db.mybatis.query.DBAnchorBlackWhiteQuery;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.dto.AnchorBlackWhiteDTO;
import cn.idongjia.live.restructure.enums.AnchorBlackWhiteEnum;
import cn.idongjia.live.restructure.event.LiveAnchorBlackData;
import cn.idongjia.live.restructure.event.processor.LiveAnchorBlackEventProcessor;
import cn.idongjia.live.restructure.event.processor.LiveCreatedEventProsser;
import cn.idongjia.live.restructure.manager.DivineSearchManager;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.restructure.pojo.cmd.anchor.AnchorBlackWhiteAddCmd;
import cn.idongjia.live.restructure.pojo.co.live.AnchorBlackWhiteCO;
import cn.idongjia.live.restructure.pojo.query.AnchorBlackWhiteQry;
import cn.idongjia.live.restructure.query.AnchorBlackWhiteQueryHandler;
import cn.idongjia.live.restructure.repo.AnchorBlackWhiteRepo;
import cn.idongjia.live.support.BaseEnum;
import cn.idongjia.live.support.spring.SpringUtils;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import org.apache.velocity.runtime.directive.Break;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/2
 * Time: 下午2:00
 */
@Component
public class AnchorBlackWhiteBO {

    @Resource
    private AnchorBlackWhiteRepo anchorBlackWhiteRepo;

    @Resource
    private AnchorBlackWhiteQueryHandler queryHandler;

    @Resource
    private DivineSearchManager divineSearchManager;



    @Resource
    private UserManager userManager;

    private static final Log LOGGER = LogFactory.getLog(AnchorBlackWhiteBO.class);

    @Transactional(rollbackFor = Throwable.class)
    public void add(AnchorBlackWhiteAddCmd cmd) {

        List<Long> anchorIds = cmd.getAnchorIds();


        //验证添加的匠人数据
        List<Long> craftsmanIds = userManager.takeBatchCraftsmans(anchorIds);

        List<Long> notExitIds = anchorIds.stream().filter(x -> !craftsmanIds.contains(x)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(notExitIds)) {
            throw LiveException.failure("匠人id " + notExitIds + " 不存在，请修改后提交");
        }


        DBAnchorBlackWhiteQuery query = DBAnchorBlackWhiteQuery.builder()
                .anchorIds(anchorIds)
                .build();
        List<AnchorBlackWhiteDTO> anchorBlackWhiteDTOS;
        try {
            anchorBlackWhiteDTOS = queryHandler.list(query).get();

        } catch (Exception e) {
            LOGGER.info("查询直播主播黑白名单失败:{}", e);
            throw LiveException.failure("查询直播主播黑白名单失败");
        }

        if (!CollectionUtils.isEmpty(anchorBlackWhiteDTOS)) {
            anchorBlackWhiteDTOS.forEach(needUpdate -> {
                AnchorBlackWhitePO po = new AnchorBlackWhitePO();
                po.setAnchorId(needUpdate.getAnchorId());
                po.setId(needUpdate.getId());
                po.setUpdateTime(Utils.getCurrentMillis());
                po.setCreateTime(Utils.getCurrentMillis());
                AnchorBlackWhiteEnum.AnchorBlackWhiteType oldType = BaseEnum.parseInt2Enum(needUpdate.getType(), AnchorBlackWhiteEnum.AnchorBlackWhiteType.values())
                        .orElseThrow(() -> LiveException.failure("主播黑白名单type 转换失败"));
                Integer integer = assembleType(oldType, cmd.getType(), 1);
                if (Objects.nonNull(integer) && Objects.equals(-1, integer.intValue())) {
                    throw LiveException.failure("该匠人已被删除，请刷新列表后重试！");
                }
                po.setType(integer);

                LOGGER.info("更新的主播  anchorId:{} ,type:{}", po.getAnchorId(), po.getType());
                AnchorBlackWhiteDTO anchorBlackWhiteDTO = new AnchorBlackWhiteDTO(po);
                anchorBlackWhiteRepo.update(anchorBlackWhiteDTO);
            });

            List<Long> needUpdateIds = anchorBlackWhiteDTOS.stream().map(AnchorBlackWhiteDTO::getAnchorId).collect(Collectors.toList());
            anchorIds = anchorIds.stream().filter(x -> !needUpdateIds.contains(x)).collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(anchorIds)) {
            anchorIds.forEach(anchorId -> {
                AnchorBlackWhitePO po = new AnchorBlackWhitePO();
                po.setAnchorId(anchorId);
                po.setUpdateTime(Utils.getCurrentMillis());
                po.setCreateTime(Utils.getCurrentMillis());
                Integer integer = assembleType(null, cmd.getType(), 1);
                if (Objects.nonNull(integer) && Objects.equals(-1, integer.intValue())) {
                    throw LiveException.failure("该匠人已被删除，请刷新列表后重试！");
                }
                po.setType(integer);

                LOGGER.info("添加新的主播  anchorId:{} ,type:{}", po.getAnchorId(), po.getType());
                AnchorBlackWhiteDTO anchorBlackWhiteDTO = new AnchorBlackWhiteDTO(po);
                anchorBlackWhiteRepo.add(anchorBlackWhiteDTO);
            });
        }
        LiveAnchorBlackEventProcessor liveAnchorBlackEventProcessor = SpringUtils.getBean("liveAnchorBlackEventProcessor", LiveAnchorBlackEventProcessor.class)
                .orElseThrow(() -> LiveException.failure("获取liveAnchorBlackEventProcessor实例失败"));
        Integer type = cmd.getType();
        if (Objects.equals(AnchorBlackWhiteEnum.BlackWhite.BLACK.getCode(), type.intValue())) {
            LiveAnchorBlackData data=LiveAnchorBlackData.builder().anchorIds(cmd.getAnchorIds()).build();
            liveAnchorBlackEventProcessor.publishEvent(data);
        }
    }


    /**
     * 组装type
     *
     * @param oldType  0都不显示 1 app显示小程序不显示 2 小程序显示 app不显示  3 小程序和app都显示
     * @param newType  1-黑名单  2-白名单
     * @param addOrDel 1-添加    2-删除
     * @return
     */
    public Integer assembleType(AnchorBlackWhiteEnum.AnchorBlackWhiteType oldType, Integer newType, Integer addOrDel) {

        Integer result = -1;

        if (Objects.isNull(oldType) && addOrDel.equals(1) && newType.equals(1)) {
            result = AnchorBlackWhiteEnum.AnchorBlackWhiteType.NO_PLAY_TYPE.getCode();
        } else if (Objects.isNull(oldType) && addOrDel.equals(1) && newType.equals(2)) {
            result = AnchorBlackWhiteEnum.AnchorBlackWhiteType.MP_APP_TYPE.getCode();
        } else {
            switch (oldType) {
                case NO_PLAY_TYPE:
                    if (newType.equals(1) && addOrDel.equals(1)) {
                        result = AnchorBlackWhiteEnum.AnchorBlackWhiteType.NO_PLAY_TYPE.getCode();
                    }
                    if (newType.equals(2) && addOrDel.equals(1)) {
                        result = AnchorBlackWhiteEnum.AnchorBlackWhiteType.ONLY_MP_TYPE.getCode();
                    }
                    if (newType.equals(1) && addOrDel.equals(2)) {
                        result = AnchorBlackWhiteEnum.AnchorBlackWhiteType.ONLY_APP_TYPE.getCode();
                    }
                    break;

                case ONLY_APP_TYPE:
                    if (newType.equals(1) && addOrDel.equals(1)) {
                        result = AnchorBlackWhiteEnum.AnchorBlackWhiteType.NO_PLAY_TYPE.getCode();
                    }
                    if (newType.equals(2) && addOrDel.equals(1)) {
                        result = AnchorBlackWhiteEnum.AnchorBlackWhiteType.MP_APP_TYPE.getCode();
                    }
                    if (newType.equals(2) && addOrDel.equals(2)) {
                        result = AnchorBlackWhiteEnum.AnchorBlackWhiteType.ONLY_APP_TYPE.getCode();
                    }
                    break;
                case ONLY_MP_TYPE:
                    if (newType.equals(1) && addOrDel.equals(1)) {
                        result = AnchorBlackWhiteEnum.AnchorBlackWhiteType.ONLY_MP_TYPE.getCode();
                    }
                    if (newType.equals(2) && addOrDel.equals(1)) {
                        result = AnchorBlackWhiteEnum.AnchorBlackWhiteType.ONLY_MP_TYPE.getCode();
                    }
                    if (newType.equals(1) && addOrDel.equals(2)) {
                        result = AnchorBlackWhiteEnum.AnchorBlackWhiteType.MP_APP_TYPE.getCode();
                    }
                    if (newType.equals(2) && addOrDel.equals(2)) {
                        result = AnchorBlackWhiteEnum.AnchorBlackWhiteType.NO_PLAY_TYPE.getCode();
                    }
                    break;
                case MP_APP_TYPE:
                    if (newType.equals(1) && addOrDel.equals(1)) {
                        result = AnchorBlackWhiteEnum.AnchorBlackWhiteType.ONLY_MP_TYPE.getCode();
                    }
                    if (newType.equals(2) && addOrDel.equals(1)) {
                        result = AnchorBlackWhiteEnum.AnchorBlackWhiteType.MP_APP_TYPE.getCode();
                    }
                    if (newType.equals(1) && addOrDel.equals(2)) {
                        result = AnchorBlackWhiteEnum.AnchorBlackWhiteType.MP_APP_TYPE.getCode();
                    }
                    if (newType.equals(2) && addOrDel.equals(2)) {
                        result = AnchorBlackWhiteEnum.AnchorBlackWhiteType.ONLY_APP_TYPE.getCode();
                    }
                    break;

                default:
                    break;

            }
        }

        return result;
    }


    public boolean delete(Long anchorId, Integer type) {


        AnchorBlackWhiteDTO anchorBlackWhiteDTO = queryHandler.get(anchorId);
        if (Objects.isNull(anchorBlackWhiteDTO)) {
            throw LiveException.failure("对不起，您删除的匠人不存在");
        }
        AnchorBlackWhiteEnum.AnchorBlackWhiteType oldType = BaseEnum.parseInt2Enum(anchorBlackWhiteDTO.getType(), AnchorBlackWhiteEnum.AnchorBlackWhiteType.values())
                .orElseThrow(() -> LiveException.failure("主播黑白名单type 转换失败"));

        Integer            delType = assembleType(oldType, type, 2);
        if (Objects.nonNull(delType) && Objects.equals(-1, delType.intValue())) {
            throw LiveException.failure("该匠人已被删除，请刷新列表后重试！");
        }
        AnchorBlackWhitePO po      = new AnchorBlackWhitePO();
        po.setId(anchorBlackWhiteDTO.getId());
        po.setUpdateTime(Utils.getCurrentMillis());
        po.setType(delType);
        AnchorBlackWhiteDTO anchorBlackWhiteDTO1 = new AnchorBlackWhiteDTO(po);
        return anchorBlackWhiteRepo.update(anchorBlackWhiteDTO1) > 0;


    }

    public MultiResponse<AnchorBlackWhiteCO> page(AnchorBlackWhiteQry qry) {
        int limit = 0;
        if (qry.getLimit() == null) {
            limit = 15;
        } else {
            limit = qry.getLimit();
        }
        CraftsmanQry craftsmanQry = new CraftsmanQry();
        craftsmanQry.setCraftsmanName(qry.getCraftsmanName());
        craftsmanQry.setUserId(qry.getCraftsmanUserId());
        craftsmanQry.setLimit(limit);
        craftsmanQry.setPage(qry.getPage());
        if (Objects.equals(AnchorBlackWhiteEnum.BlackWhite.BLACK.getCode(), qry.getType().intValue())) {
            List<Integer> showType = Arrays.asList(AnchorBlackWhiteEnum.AnchorBlackWhiteType.NO_PLAY_TYPE.getCode()
                    , AnchorBlackWhiteEnum.AnchorBlackWhiteType.ONLY_MP_TYPE.getCode());
            craftsmanQry.setShowType(showType);
        } else if (Objects.equals(AnchorBlackWhiteEnum.BlackWhite.WHITE.getCode(), qry.getType().intValue())) {
            List<Integer> showType = Arrays.asList(AnchorBlackWhiteEnum.AnchorBlackWhiteType.ONLY_MP_TYPE.getCode()
                    , AnchorBlackWhiteEnum.AnchorBlackWhiteType.MP_APP_TYPE.getCode());
            craftsmanQry.setShowType(showType);
        }
        cn.idongjia.divine.lib.pojo.response.MultiResponse<LiveSpecialCraftsmanCO> liveAnchorBlackWhite = divineSearchManager.getLiveAnchorBlackWhite(craftsmanQry);

        int total = liveAnchorBlackWhite.getTotal();
        if (total > 0) {
            List<LiveSpecialCraftsmanCO> data = (List<LiveSpecialCraftsmanCO>) liveAnchorBlackWhite.getData();
            List<AnchorBlackWhiteCO> collect = data.stream().map(liveSpecialCraftsmanCO -> {
                AnchorBlackWhiteCO anchorBlackWhiteCO = new AnchorBlackWhiteCO();
                anchorBlackWhiteCO.setCraftsmanUserId(liveSpecialCraftsmanCO.getCraftsmanUserId());
                anchorBlackWhiteCO.setCreateTime(liveSpecialCraftsmanCO.getCreateTime());
                anchorBlackWhiteCO.setCraftsmanAvatar(liveSpecialCraftsmanCO.getCraftsmanAvatar());
                anchorBlackWhiteCO.setCraftsmanName(liveSpecialCraftsmanCO.getCraftsmanName());
                return anchorBlackWhiteCO;
            }).collect(Collectors.toList());
            return MultiResponse.of(collect, total);

        }

        return MultiResponse.buildSuccess();
    }

}
