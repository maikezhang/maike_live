package cn.idongjia.live.restructure.cache.pojo;

import cn.idongjia.live.restructure.redis.BaseRedis;
import cn.idongjia.live.support.redis.RedisTools;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/7/25
 * Time: 上午10:44
 */
@Component
public class TemplateCache implements BaseRedis<Long, String> {


    @Resource
    private RedisTools redisCache;


    private static final String KEY = "live_template_";

    private static final int timeout = 12 * 60 * 60;


    @Override
    public boolean putRedis(Long templateId, String data) {
        String buildKey = buildKey(KEY, templateId);
        return redisCache.set(buildKey, data, timeout);
    }

    @Override
    public Optional<String> takeRedis(Long templateId) {
        String buildKey = buildKey(KEY, templateId);
        String data = redisCache.get(buildKey).orElse(null);
        return Optional.ofNullable(data);
    }

    @Override
    public boolean vanishRedis(Long templateId) {
        String buildKey = buildKey(KEY, templateId);

        return redisCache.del(buildKey);


    }


}
