package cn.idongjia.live.restructure.cache.liveHomePage;

import cn.idongjia.live.pojo.homePage.LiveTabCO;
import cn.idongjia.live.pojo.live.LiveModule;
import cn.idongjia.live.support.JsonUtils;
import cn.idongjia.live.support.redis.RedisKey;
import cn.idongjia.live.support.redis.RedisTools;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * Description:
 * Created with IntelliJ IDEA.
 * User: zhangyingjie
 * Date: 2018/7/25
 * Time: 下午5:19
 */
@Component
public class HPTabCache implements RedisKey {

    @Resource
    private RedisTools redisCache;

    private static final String KEY="live_homepage_tab";

    private static final Integer TIMEROUT=30*60;


    public boolean putRedis(List<LiveTabCO> liveTabCOS){
        String buildKey=buildKey(KEY);
        String json= JsonUtils.toJson(liveTabCOS);

        return redisCache.set(buildKey,json,TIMEROUT);
    }

    public Optional<List<LiveTabCO>> takeRedis(){
        String buildKey=buildKey(KEY);

        List<LiveTabCO> liveTabCOS = redisCache.get(buildKey).map(redisCache->{
            return JsonUtils.toList(redisCache,LiveTabCO.class);
        }).orElse(null);

        return Optional.ofNullable(liveTabCOS);


    }

    public boolean vanishRedis(){
        String buildKey=buildKey(KEY);
        return redisCache.del(buildKey);
    }

}
