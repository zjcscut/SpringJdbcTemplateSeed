package cn.zjc.jedis;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangjinci
 * @version 2016/8/25 17:58
 * @function 抽象Jedis服务类(普通同步方式)
 */
@Component
public abstract class AbstractJedisService<K, V> implements InitializingBean {

    @Autowired
    private RedisTemplate<K, V> redisTemplate;

    private ValueOperations<K, V> operations;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.operations = redisTemplate.opsForValue();
        redisTemplate.setKeySerializer(new StringRedisSerializer());  //设置key序列化器
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(getValueType()));  //设置value序列化器
    }

    public AbstractJedisService() {

    }

    abstract protected Class<V> getValueType();

    /**
     * 返回旧的值并且设置K-V
     *
     * @param key
     * @param value
     * @return
     */
    public V getAndSet(K key, V value) {
        return operations.getAndSet(key, value);
    }

    /**
     * 设置K-V，长期生存(不建议使用)
     *
     * @param key
     * @param value
     */
    public void set(K key, V value) {
        operations.set(key, value);
    }

    /**
     * 设置K-V，包括生存策略
     *
     * @param key
     * @param value
     * @param expire
     * @param unit
     */
    public void set(K key, V value, long expire, TimeUnit unit) {
        operations.set(key, value, expire, unit);
    }

    /**
     * 设置K-V，包括生存策略为秒
     *
     * @param key
     * @param value
     * @param expireSeconds
     */
    public void set(K key, V value, long expireSeconds) {
        this.set(key, value, expireSeconds, TimeUnit.SECONDS);
    }

    /**
     * 根据K获取V
     *
     * @param key
     * @return
     */
    public V get(K key) {
        return operations.get(key);
    }

    /**
     * 根据keys获取List<Value>
     *
     * @param keys
     * @return
     */
    public List<V> get(Collection<K> keys) {
        return operations.multiGet(keys);
    }


    /**
     * 设置Key的生存周期
     *
     * @param key
     * @param expires
     * @param timeUnit
     * @return
     */
    public Boolean expire(K key, long expires, TimeUnit timeUnit) {
        return redisTemplate.expire(key, expires, timeUnit);
    }

    /**
     * 设置Key的生存周期,单位为秒
     *
     * @param key
     * @return
     */
    public Boolean expire(K key, long expireSeconds) {
        return redisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS);
    }

    /**
     * 按照日期设置Key的生存周期
     *
     * @param key
     * @param expireDate
     * @return
     */
    public Boolean expireAt(K key, Date expireDate) {
        return redisTemplate.expireAt(key, expireDate);
    }

    /**
     * 根据key删除K-V
     *
     * @param key
     */
    public void del(K key) {
        redisTemplate.delete(key);
    }

    /**
     * 根据keys删除K-V
     *
     * @param keys
     */
    public void del(Collection<K> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 匹配删除
     *
     * @param pattern
     */
    public void delByPattern(K pattern) {
        Set<K> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 根据key返回其过期时间(时间戳)
     *
     * @param key
     * @return
     */
    public Long getExpireByKey(K key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 根据key返回其过期时间(时间模式自定义)
     *
     * @param key
     * @return
     */
    public Long getExpireByKey(K key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 根据key判断key-value是否存在
     *
     * @param key
     * @return
     */
    public Boolean existsKey(K key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 重命名K-V
     *
     * @param oldKey
     * @param newKey
     * @return
     */
    public Boolean rename(K oldKey, K newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

}
