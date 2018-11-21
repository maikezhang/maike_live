package cn.idongjia.live.restructure.cache.liveMP;

import cn.idongjia.live.pojo.homePage.LiveTabCO;
import cn.idongjia.live.support.JsonUtils;
import cn.idongjia.live.support.redis.RedisKey;
import cn.idongjia.live.support.redis.RedisTools;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.user.lib.pojo.vo.CustomerVo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/7/25
 * Time: 下午5:19
 */
@Component
public class LiveMPPushCache implements RedisKey {

    @Resource
    private RedisTools redisCache;

    private static final String LIVEKEY = "live_MP_push_";
    private static final String USERKEY = "live_MP_user_";

    private static final Integer TIMEROUT = 30 * 60;

    private static final Log LOGGER= LogFactory.getLog(LiveMPPushCache.class);


    /**
     * 删除
     *
     * @param liveId 直播Id
     * @return
     */
    public boolean vanishLiveUserRedis(Long liveId) {
           String buildKey = LIVEKEY + liveId;
            return redisCache.del(buildKey);
    }

    public boolean batchDelRedis(List<Long> userId,Long liveId){
        List<String> keys = userId.stream().map(user -> {
            return USERKEY + user+"_"+liveId;
        }).collect(Collectors.toList());
        return redisCache.pipeDel(keys);
    }



    public boolean putLiveUserRedis(Long liveId, String user, Integer expireTime) {
        String buildKey = LIVEKEY + liveId;
        redisCache.saveSet(buildKey, user);

        if (expireTime != null && expireTime != 0) {
            redisCache.expire(buildKey, expireTime);
        }
        return true;
    }

    public Set<String> getLiveUserRedis(Long liveId) {
        String buildKey = LIVEKEY + liveId;
        return redisCache.getFromSet(buildKey);
    }



    public boolean putUserFormIdRedis(Long uid,Long liveId, String formId) {

        String buileKey = USERKEY + uid+"_"+liveId ;

         redisCache.set(buileKey, formId,null);
         return true;

    }



    public Map<Long, String> batchGetUserFormIdRedis(List<Long> userId,Long liveId) {
        List<String> keys = userId.stream().map(user -> {
            return USERKEY + user+"_"+liveId;
        }).collect(Collectors.toList());

        Map<String, String> stringStringMap = redisCache.pipeGet(keys);

        LOGGER.info("获取user的formId数据：{}",JsonUtils.toJson(stringStringMap));



        if (Objects.isNull(stringStringMap)) {
            return null;
        }
        return stringStringMap.entrySet().stream().collect(Collectors.toMap(v1 -> {
                    return Long.parseLong(v1.getKey().split("_")[3]);
                }, v1 -> v1.getValue()
                , (v1, v2) -> v1));


    }


}
