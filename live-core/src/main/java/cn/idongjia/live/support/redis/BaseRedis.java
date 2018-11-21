package cn.idongjia.live.support.redis;


import java.util.Optional;

/**
 * redisDB基础接口
 *
 * @author zhang created on 2017/9/7 上午10:38
 * @version 1.0
 */
public interface BaseRedis<K, V>{

    /**
     * 放置缓存
     *
     * @param key        缓存key
     * @param v          缓存数据
     * @return 是否放置成功
     */
    boolean putRedis(K key, V v);

    /**
     * 获取缓存
     *
     * @param key 缓存key
     * @return 返回缓存数据
     */
    Optional<V> takeRedis(K key);


    /**
     * 删除对应数据
     *
     * @param key 缓存key
     */
    boolean vanishRedis(K key);
}
