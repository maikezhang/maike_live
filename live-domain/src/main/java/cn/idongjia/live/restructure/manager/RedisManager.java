package cn.idongjia.live.restructure.manager;

import cn.idongjia.clan.lib.pojo.User;
import cn.idongjia.live.pojo.live.LivePullUrl;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

import static cn.idongjia.live.support.RedisConst.EXPIRE_TIME;
import static cn.idongjia.live.support.RedisConst.LIVE_URL_PRE;

@Component
public class RedisManager {

    @Resource(name = "cachePool")
    private JedisPool     cachePool;
    @Resource
    private ConfigManager configManager;

    private static final String LIVE_USER_PRE      = "live_user_";
    private static final String LIVE_PUSH_USER_PRE = "live_push_user_";

    /**
     * 把拉流地址按对应uid放入redis
     *
     * @param uid         主播ID
     * @param livePullUrl 拉流地址
     */
    public void putUrlsToRedis(Long uid, LivePullUrl livePullUrl) {
        try (Jedis jedis = cachePool.getResource()) {
            Gson gson = new Gson();
            jedis.set(LIVE_URL_PRE + uid, gson.toJson(livePullUrl));
            jedis.expire(LIVE_URL_PRE + uid, EXPIRE_TIME);
        }
    }

    /**
     * 根据直播ID从redis获取对应urls
     *
     * @param uid 直播键值
     * @return 拉流地址
     */
    public LivePullUrl getUrlsFromRedis(Long uid) {
        try (Jedis jedis = cachePool.getResource()) {
            String urls = jedis.get(LIVE_URL_PRE + uid);
            if (urls == null) return null;
            Gson gson = new Gson();
            return gson.fromJson(urls, LivePullUrl.class);
        }
    }

    /**
     * 缓存主播用户信息
     *
     * @param user 用户信息
     */
    void
    putUserToRedis(User user) {
        try (Jedis jedis = cachePool.getResource()) {
            Gson gson = new Gson();
            jedis.set(LIVE_USER_PRE + user.getUid(), gson.toJson(user));
            jedis.expire(LIVE_USER_PRE + user.getUid(), configManager.getRedisExpireTime());
        }
    }

    /**
     * 获取主播用户信息
     *
     * @param uid 用户ID
     * @return 用户信息
     */
    User getUserFromRedis(Long uid) {
        try (Jedis jedis = cachePool.getResource()) {
            String userStr = jedis.get(LIVE_USER_PRE + uid);
            if (userStr == null) return null;
            Gson gson = new Gson();
            return gson.fromJson(userStr, User.class);
        }
    }

    /**
     * 缓存开始直播时发送用户的信息
     *
     * @param lid  直播ID
     * @param uids 已发送的用户ID
     */
    public void addPushUsersToRedis(Long lid, Set<Long> uids) {
        try (Jedis jedis = cachePool.getResource()) {
            String key = LIVE_PUSH_USER_PRE + lid;
            Set<Long> uidBefore = getPushUsersFromRedis(lid);
            uids.addAll(uidBefore);
            String value = StringUtils.join(uids, ",");
            jedis.set(key, value);
            jedis.expire(key, configManager.getRedisExpireTime());
        }
    }

    /**
     * 获取缓存中已发送推送用户的ID
     *
     * @param lid 直播ID
     */
    public Set<Long> getPushUsersFromRedis(Long lid) {
        Set<Long> uids = new HashSet<>();
        try (Jedis jedis = cachePool.getResource()) {
            String key = LIVE_PUSH_USER_PRE + lid;
            String value = jedis.get(key);
            if (value == null || "".equals(value)) {
                return uids;
            }
            for (String v : value.split(",")) {
                uids.add(Long.valueOf(v));
            }
        }
        return uids;
    }
}
