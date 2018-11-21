package cn.idongjia.live.restructure.biz;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.divine.lib.pojo.request.live.LiveQry;
import cn.idongjia.divine.lib.pojo.response.live.general.GeneralLiveCO;
import cn.idongjia.live.api.live.pojo.response.MultiResponse;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.cache.liveMP.LiveMPPushCache;
import cn.idongjia.live.restructure.convert.ConvertorI;
import cn.idongjia.live.restructure.convert.LiveMPConvertor;
import cn.idongjia.live.restructure.domain.entity.zoo.LiveZoo;
import cn.idongjia.live.restructure.dto.LiveLikeDTO;
import cn.idongjia.live.restructure.dto.LiveMPListDTO;
import cn.idongjia.live.restructure.dto.LivePullUrlDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.enums.LiveEnum;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.restructure.manager.DivineSearchManager;
import cn.idongjia.live.restructure.manager.TemplateManager;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.restructure.pojo.co.live.LiveMPCO;
import cn.idongjia.live.restructure.pojo.co.live.LiveMPDetailCO;
import cn.idongjia.live.restructure.pojo.co.live.LiveMPFormIdCO;
import cn.idongjia.live.restructure.pojo.query.LiveMPQry;
import cn.idongjia.live.restructure.query.*;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/9/4
 * Time: 上午11:15
 */
@Component
public class LiveMPBO {

    @Resource
    private ConvertorI<LiveMPCO, LiveMPCO, GeneralLiveCO> liveMPConvertor;

    @Resource
    private LiveShowBO liveShowBO;

    @Resource
    private LiveResourceQueryHandler liveResourceQueryHandler;

    @Resource
    private TemplateManager templateManager;

    @Resource
    private ConfigManager configManager;

    @Resource
    private LiveZooQueryHandler liveZooQueryHandler;

    @Resource
    private LiveLikeQueryHandler liveLikeQueryHandler;

    @Resource
    private LiveMPPushCache liveMPPushCache;

    @Resource
    private UserManager userManager;

    @Resource
    private LiveShowQueryHandler liveShowQueryHandler;

    @Resource
    private LiveRoomQueryHandler liveRoomQueryHandler;


    private static final Log LOGGER = LogFactory.getLog(LiveMPBO.class);


    @Resource
    DivineSearchManager divineSearchManager;

    public MultiResponse<LiveMPCO> list(LiveMPQry qry) {


        LiveQry qry1 = QueryFactory.assembleLiveQry(qry, 0);

        cn.idongjia.divine.lib.pojo.response.MultiResponse<GeneralLiveCO> search = divineSearchManager.search(qry1);
        int total = search.getTotal();
        if (total > 0) {
            List<GeneralLiveCO> generalLiveCOS = (List<GeneralLiveCO>) search.getData();
            List<LiveMPCO> collect = generalLiveCOS.stream().map(generalLiveCO -> {
                return liveMPConvertor.dataToClient(generalLiveCO);
            }).collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(collect)) {
                return MultiResponse.of(collect, total);
            }
        }
        return MultiResponse.buildSuccess();
    }

    public boolean updateWeight(Long liveId, Integer weight) {

        liveShowBO.modifyLiveGeneralWeight(liveId, weight);
        return true;
    }

    public List<LiveMPCO> mpPageApi(LiveMPQry qry) {
        LiveQry qry1 = QueryFactory.assembleLiveQry(qry, 1);
        List<GeneralLiveCO> search = divineSearchManager.liveSearch(qry1);
        List<LiveMPCO> collect = search.stream().map(generalLiveCO -> {
            return liveMPConvertor.dataToClient(generalLiveCO);
        }).collect(Collectors.toList());

        return collect;
    }

    public LiveMPDetailCO getDetail(Long liveId, Long userId) {

        List<GeneralLiveCO> generalLiveCOS = divineSearchManager.liveList(Arrays.asList(liveId));

        boolean isLike = false;
        LiveLikeDTO dto = liveLikeQueryHandler.get(liveId, userId);

        if (Objects.nonNull(dto)) {
            isLike = true;
        }

        if (CollectionUtils.isEmpty(generalLiveCOS)) {
            throw LiveException.failure("直播不存在");
        }
        GeneralLiveCO generalLiveCO = generalLiveCOS.get(0);

        Long templateId = liveResourceQueryHandler.getTemplateId(generalLiveCO.getId());

        LivePullUrlDTO pullUrl = liveRoomQueryHandler.getPullUrl(generalLiveCO.getRoomId());

        LiveMPListDTO liveMPListDTO = new LiveMPListDTO();

        liveMPListDTO.setGeneralLiveCO(generalLiveCO);
        liveMPListDTO.setTemplateJson(getTemplateJson(templateId));
        String shareDescTemplate = configManager.getShareDescTemplate();
        liveMPListDTO.setShareDesc(shareDescTemplate);
        liveMPListDTO.setLivePullUrlDTO(pullUrl);

        liveMPListDTO.setIsLike(isLike);
        Map<Long, LiveZoo> liveZooMap = new HashMap<>();
        try {
            liveZooMap = liveZooQueryHandler.map(Arrays.asList(generalLiveCO.getZid())).get();

        } catch (Exception e) {
            LOGGER.info("查询直播聊天室数据失败：{}", e);
        }
        liveMPListDTO.setLiveZoo(liveZooMap.get(generalLiveCO.getZid()));


        LiveMPDetailCO liveMPDetailCO = LiveMPConvertor.dataToLiveMPDetail(liveMPListDTO);

        //直播状态为预展的 将访问的用户数据存储起来 开播的时候做小程序的服务推送
        Integer state = liveMPDetailCO.getState();
        if (Objects.equals(LiveEnum.LiveState.UNSTART.getCode(), state.intValue())) {

            String userOpenId = userManager.getUserOpenId(userId);
            if(Objects.isNull(userOpenId)){
                return liveMPDetailCO;
            }
            try {
                liveMPPushCache.putLiveUserRedis(liveId,userId+"<==>"+userOpenId,null);
            }catch (Exception e){
                LOGGER.info("缓存直播用户信息失败：{}", e);
            }
        }


        return liveMPDetailCO;
    }

    public String getTemplateJson(Long templateId) {
        String templateJsonStr = "";
        try {
            templateJsonStr = templateManager.appQueryTemplate(templateId);
        } catch (Exception e) {
            LOGGER.warn(e);
        }

        return templateJsonStr;
    }

    public boolean collectFormId(LiveMPFormIdCO liveMPFormIdCO) {

        Long liveId=liveMPFormIdCO.getLiveId();
        Long userId=liveMPFormIdCO.getUserId();
        LiveShowDTO liveShowDTO = liveShowQueryHandler.getById(liveId);
        if(!Objects.equals(LiveEnum.LiveState.UNSTART.getCode(),liveShowDTO.getState().intValue())){
            return false;
        }
        String formId=liveMPFormIdCO.getFormId();
        return liveMPPushCache.putUserFormIdRedis(userId,liveId,formId);
    }
}
