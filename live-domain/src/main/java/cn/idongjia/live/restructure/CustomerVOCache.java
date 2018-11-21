package cn.idongjia.live.restructure;

import cn.idongjia.live.restructure.redis.BaseRedis;
import cn.idongjia.live.support.JsonUtils;
import cn.idongjia.live.support.redis.RedisTools;
import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import cn.idongjia.user.lib.pojo.vo.CustomerVo;
import cn.idongjia.util.Utils;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lc
 * @create at 2018/7/21.
 */
@Repository("customerVOCache")
public class CustomerVOCache implements BaseRedis<Long, CustomerVo> {

    @Resource
    private RedisTools redisCache;


    private static final String KEY = "live_customer_vo";

    private static final int timeout = 5 * 60;

    private static final Log LOGGER= LogFactory.getLog(CustomerVOCache.class);

    /**
     * 放置缓存
     *
     * @param userId     缓存key
     * @param customerVo 缓存数据
     * @return 是否放置成功
     */
    @Override
    public boolean putRedis(Long userId, CustomerVo customerVo) {
        String buildKey = buildKey(KEY, userId);
        String json     = JsonUtils.toJson(customerVo);
        return redisCache.set(buildKey, json, timeout);
    }

    /**
     * 获取缓存
     *
     * @param userId 缓存key
     * @return 返回缓存数据
     */
    @Override
    public Optional<CustomerVo> takeRedis(Long userId) {
        String buildKey = buildKey(KEY, userId);
        CustomerVo customerVo = redisCache.get(buildKey).map(redisValue -> {
            return JsonUtils.fromJson(redisValue, CustomerVo.class);
        }).orElse(null);
        return Optional.ofNullable(customerVo);
    }


    /**
     * 删除对应数据
     *
     * @param userId 缓存key
     */
    @Override
    public boolean vanishRedis(Long userId) {
        String buildKey = buildKey(KEY, userId);

        return redisCache.del(buildKey);
    }

    @Override
    public Map<Long, CustomerVo> batchGet(List<Long> keys) {
        HashSet h = new HashSet(keys);
        keys.clear();
        keys.addAll(h);
        List<String> redisKeys = keys.stream().map(key -> {
            return buildKey(KEY, key);
        }).collect(Collectors.toList());
        LOGGER.info("CustomerVOCache  keys===>>{}", redisKeys);
        Long startTime1=Utils.getCurrentMillis();
        Map<String, String> redisValueMap = redisCache.pipeGet(redisKeys);
        Long endTime1=Utils.getCurrentMillis();
        LOGGER.info("读取redis用户缓存数据耗时：{}",endTime1-startTime1);
        if (Utils.isEmpty(redisValueMap)) {
            return null;
        }
        LOGGER.info("CustomerVOCache  redisValueMap===>>{}", JSON.toJSONString(redisValueMap));
        Long startTime=Utils.getCurrentMillis();
        Map<Long, CustomerVo> collect = redisValueMap.entrySet().parallelStream().map(x -> {
            String redisValue = x.getValue();
            return JsonUtils.fromJson(redisValue, CustomerVo.class);
        }).collect(Collectors.toMap(CustomerVo::getMainUserId, v1 -> v1, (v1, v2) -> v1));
        Long endTime=Utils.getCurrentMillis();
        LOGGER.info("组装用户缓存数据耗时：{}",endTime-startTime);
        return collect;
    }

    @Override
    public boolean batchSave(Map<Long, CustomerVo> map) {
        Map<String, String> redisMap = map.entrySet().stream().collect(Collectors.toMap(v1 -> {
            Long key = v1.getKey();
            return buildKey(KEY, key);
        }, v1 -> {
            return JsonUtils.toJson(v1.getValue());
        }, (v1, v2) -> v1));
        return redisCache.pipeSet(redisMap);
    }
}
