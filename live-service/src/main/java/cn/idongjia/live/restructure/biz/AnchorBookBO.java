package cn.idongjia.live.restructure.biz;

import cn.idongjia.clan.lib.service.UserCenterService;
import cn.idongjia.live.db.mybatis.po.AnchorBookPO;
import cn.idongjia.live.db.mybatis.po.LiveBookPO;
import cn.idongjia.live.db.mybatis.query.DBAnchorBookQuery;
import cn.idongjia.live.db.mybatis.query.DBLiveShowQuery;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.purelive.book.AnchorsBookDO;
import cn.idongjia.live.pojo.user.Anchor;
import cn.idongjia.live.query.purelive.book.AnchorsBookSearch;
import cn.idongjia.live.restructure.convert.ConvertorI;
import cn.idongjia.live.restructure.domain.entity.user.AnchorEntity;
import cn.idongjia.live.restructure.domain.entity.user.LiveAnchor;
import cn.idongjia.live.restructure.dto.AnchorBookDTO;
import cn.idongjia.live.restructure.dto.AnchorDTO;
import cn.idongjia.live.restructure.dto.LiveShowDTO;
import cn.idongjia.live.restructure.factory.AnchorEntityFactory;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.restructure.query.AnchorBookQueryHandler;
import cn.idongjia.live.restructure.query.LiveShowQueryHandler;
import cn.idongjia.live.restructure.query.QueryFactory;
import cn.idongjia.live.restructure.repo.HotAnchorsRepo;
import cn.idongjia.live.restructure.repo.LiveBookRepo;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.user.lib.pojo.vo.CustomerVo;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 主播
 *
 * @author lc
 * @create at 2018/6/13.
 */
@Component
public class AnchorBookBO {

    private static final Log LOGGER = LogFactory.getLog(AnchorBookBO.class);

    @Resource
    private AnchorBookQueryHandler anchorBookQueryHandler;

    @Resource
    private UserCenterService userCenterService;

    @Resource
    private UserManager userManager;


    @Resource
    private LiveShowQueryHandler liveShowQueryHandler;

    @Resource
    private LiveBookRepo livePureBookRepo;


    @Resource
    private ConvertorI<AnchorsBookDO, LiveAnchor, AnchorBookPO> anchorAnchorBookConvertor;

    @Resource
    private ConvertorI<Anchor, LiveAnchor, AnchorDTO> anchoConvertor;


    public List<AnchorsBookDO> list(AnchorsBookSearch anchorsBookSearch) {
        DBAnchorBookQuery dbAnchorBookQuery = QueryFactory.getInstance(anchorsBookSearch);
        try {
            List<AnchorBookDTO> liveAnchorBookDTOS = anchorBookQueryHandler.list(dbAnchorBookQuery).get();
            return liveAnchorBookDTOS.stream().map(anchorBookDTO -> {
                return anchorAnchorBookConvertor.dataToClient(anchorBookDTO.toDO());
            }).collect(Collectors.toList());
        } catch (Exception e) {
            AnchorBookBO.LOGGER.error("查询订阅主播记录失败{}", e);
            throw LiveException.failure("查询订阅主播记录失败");
        }
    }

    public Integer count(AnchorsBookSearch anchorsBookSearch) {
        DBAnchorBookQuery dbAnchorBookQuery = QueryFactory.getInstance(anchorsBookSearch);
        return anchorBookQueryHandler.count(dbAnchorBookQuery);

    }


    public List<Anchor> listAnchorsByUid(Long uid, AnchorsBookSearch anchorsBookSearch) {
        anchorsBookSearch.setUid(uid);
        DBAnchorBookQuery dbAnchorBookQuery = QueryFactory.getInstance(anchorsBookSearch);
        try {
            List<AnchorBookDTO>   liveAnchorBookDTOS = anchorBookQueryHandler.list(dbAnchorBookQuery).get();
            List<Long>            anchorIds          = liveAnchorBookDTOS.stream().map(AnchorBookDTO::getAnchorId).collect(Collectors.toList());
            Map<Long, CustomerVo> customerVoMap      = userManager.takeBatchCustomer(anchorIds);
            List<Long>            followedUserIds    = userCenterService.listFollowIdsByFollowIdIn(uid, anchorIds);

            return liveAnchorBookDTOS.stream().map(liveAnchorBookDTO -> {
                Long       anchorId     = liveAnchorBookDTO.getAnchorId();
                int        countFollows = userCenterService.countFollows(anchorId);
                CustomerVo customerVo   = customerVoMap.get(anchorId);
                int        isBooked     = followedUserIds.contains(anchorId) ? 1 : 0;
                AnchorDTO  anchorDTO    = new AnchorDTO();
                anchorDTO.setAnchorBookDTO(liveAnchorBookDTO);
                anchorDTO.setCustomerVo(customerVo);
                anchorDTO.setFollowerCount(countFollows);
                anchorDTO.setIsBooked(isBooked);
                return anchoConvertor.dataToClient(anchorDTO);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            AnchorBookBO.LOGGER.error("查询订阅主播记录失败{}", e);
            throw LiveException.failure("查询订阅主播记录失败");
        }
    }

    /**
     * 对主播订阅
     *
     * @param anchorsBookDO 主播do
     * @return
     * @author zhangyingjie
     */
    public Long addAnchorBook(AnchorsBookDO anchorsBookDO) {
        AnchorEntity entity = AnchorEntityFactory.getInstance(anchorsBookDO);
        Long         id     = entity.addAnchorBook();
        //如果用户订阅了该主播  需要同步订阅该主播下所有的直播(除了已结束的直播)
        Long uid      = anchorsBookDO.getUid();
        Long anchorId = anchorsBookDO.getAnchorId();
        //获取该主播所有直播id(除了已结束的直播)
        List<Long> lids = new ArrayList<>();
        DBLiveShowQuery dbLiveShowQuery = DBLiveShowQuery.builder()
                .userIds(Arrays.asList(uid)).online(LiveConst.STATUS_LIVE_ONLINE).build();
        try {
            List<LiveShowDTO> liveShowDTOS = liveShowQueryHandler.list(dbLiveShowQuery).get();
            if (CollectionUtils.isEmpty(liveShowDTOS)) {
                return id;
            }
            lids = liveShowDTOS.stream().map(LiveShowDTO::getId).collect(Collectors.toList());

        } catch (Exception e) {
            LOGGER.error("查询失败{}", e);
            throw LiveException.failure("查询失败");
        }
        List<LiveBookPO> liveBookPOS = new ArrayList<>();
        lids.forEach(lid -> {
            LiveBookPO po = new LiveBookPO();
            po.setCreateTime(new Timestamp(Utils.getCurrentMillis()));
            po.setStatus(LiveConst.STATUS_BOOK_NORMAL);
            po.setModifiedTime(new Timestamp(Utils.getCurrentMillis()));
            po.setUserId(uid);
            po.setLiveId(lid);
            liveBookPOS.add(po);
        });
        livePureBookRepo.batchAddLiveBook(liveBookPOS);
        return id;
    }

    /**
     * 取消对主播的订阅
     *
     * @param anchorsBookDO 取消订阅的参数
     * @return true or false
     * @author zhangyingjie
     */
    public boolean delAnchorBook(AnchorsBookDO anchorsBookDO) {
        AnchorEntity entity = AnchorEntityFactory.getInstance(anchorsBookDO);
        //取消对主播的订阅 就要取消对主播的直播订阅
        if (entity.delAnchorBook()) {
            Long       uid      = anchorsBookDO.getUid();
            Long       anchorId = anchorsBookDO.getAnchorId();
            List<Long> lids     = new ArrayList<>();
            DBLiveShowQuery dbLiveShowQuery = DBLiveShowQuery.builder()
                    .userIds(Arrays.asList(uid)).online(LiveConst.STATUS_LIVE_ONLINE).build();
            try {
                List<LiveShowDTO> liveShowDTOS = liveShowQueryHandler.list(dbLiveShowQuery).get();
                if (CollectionUtils.isEmpty(liveShowDTOS)) {
                    return true;
                }
                lids = liveShowDTOS.stream().map(LiveShowDTO::getId).collect(Collectors.toList());

            } catch (Exception e) {
                LOGGER.error("查询主播的直播失败{}", e);
                throw LiveException.failure("查询失败");
            }
            List<LiveBookPO> liveBookPOS = new ArrayList<>();
            lids.forEach(lid -> {
                LiveBookPO po = new LiveBookPO();
                po.setStatus(LiveConst.STATUS_BOOK_DEL);
                po.setModifiedTime(new Timestamp(Utils.getCurrentMillis()));
                po.setUserId(uid);
                po.setLiveId(lid);
                liveBookPOS.add(po);
            });
            livePureBookRepo.batchDeleteLiveBook(liveBookPOS);
        }

        return true;

    }

}
