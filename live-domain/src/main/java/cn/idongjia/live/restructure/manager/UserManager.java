package cn.idongjia.live.restructure.manager;

import cn.idongjia.clan.lib.dto.CraftsmanCategoryDto;
import cn.idongjia.clan.lib.entity.FansRel;
import cn.idongjia.clan.lib.pojo.CategoryDetail;
import cn.idongjia.clan.lib.pojo.FansUser;
import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.clan.lib.pojo.UserDetail;
import cn.idongjia.clan.lib.query.UserSearch;
import cn.idongjia.clan.lib.service.CraftsmanService;
import cn.idongjia.clan.lib.service.CustomerService;
import cn.idongjia.clan.lib.service.OAuthService;
import cn.idongjia.clan.lib.service.UserCenterService;
import cn.idongjia.clan.lib.service.UserService;
import cn.idongjia.common.query.BaseSearch;
import cn.idongjia.live.exception.LiveException;
import cn.idongjia.live.pojo.user.Anchor;
import cn.idongjia.live.restructure.domain.entity.user.Category;
import cn.idongjia.live.restructure.domain.entity.user.LiveAdmin;
import cn.idongjia.live.restructure.domain.entity.user.LiveAnchor;
import cn.idongjia.live.restructure.redis.BaseRedis;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.sm.dict.DJClient;
import cn.idongjia.user.lib.pojo.Customer;
import cn.idongjia.user.lib.pojo.vo.CraftsmanUserVo;
import cn.idongjia.user.lib.pojo.vo.CustomerVo;
import cn.idongjia.user.lib.pojo.vo.OauthVo;
import cn.idongjia.util.Utils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserManager {

    private static final Log     LOGGER       = LogFactory.getLog(UserManager.class);
    private static final Integer IS_CRAFTSMAN = 1;
    @Resource
    private UserService       userService;
    @Resource
    private RedisManager      redisManager;
    @Resource
    private UserCenterService userCenterService;

    @Resource
    private CustomerService customerService;


    @Resource
    private CraftsmanService craftsmanService;


    @Resource
    private BaseRedis<Long, CustomerVo> customerVOCache;

    @Resource
    private OAuthService oAuthService;

    public List<Long> takeFollowerList(Long uid, List<Long> followerIds) {
        return userCenterService.listFollowIdsByFollowIdIn(uid, followerIds);
    }

    /**
     * 使用用户服务获取用户信息
     *
     * @param uid 用户ID
     * @return 用户信息
     */
    public User getUser(Long uid) {
        User user = redisManager.getUserFromRedis(uid);
        if (user == null) {
            user = getUserFromService(uid);
            redisManager.putUserToRedis(user);
        }
        return user;
    }

    public List<User> listUsers(List<Long> uids) {
        List<User> users = new ArrayList<>();
        uids.forEach(uid -> {
            users.add(getUser(uid));
        });
        return users;
    }


    public Map<Long, CustomerVo> takeBatchCustomer(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new HashMap<>();
        }
        Map<Long, CustomerVo> cacheCustomerVOs = customerVOCache.batchGet(ids);
        if (!Utils.isEmpty(cacheCustomerVOs)) {
            ids.removeAll(cacheCustomerVOs.keySet());
        }
        try {
            int size = 50;
            int time = ids.size() / size;
            time = ids.size() % size == 0 ? time : time + 1;
            List<Long>       tmpIds;
            List<CustomerVo> customerVos  = new ArrayList<>();
            List<CustomerVo> tmpCustomers = null;
            for (int i = 0; i < time; i++) {
                if (i == time - 1) {
                    tmpIds = ids.subList(i * size, ids.size());
                } else {
                    tmpIds = ids.subList(i * size, (i + 1) * size);
                }
                tmpCustomers = customerService.listByUserIds(tmpIds);
                if (!Utils.isEmpty(tmpCustomers)) {
                    customerVos.addAll(tmpCustomers);
                }
            }
            Map<Long, CustomerVo> customerVoMap = new HashMap<>();
            for (CustomerVo customerVo : customerVos) {
                for (cn.idongjia.user.lib.pojo.User user : customerVo.getUsers()) {
                    customerVoMap.put(user.getId(), customerVo);
                }
            }
            if (!Utils.isEmpty(customerVoMap)) {
                customerVOCache.batchSave(customerVoMap);
            }
            if (!Utils.isEmpty(cacheCustomerVOs)) {
                customerVoMap.putAll(cacheCustomerVOs);
            }
            return customerVoMap;
        } catch (Exception e) {
            UserManager.LOGGER.warn("调用cn.idongjia.clan.lib.service.listByUserIds.listCustomerByUserIds失败: " + e);
            return new HashMap<>();
        }
    }


    public Map<Long, User> takeBatchUser(List<Long> ids) {
        try {
            int size = 50;
            int time = ids.size() / size;
            time = ids.size() % size == 0 ? time : time + 1;
            List<Long> tmpIds;
            List<User> users    = new ArrayList<>();
            List<User> tmpUsers = null;
            for (int i = 0; i < time; i++) {
                if (i == time - 1) {
                    tmpIds = ids.subList(i * size, ids.size());
                } else {
                    tmpIds = ids.subList(i * size, (i + 1) * size);
                }
                tmpUsers = userService.getUserByUids(tmpIds);
                if (!Utils.isEmpty(tmpUsers)) {
                    users.addAll(tmpUsers);
                }
            }
            Map<Long, User> userMap = new HashMap<>();
            for (User user : users) {
                List<Long> uids = user.getUserIds();
                for (Long uid : uids) {
                    userMap.put(uid, user);
                }
            }
            return userMap;
        } catch (Exception e) {
            UserManager.LOGGER.warn("调用cn.idongjia.clan.lib.service.UserService.getUserByUids失败: " + e);
            return new HashMap<>();
        }
    }

    public User getUserFromService(Long uid) {
        if (uid == null) {
            throw new LiveException(-12138, "主播ID为空");
        }
        try {
            User user = userService.getUser(uid);
            if (user == null) {
                throw new LiveException(-12138, "用户信息不存在");
            }
            if (!UserManager.IS_CRAFTSMAN.equals(user.getCraftsmanStatus())) {
                user.setTitle(null);
            }
            return user;
        } catch (Exception e) {
            UserManager.LOGGER.warn("用户信息服务失败" + e);

            throw new LiveException(-12138, "用户信息服务失败");
        }
    }

    public User getUserSafe(Long uid) {
        try {
            return getUserFromService(uid);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 根据主播用户手机号获取用户ID
     *
     * @param mobile 用户手机号
     * @return 用户ID
     */
    public Long getUserIdByMobile(String mobile) {
        User user;
        try {
            user = userService.getUserByUsernameOrMobile(null, mobile);
        } catch (Exception e) {

            UserManager.LOGGER.warn("手机号查询用户服务失败" + e.getMessage());
            throw new LiveException(-12138, "手机号查询用户服务失败");
        }
        if (user == null) {
            UserManager.LOGGER.warn("无此手机号对应的用户信息");
            throw new LiveException(-12138, "无此手机号对应的用户信息");
        }
        return user.getUid();
    }

    /**
     * 根据uid列表字符串获取用户信息
     *
     * @param uidStr 用户ID列表
     * @return 用户信息列表
     */
    public List<Anchor> getAnchorList(String uidStr) {
        List<Anchor> anchors = new ArrayList<>();
        if (uidStr == null) {
            return anchors;
        }
        for (String uid : uidStr.split(",")) {
            try {
                Anchor anchor = getAnchorByUid(Long.parseLong(uid));
                anchors.add(anchor);
            } catch (Exception e) {
                LOGGER.error("获取主播信息失败" + e.getMessage());
            }
        }
        return anchors;
    }

    /**
     * 根据ID获取主播信息
     *
     * @param uid 主播ID
     * @return 主播信息
     */
    public Anchor getAnchorByUid(Long uid) {
        User   user      = getUser(uid);
        int    countFans = userCenterService.countFans(uid);
        Anchor anchor    = new Anchor();
        anchor.setHuid(user.getUid());
        anchor.setHusername(user.getUsername());
        anchor.setHavatar(user.getAvatar());
        anchor.setHtitle(user.getTitle());
        anchor.setFans(countFans);
        return anchor;
    }

    /**
     * 匠人是否存在
     *
     * @param userId
     * @return
     */
    public boolean hasCraftsman(Long userId) {
        if (userId == null) {
            return false;
        }
        try {
            final Map<Long, LiveAnchor> craftmanMap = takeCraftmsnWithCategoryList(Collections.singletonList(userId));
            if (craftmanMap.size() > 0) {
                return true;
            }
        } catch (Exception e) {
            LOGGER.error(e);
            return false;
        }
        return false;
    }

    public Map<Long, LiveAnchor> takeCraftmsnWithCategoryList(List<Long> userIds) {
        if (Utils.isEmpty(userIds)) {
            return Collections.emptyMap();
        }
        List<CraftsmanCategoryDto> craftsmanCategoryDtos;
        try {
            craftsmanCategoryDtos = craftsmanService.listCraftsmanCategory(userIds);
        } catch (Exception e) {
            LOGGER.error("调用cn.idongjia.clan.lib.service.CraftsmanService#listCraftsmanCategory失败", e);
            return Collections.emptyMap();
        }
        return craftsmanCategoryDtos.stream()
                .map(this::assembleAnchorWithCraftsman)
                .collect(Collectors.toMap(LiveAnchor::getId, anchor -> anchor, (a1, a2) -> a1));
    }

    private LiveAnchor assembleAnchorWithCraftsman(CraftsmanCategoryDto craftsmanCategoryDto) {
        LiveAnchor anchor   = new LiveAnchor(craftsmanCategoryDto.getCustomer().getId());
        Customer   customer = craftsmanCategoryDto.getCustomer();
        if (customer != null) {
            anchor.setUsername(customer.getName());
            anchor.setRegisterTime(customer.getCreateTime());
            anchor.setAvatar(customer.getAvatar());
        }
        List<CategoryDetail> categoryDetails = craftsmanCategoryDto.getCategoryList();
        if (!Utils.isEmpty(categoryDetails)) {
            List<Category> categories = categoryDetails.stream()
                    .map(this::assembleCategory)
                    .collect(Collectors.toList());
            anchor.setCategories(categories);
        }
        return anchor;
    }

    private Category assembleCategory(CategoryDetail categoryDetail) {
        Category category = new Category();
        category.setId(categoryDetail.getIcid());
        category.setName(categoryDetail.getName());
        return category;
    }

    public User getUserByMobile(String mobile) {
        return userService.getUserByMobile(mobile);
    }


    public Map<Long, LiveAdmin> takeBatchAdmin(List<Long> adminIds) {
        if (Utils.isEmpty(adminIds)) {
            return Collections.emptyMap();
        }
        List<User> adminByUids;
        try {
            adminByUids = userService.getAdminByUids(adminIds);
        } catch (Exception e) {
            LOGGER.error("调用cn.idongjia.clan.lib.service.UserService#getAdminByUids失败", e);
            return Collections.emptyMap();
        }
        return adminByUids.stream().map(this::assembleAdminUser)
                .collect(Collectors.toMap(LiveAdmin::getId, admin -> admin, (a1, a2) -> a1));
    }

    private LiveAdmin assembleAdminUser(User adminUser) {
        LiveAdmin liveAdmin = LiveAdmin.builder()
                .id(adminUser.getAid())
                .name(adminUser.getUsername())
                .build();
        return liveAdmin;
    }

    public List<UserDetail> searchUser(String name, Integer userType) {
        UserSearch userSearch = new UserSearch();
        userSearch.setUsername(name);
        userSearch.setLimit(100);
        if (userType != null) {
            userSearch.setType(userType);
        }
        return userService.list(userSearch);
    }

    public List<Long> searchUserIds(String name) {
        try {
            final List<UserDetail> userDetails = searchUser(name, null);
            return userDetails.stream().map(UserDetail::getUid).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        }
    }

    public List<Long> takeBatchCraftsmans(List<Long> anchorIds) {
        List<CraftsmanUserVo> craftsmanUserVos = craftsmanService.listCraftsmanUserVo(anchorIds);
        LOGGER.info("获取匠人数据：{}", craftsmanUserVos);
        List<Long> collect = craftsmanUserVos.stream().map(CraftsmanUserVo::getCustomerId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)) {
            return new ArrayList<>();
        } else {
            return collect;
        }
    }

    public String getUserOpenId(Long uid) {
        String openId = null;
        try {
            List<OauthVo> oauthVos = oAuthService.listWechatAuthInfo(uid);

            if (CollectionUtils.isEmpty(oauthVos)) {
                return null;
            }
            for (OauthVo oauthVo : oauthVos) {
                if (Objects.equals(DJClient.LIVE_JADE_MP.getId(), oauthVo.getClient().intValue())) {
                    openId = oauthVo.getAttatchmentId();
                }
            }


        } catch (Exception e) {
            LOGGER.info("调用 cn.idongjia.clan.lib.service.OAuthService#listWechatAuthInfo 失败,{}", e);
        }


        return openId;


    }

    public List<Long> getCraftsManFollowUsers(Long craftsmanUserId) {

        List<FansUser> fansRels  = new ArrayList<>();
        List<FansUser> fansUsers;
        try {
            int limit = 100;
            int temp  = 0;

            do {
                BaseSearch search = new BaseSearch();
                search.setLimit(limit);
                search.setPage(temp + 1);
                fansUsers = userCenterService.getFansUsers(Arrays.asList(craftsmanUserId), search);
                if (!CollectionUtils.isEmpty(fansUsers)) {
                    fansRels.addAll(fansUsers);
                    temp++;
                }

            } while (fansUsers != null && fansUsers.size() != 0 && fansUsers.size() == limit);

        } catch (Exception e) {
            LOGGER.warn("调用cn.idongjia.clan.lib.service.UserCenterService#searchFollowUsers 失败,param:{},exception:{}", craftsmanUserId, e);
            return null;
        }

        if (CollectionUtils.isEmpty(fansRels)) {
            return null;
        }
        List<Long> collect = fansRels.stream().map(FansUser::getUid).collect(Collectors.toList());
        return collect;


    }


    public List<Long> getCustomerIdsByUserIds(List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return null;
        }

        List<CustomerVo> customerVos = new ArrayList<>();
        try {
            int temp=1;
            List<Long> queryUserIds=new ArrayList<>();
            for (int i=0;i<userIds.size();i++){
                queryUserIds.add(userIds.get(i));
                if (i == 100 *temp|| i==(userIds.size()-1)){
                    temp++;
                    List<CustomerVo> customerVos1 = customerService.listByUserIds(queryUserIds);
                    if(!CollectionUtils.isEmpty(customerVos1)){
                        customerVos.addAll(customerVos1);
                    }
                    queryUserIds.clear();

                }
            }

            if (CollectionUtils.isEmpty(customerVos)) {
                return null;
            }
        } catch (Exception e) {
            LOGGER.warn("调用cn.idongjia.clan.lib.service.customerService#listByUserIds 失败,param:{},exception:{}", userIds, e);
            return new ArrayList<>();
        }
        List<Long> collect     = customerVos.stream().map(CustomerVo::getId).collect(Collectors.toList());
        Set<Long>  uidBookedId = new HashSet<>(collect);
        return new ArrayList<>(uidBookedId);

    }


}
