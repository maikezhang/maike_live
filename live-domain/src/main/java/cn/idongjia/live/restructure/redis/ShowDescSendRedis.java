package cn.idongjia.live.restructure.redis;

import com.google.common.base.Strings;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

/**
 * 是否发送直播简介
 * @author zhang created on 2018/2/8 上午10:20
 * @version 1.0
 */
@Repository
public class ShowDescSendRedis implements BaseRedis<Long, Integer>{

    private static final String SHOW_DESC_KEY_PREFIX = "live.ShowDescSendRedis.";
    @Resource
    private JedisPool cachePool;

    @Override
    public boolean putRedis(Long key, Integer integer) {
        String redisKey = SHOW_DESC_KEY_PREFIX + key;
        if (Objects.isNull(integer)){
            integer = 1;
        }
        try (Jedis jedis = cachePool.getResource()){
            jedis.set(redisKey, integer.toString());
            return true;
        }catch (Throwable t){
            return false;
        }
    }

    @Override
    public Optional<Integer> takeRedis(Long key) {
        String redisKey = SHOW_DESC_KEY_PREFIX + key;
        try (Jedis jedis = cachePool.getResource()){
            String value = jedis.get(redisKey);
            if (! Strings.isNullOrEmpty(value)){
                return Optional.of(Integer.parseInt(value));
            }else{
                return Optional.empty();
            }
        }
    }

    @Override
    public boolean vanishRedis(Long key) {
        return false;
    }
}
