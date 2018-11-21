package cn.idongjia.live.restructure.repo;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.live.db.mybatis.mapper.AnchorBookMapper;
import cn.idongjia.live.db.mybatis.po.AnchorBookPO;
import cn.idongjia.live.db.mybatis.query.DBAnchorBookQuery;
import cn.idongjia.live.pojo.user.Anchor;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.restructure.manager.DisconfManager;
import cn.idongjia.live.restructure.manager.UserManager;
import cn.idongjia.live.support.LiveConst;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.util.Utils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class HotAnchorsRepo {

    private static final Log LOGGER = LogFactory.getLog(HotAnchorsRepo.class);
    @Resource
    private ConfigManager configManager;
    @Resource
    private DisconfManager disconfManager;
    @Resource
    private UserManager userManager;
    @Resource
    private AnchorBookMapper anchorBookMapper;

    /**
     * 批量增加推荐主播
     *
     * @param uids 主播ID列表
     * @return 是否成功
     */
    public boolean addHotAnchors(List<Long> uids) {
        Set<Long> anchorIds = listHotAnchorsAsSet();
        Set<Long> failedAnchors = new HashSet<>();
        HotAnchorsRepo.LOGGER.info("增加之前热门主播: " + anchorIds);
        List<User> users = userManager.listUsers(uids);
        List<Long> userList = users.stream().map(User::getUid).collect(Collectors.toList());
        anchorIds.addAll(userList);
        HotAnchorsRepo.LOGGER.info("增加之后热门主播: " + anchorIds);
        replaceHotAnchors(anchorIds);
        if (failedAnchors.size() == 0) {
            return true;
        } else {
            throw new LiveException(-12138, "用户ID: " + StringUtils.join(failedAnchors, ",") + "不存在");
        }
    }

    /**
     * 列表形式列出所有推荐主播ID
     *
     * @return 主播ID列表
     */
    public List<Long> listHotAnchors() {
        String uidStr = configManager.getHotAnchors();
        List<Long> reList = new ArrayList<>();
        if (uidStr == null || "".equals(uidStr)) {
            return reList;
        }
        for (String uid : uidStr.split(",")) {
            reList.add(Long.parseLong(uid));
        }
        return reList;
    }

    /**
     * 集合形式获取主播ID
     *
     * @return 主播ID集合
     */
    private Set<Long> listHotAnchorsAsSet() {
        String uidStr = configManager.getHotAnchors();
        Set<Long> reSet = new HashSet<>();
        if (uidStr == null || "".equals(uidStr)) {
            return reSet;
        }
        for (String uid : uidStr.split(",")) {
            reSet.add(Long.parseLong(uid));
        }
        return reSet;
    }

    /**
     * 随机列出推荐主播ID
     *
     * @param uid 用户ID，判断是否订阅
     * @return 主播ID列表
     */
    public List<Anchor> listRandomly(Long uid) {
        List<Long> anchors = listHotAnchors();
        if (anchors == null || anchors.size() == 0) {
            return new ArrayList<>();
        }
        Set<Integer> randomPos = new HashSet<>();
        int bound = anchors.size();
        if (bound <= configManager.getRecommendNum()) {
            for (int i = 0; i < bound; i++) {
                randomPos.add(i);
            }
        } else {
            int[] values = new int[bound];
            Random random = new Random(Utils.getCurrentMillis());
            for (int i = 0; i < values.length; i++) {
                values[i] = i;
            }
            int pos1, pos2, pos3;
            for (int i = 0; i < values.length; i++) {
                pos1 = random.nextInt(bound - 1);
                pos2 = random.nextInt(bound - 1);
                if (pos1 != pos2) {
                    pos3 = values[pos1];
                    values[pos1] = values[pos2];
                    values[pos2] = pos3;
                }
            }
            for (int i = 0; i < configManager.getRecommendNum(); i++) {
                randomPos.add(values[i]);
            }
            /*
             * while (randomPos.size() <= configManager.getRecommendNum() - 1) {
             * Random random = new Random(Utils.getCurrentMillis());
             * int p = random.nextInt(bound);
             * randomPos.persist(p);
             * }
             */
        }
        List<Anchor> randomList = new ArrayList<>();
        for (Integer pos : randomPos) {
            Long anchorId = anchors.get(pos);
            try {
                Anchor anchor = userManager.getAnchorByUid(anchorId);
                randomList.add(anchor);
                DBAnchorBookQuery dbAnchorBookQuery = DBAnchorBookQuery.builder().userIds(Arrays.asList(uid)).anchorIds(Arrays.asList(anchor.getHuid()))
                        .status(Arrays.asList(LiveConst.STATUS_BOOK_NORMAL)).build();
                List<AnchorBookPO> anchorsBookPOS = anchorBookMapper.list(dbAnchorBookQuery);
                if (!Utils.isEmpty(anchorsBookPOS)) {
                    anchor.setIsBooked(LiveConst.STATUS_ANCHOR_BOOKED);
                } else {
                    anchor.setIsBooked(LiveConst.STATUS_ANCHOR_UNBOOKED);
                }
            } catch (Exception e) {
                HotAnchorsRepo.LOGGER.error("随机主播异常" + e.getMessage());
            }
        }
        return randomList;
    }

    /**
     * 删除推荐主播ID
     *
     * @param uid 主播ID
     * @return 是否成功
     */
    public boolean deleteHotAnchor(Long uid) {
        Set<Long> uids = listHotAnchorsAsSet();
        HotAnchorsRepo.LOGGER.info("删除之前热门主播: " + uids);
        if (uids.contains(uid)) {
            uids.remove(uid);
        }
        HotAnchorsRepo.LOGGER.info("删除之后热门主播: " + uids);
        return replaceHotAnchors(uids);
    }

    /**
     * 增加推荐主播ID
     *
     * @param uid 主播ID
     * @return 是否成功
     */
    public boolean addHotAnchor(Long uid) {
        userManager.getUser(uid);
        Set<Long> uids = listHotAnchorsAsSet();
        HotAnchorsRepo.LOGGER.info("增加之前热门主播: " + uids);
        try {
            userManager.getUser(uid);
            uids.add(uid);
        } catch (Exception e) {
            throw new LiveException(-12138, "用户ID: " + uid + "不存在");
        }
        HotAnchorsRepo.LOGGER.info("增加之后热门主播: " + uids);
        return replaceHotAnchors(uids);
    }

    /**
     * 根据推荐主播集合写入disconf
     *
     * @param collection 集合
     * @return 是否成功
     */
    private boolean replaceHotAnchors(Collection collection) {
        String uidStr = StringUtils.join(collection, ",");
        return disconfManager.addConfig(uidStr, configManager.getHotAnchorsConfName(), "hot_anchors");
    }
}
