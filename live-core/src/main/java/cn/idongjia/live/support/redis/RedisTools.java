package cn.idongjia.live.support.redis;

import cn.idongjia.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author lc
 */
public class RedisTools {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisTools.class);

    private JedisPool jedisPool;

    private static final String LOCK_SUCCESS         = "OK";
    private static final String SET_IF_NOT_EXIST     = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    public RedisTools(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * 缓存给定key对应的值，并设置超时时间 如果给定的话
     *
     * @param key     key
     * @param value   value
     * @param timeout 超时时间（秒）
     */
    public String set(byte[] key, byte[] value, Integer timeout) {
        try (Jedis jedis = jedisPool.getResource()) {
            String result = jedis.set(key, value);
            if (timeout != null && timeout != 0) {
                jedis.expire(key, timeout);
            }
            return result;
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public Long expire(String key,Integer timeout){
        Long result=null;
        if(timeout==null || timeout==0){
            return result;
        }

        try (Jedis jedis = jedisPool.getResource()) {
            result=jedis.expire(key,timeout);
        }catch (Throwable e){
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 批量写缓存
     *
     * @param map 缓存数据
     * @return 是否成功
     */
    public boolean pipeSetBytes(Map<byte[], byte[]> map) {
        try (Jedis jedis = jedisPool.getResource()) {
            Pipeline pipeline = jedis.pipelined();
            for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {
                pipeline.set(entry.getKey(), entry.getValue());
            }
            pipeline.sync();
            return true;
        } catch (Throwable t) {
            LOGGER.warn("批量写bytes缓存失败: " + t.getMessage());
            return false;
        }
    }

    /**
     * 缓存给定key对应的值，并设置超时时间 如果给定的话
     *
     * @param key     key
     * @param value   value
     * @param timeout 超时时间（秒）
     */
    public boolean set(String key, String value, Integer timeout) {
        try (Jedis jedis = jedisPool.getResource()) {
            String code = jedis.set(key, value);
            if (timeout != null && timeout != 0) {
                jedis.expire(key, timeout);
            }
            return "OK".equalsIgnoreCase(code);
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }


    /**
     * 获取key对应的值
     *
     * @param key key
     * @return byte[] key中的数据
     */
    public byte[] get(byte[] key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public Optional<String> get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return Optional.ofNullable(jedis.get(key));
        } catch (Throwable e) {
            LOGGER.error("redis获取数据失败: " + e);
            return Optional.empty();
        }
    }

    public Map<String, String> pipeGet(List<String> keys) {
        try (Jedis jedis = jedisPool.getResource()) {
            Pipeline            pipeline  = jedis.pipelined();
            Map<String, String> responses = new HashMap<>();
            for (String key : keys) {
                pipeline.get(key);
            }
            List<Object> objects = pipeline.syncAndReturnAll();
            LOGGER.info("redis pipeLine读取数据:{}",objects);
            for (int i = 0; i < keys.size(); i++) {
                if(objects.get(i)!=null) {
                    responses.put(keys.get(i), objects.get(i).toString());
                }
            }
            return responses;
        } catch (Throwable t) {
            LOGGER.warn("redis pipeLine读取数据失败:{}",t);
            return new HashMap<>();
        }
    }

    /**
     * 批量写缓存数据
     *
     * @param dataMap 数据字典
     * @return 是否成功
     */
    public boolean pipeSet(Map<String, String> dataMap) {
        try (Jedis jedis = jedisPool.getResource()) {
            Pipeline pipeline = jedis.pipelined();
            pipeline.sync();
            for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                pipeline.set(entry.getKey(), entry.getValue());
            }
            return true;
        } catch (Throwable t) {
            LOGGER.warn("redis pipeLine写入数据失败");
            return false;
        }
    }


    public boolean pipeDel(List<String> keys) {
        try (Jedis jedis = jedisPool.getResource()) {
            Pipeline pipeline = jedis.pipelined();
            pipeline.sync();
            for (String key : keys) {
                pipeline.del(key);
            }
            return true;
        } catch (Throwable t) {
            LOGGER.warn("redis pipeLine删除数据失败");
            return false;
        }
    }

    /**
     * 批量lpop
     * @param keys
     * @return
     */
    public List<String> pipeLpop(List<String> keys) {
        try (Jedis jedis = jedisPool.getResource()) {
            Pipeline pipeline = jedis.pipelined();
            for (String key : keys) {
                pipeline.lpop(key);
            }
            List<Object> objects = pipeline.syncAndReturnAll();
            return objects.stream().map(o -> {
                return (String)o;
            }).collect(Collectors.toList());

        } catch (Throwable t) {
            LOGGER.warn("redis pipeLine删除数据失败");
            return null;
        }
    }

    public List<String> mGet(List<String> keys) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.mget(keys.toArray(new String[keys.size()]));
        } catch (Throwable e) {
            LOGGER.warn("redis批量获取数据失败: " + e);
            return new ArrayList<>();
        }
    }

    public void mSet(Map<String, String> dataMap) {
        List<String> batchString = new ArrayList<>();
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            batchString.add(entry.getKey());
            batchString.add(entry.getValue());
        }
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.mset(batchString.toArray(new String[batchString.size()]));
        } catch (Exception e) {
            LOGGER.warn("redis批量修改数据失败: " + e);
        }
    }

    /**
     * @param key
     * @param value
     * @param seconds
     * @param repeatcount 重复次数
     * @return
     */
    public boolean lock(String key, String value, Integer seconds, int repeatcount) {

        while (repeatcount-- > 0) {
            if (lock(key, value, seconds)) {
                return true;
            }
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                LOGGER.warn("获取锁失败{}", e);
                return false;
            }
        }
        return false;
    }

    /**
     * 当key不存在时 设置value
     *
     * @param key     key
     * @param value   value
     * @param seconds 超时时间 默认为秒
     * @return 是否保存成功
     */
    public boolean lock(String key, String value, Integer seconds) {
        try (Jedis jedis = jedisPool.getResource()) {

            String result = jedis.set(key, value, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, seconds);
            if (LOCK_SUCCESS.equalsIgnoreCase(result)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除key
     *
     * @param key key
     */
    public boolean del(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            Long value = jedis.del(key);
            if (Objects.isNull(value) || value < 1) {
                LOGGER.warn("删除对应key失败: " + key);
                return false;
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("删除对应key失败: " + key + " 错误: " + e);
            return false;
        }
    }

    /**
     * 向set添加数据
     *
     * @param key    key
     * @param values values
     * @return 添加成功条数
     */
    public Long saveSet(String key, String... values) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.sadd(key, values);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * lpush 数据
     *
     * @param key    key
     * @param values values
     * @return 添加成功条数
     */
    public Long lpush(String key, String... values) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lpush(key, values);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    // TODO 临时修改，下周修改
    public Long feedsLpush(String key, String... values) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(4);
            return jedis.lpush(key, values);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * lpop数据
     *
     * @param key key
     * @return 第一条数据
     */
    public String lpop(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lpop(key);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * rpush数据
     *
     * @param key    key
     * @param values values
     * @return 写入成功条数
     */
    public Long rpush(String key, String... values) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.rpush(key, values);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * rpop数据
     *
     * @param key key
     * @return right第一条数据
     */
    public String rpop(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.rpop(key);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 从list中获取数据
     *
     * @param key   key
     * @param start 开始位置
     * @param end   结束位置 -1表示所有
     * @return start到end之间的数据
     */
    public List<String> getFromList(String key, int start, int end) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 从set中获取数据
     *
     * @param key key
     * @return key对应的set
     */
    public Set<String> getFromSet(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.smembers(key);
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public String getOneSet(String key){
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.spop(key);
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 从set中删除某些值
     *
     * @param key     key
     * @param members 要删除的值
     * @return 删除成功条数
     */
    public Long deleteInSet(String key, String... members) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.srem(key, members);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 判断set中是否存在给定的值
     *
     * @param key   key
     * @param value value
     * @return 是否存在
     */
    public Boolean exist(String key, String value) {

        try (Jedis jedis = jedisPool.getResource()) {
            if (Utils.isEmpty(value)) {
                return jedis.exists(key);
            } else {
                return jedis.sismember(key, value);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    public boolean hset(String key, String field, String value, Integer expireTime) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(key, field, value);
            if (!Objects.isNull(expireTime) && expireTime != 0) {
                jedis.expire(key, expireTime);
            }
            return true;
        } catch (Exception e) {
            LOGGER.warn("保存redis失败: " + key);
            return false;
        }
    }

    public boolean setMap(String key, Map<String, String> map, Integer expireTime) {
        try (Jedis jedis = jedisPool.getResource()) {
            String result = jedis.hmset(key, map);
            if (expireTime != null && expireTime != 0) {
                jedis.expire(key, expireTime);
            }
            return "OK".equalsIgnoreCase(result);
        } catch (Exception e) {
            LOGGER.warn("保存redis出错: " + e);
            return false;
        }
    }

    public Optional<Map<String, String>> getMap(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return Optional.ofNullable(jedis.hgetAll(key));
        } catch (Exception e) {
            LOGGER.warn("读取redis出错: " + e);
            return Optional.empty();
        }
    }


    /**
     * 获取key的类型
     *
     * @param key key
     * @return 类型
     */
    public String getType(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.type(key);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }


    public int count(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            Long pcount = jedis.scard(key);
            pcount = pcount == null ? 0 : pcount;
            return pcount.intValue();
        }
    }


    public boolean saveHash(String key, Map<String, String> map) {
        try (Jedis jedis = jedisPool.getResource()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                jedis.hset(key, entry.getKey(), entry.getValue());
            }
            return true;
        } catch (Exception e) {
            LOGGER.warn("设置redis失败: " + e);
            return false;
        }
    }

    public Map<String, String> getHash(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hgetAll(key);
        } catch (Exception e) {
            LOGGER.warn("读取redis失败: " + e);
            return new HashMap<>();
        }
    }

    public String getHash(String key, String field) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hget(key, field);
        } catch (Exception e) {
            LOGGER.warn("读取redis失败: " + e);
            return null;
        }
    }


    public Optional<Set<String>> listAllKeys(String keyPattern) {
        try (Jedis jedis = jedisPool.getResource()) {
            return Optional.ofNullable(jedis.keys(keyPattern));
        } catch (Exception e) {
            LOGGER.warn("读取redis失败: " + e);
            return Optional.empty();
        }
    }

    /**
     * 批量lpop
     * @return
     */
    public List<String> pipeLpopByCount(String key,Integer count) {
        try (Jedis jedis = jedisPool.getResource()) {
            Pipeline pipeline = jedis.pipelined();
            for (int i=0;i<count;i++) {
                pipeline.lpop(key);
            }
            List<Object> objects = pipeline.syncAndReturnAll();
            return objects.stream().map(o -> {
                return (String)o;
            }).filter(x->x!=null).collect(Collectors.toList());

        } catch (Throwable t) {
            LOGGER.warn("redis pipeLine删除数据失败");
            return null;
        }
    }

    /**
     * 批量lpush
     * @return
     */
    public boolean pipeLpush(String key,List<String> values) {
        try (Jedis jedis = jedisPool.getResource()) {
            Pipeline pipeline = jedis.pipelined();
            pipeline.sync();
            for (String value : values) {
                pipeline.lpush(key,value);
            }
            return true;

        } catch (Throwable t) {
            LOGGER.warn("redis pipeLine删除数据失败");
            return false;
        }
    }


}
