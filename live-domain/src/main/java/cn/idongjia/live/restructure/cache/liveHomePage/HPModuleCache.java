package cn.idongjia.live.restructure.cache.liveHomePage;

import cn.idongjia.live.pojo.live.LiveModule;
import cn.idongjia.live.restructure.redis.BaseRedis;
import cn.idongjia.live.support.JsonUtils;
import cn.idongjia.live.support.redis.RedisKey;
import cn.idongjia.live.support.redis.RedisTools;
import org.apache.zookeeper.KeeperException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/7/25
 * Time: 下午3:53
 */
@Component
public class HPModuleCache  implements RedisKey {


    @Resource
    private RedisTools redisCache;

    private static final String KEY="live_homepage_module";

    private static final Integer TIMEROUT=30*60;


    public boolean putRedis(List<LiveModule> modules){
        String buildKey=buildKey(KEY);
        String json= JsonUtils.toJson(modules);

        return redisCache.set(buildKey,json,TIMEROUT);
    }

    public Optional<List<LiveModule>> takeRedis(){
        String buildKey=buildKey(KEY);

        List<LiveModule> liveModules = redisCache.get(buildKey).map(redisCache->{
            return JsonUtils.toList(redisCache,LiveModule.class);
        }).orElse(null);

        return Optional.ofNullable(liveModules);


    }

    public boolean vanishRedis(){
        String buildKey=buildKey(KEY);
        return redisCache.del(buildKey);
    }

}
