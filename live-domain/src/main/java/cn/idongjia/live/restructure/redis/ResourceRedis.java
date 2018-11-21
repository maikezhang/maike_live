package cn.idongjia.live.restructure.redis;

import cn.idongjia.log.Log;
import cn.idongjia.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.Optional;

/**
 * 资源db
 */
@Repository
public class ResourceRedis implements BaseRedis<String,Map<String,String>>{

    private static final Log LOG= LogFactory.getLog(ResourceRedis.class);

    private static final String CACHE_KEY = "live.main.resource_";

    @Autowired
    private JedisPool cachePool;

    @Override
    public boolean putRedis(String key,Map<String,String> s) {
        try(Jedis jedis = cachePool.getResource()){
            jedis.hmset(CACHE_KEY+key,s);
            return true;
        }catch (Throwable t){
            LOG.warn("设置主推缓存失败: " + t.getMessage());
            return false;
        }
    }

    @Override
    public Optional<Map<String,String>> takeRedis(String key) {
        try(Jedis jedis = cachePool.getResource()){
            return Optional.of(jedis.hgetAll(CACHE_KEY+key));
        }catch (Throwable t){
            LOG.warn("获取主推缓存失败: " + t.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean vanishRedis(String key) {
        try(Jedis jedis = cachePool.getResource()){
            jedis.del(CACHE_KEY+key);
            return true;
        }catch (Throwable t){
            LOG.warn("删除主推缓存失败: " + t.getMessage());
            return false;
        }

    }

    /**
     * 删除某个field
     * @param key
     * @param rtype
     * @return
     */
    public boolean delField(String key,String rtype){
        try(Jedis jedis = cachePool.getResource()){
            jedis.hdel(CACHE_KEY+key,rtype);
            return true;
        }catch (Throwable t){
            LOG.warn("删除主推缓存失败: " + t.getMessage());
            return false;
        }
    }

}
