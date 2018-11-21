package cn.idongjia.live.restructure.cache.liveHomePage;

import cn.idongjia.live.pojo.homePage.LiveBannerCO;
import cn.idongjia.live.pojo.live.LiveModule;
import cn.idongjia.live.restructure.dto.LiveBannerDTO;
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
 * Time: 下午5:18
 */
@Component
public class HPBannerCache implements RedisKey {

    @Resource
    private RedisTools redisCache;

    private static final String KEY="live_homepage_banner";

    private static final String newVersion="newVersion";

    private static final Integer TIMEROUT=30*60;


    public boolean putRedis(List<LiveBannerCO> liveBannerCOS,boolean isNewVersion){
        String key;
        if(isNewVersion) {
            key = KEY + newVersion;
        }else{
            key=KEY;
        }

        String buildKey=buildKey(key);
        String json= JsonUtils.toJson(liveBannerCOS);

        return redisCache.set(buildKey,json,TIMEROUT);
    }

    public Optional<List<LiveBannerCO>> takeRedis(boolean isNewVersion){
        String key;
        if(isNewVersion) {
            key = KEY + newVersion;
        }else{
            key=KEY;
        }

        String buildKey=buildKey(key);

        List<LiveBannerCO> liveBannerCOS = redisCache.get(buildKey).map(redisCache->{
            return JsonUtils.toList(redisCache,LiveBannerCO.class);
        }).orElse(null);

        return Optional.ofNullable(liveBannerCOS);


    }

    public boolean vanishRedis(){
        String buildnewKey=buildKey(KEY+newVersion);
        redisCache.del(buildnewKey);

        String buildKey=buildKey(KEY);
        return redisCache.del(buildKey);
    }
}
