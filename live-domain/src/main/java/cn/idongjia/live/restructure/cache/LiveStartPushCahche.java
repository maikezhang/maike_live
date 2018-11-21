package cn.idongjia.live.restructure.cache;

import cn.idongjia.live.restructure.cache.liveMP.LiveMPPushCache;
import cn.idongjia.live.restructure.manager.ConfigManager;
import cn.idongjia.live.support.redis.RedisKey;
import cn.idongjia.live.support.redis.RedisTools;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/10/15
 * Time: 上午9:44
 */
@Component
public class LiveStartPushCahche implements RedisKey {


    @Resource
    private RedisTools redisCache;

    @Resource
    private ConfigManager configManager;

    private static final String LIVESTARTPUSHKEY = "live_start_push_";

    private static final String LIVE_PUSH_USER_PRE = "live_push_user_";


    private static final Log LOGGER = LogFactory.getLog(LiveMPPushCache.class);


    public boolean lpushLiveStartPushUserId(Long liveId, List<Long> userIds) {
        String key = buildKey(LIVESTARTPUSHKEY + liveId);
        List<String> values = userIds.stream().map(userId -> {
            return Long.toString(userId);
        }).collect(Collectors.toList());
        return redisCache.pipeLpush(key, values);
    }


    public List<Long> lpopLiveStartPushUserIdByCount(Long liveId, int count) {
        String key = buildKey(LIVESTARTPUSHKEY + liveId);


        List<String> strings = redisCache.pipeLpopByCount(key, count);
        if (CollectionUtils.isEmpty(strings)) {
            return null;
        }

        return strings.stream().map(str -> {
            return Long.parseLong(str);
        }).collect(Collectors.toList());

    }


    /**
     * 缓存开始直播时发送用户的信息
     *
     * @param lid  直播ID
     * @param uids 已发送的用户ID
     */
    public void addPushUsersToRedis(Long lid, Set<Long> uids) {
            String key = LIVE_PUSH_USER_PRE + lid;
            Set<Long> uidBefore = getPushUsersFromRedis(lid);
            uids.addAll(uidBefore);
            String value = StringUtils.join(uids, ",");
        redisCache.set(key.getBytes(), value.getBytes(),configManager.getRedisExpireTime());
    }


    /**
     * 获取缓存中已发送推送用户的ID
     *
     * @param lid 直播ID
     */
    public Set<Long> getPushUsersFromRedis(Long lid) {
        Set<Long>        uids   = new HashSet<>();
        String           key    = LIVE_PUSH_USER_PRE + lid;
        Optional<String> values = redisCache.get(key);
        String           value  = null;
        if (values.isPresent()) {
            value = values.get();
        }
        if (value == null || "".equals(value)) {
            return uids;
        }
        for (String v : value.split(",")) {
            uids.add(Long.valueOf(v));
        }
        return uids;
}



}
