package com.xurent.live.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.TimeUnit;


/**
 * @Author: HXR
 * @Date: 2019/8/1 11:28
 * @Info:
 */
@Slf4j
@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;



    /**
     * 指定缓存失效时间
     * @param key key 键
     * @param time time 时间(秒)
     * @return
     */
    public  boolean expire(String key,long time){
        try{
            if(time>0){
                redisTemplate.expire(key,time, TimeUnit.SECONDS);
            }
            return  true;
        }catch (Exception e){
            e.printStackTrace();
            log.error("Redis set error, key:{}",key);
            return  false;
        }
    }

    /**
     * 根据key 获取过期时间
     * @param key
     * @return 时间(秒) 返回0代表为永久有效
     */
    public  long getExpire(String key){
        return  redisTemplate.getExpire(key,TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public boolean hasKey(String key){
        try {
            return  redisTemplate.hasKey(key);
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }
    }

    /**
     * 删除存储
     * @param key
     */
    public void del(String... key){
        if(key!=null&&key.length>0){
            if(key.length==1){
                redisTemplate.delete(key[0]);
            }else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 获取缓存
     * @param key
     * @return
     */
    public Object get(String key){
        return  key==null?null:redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置缓存
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key,Object value){
        try {
            redisTemplate.opsForValue().set(key,value);
            return  true;
        }catch (Exception e){
            log.error("Redis set error, key:{} value:{}",key,value);
            e.printStackTrace();
            return  false;
        }
    }

    /**
     * 设置缓存并设置时间
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean set(String key,Object value,long time){
        try {
            if(time>0){
                redisTemplate.opsForValue().set(key,value,time,TimeUnit.SECONDS);
            }else {
                set(key,value);
            }
            return  true;
        }catch (Exception e){
            log.error("Redis set error, key:{} value:{}",key,value);
            e.printStackTrace();
            return  false;
        }
    }

    /**
     * 根据key删除缓存
     * @param key
     * @return
     */
    public  boolean remove(String key){
        return  redisTemplate.delete(key);
    }

    /**
     * 移除值为value的
     * @param key
     * @param values
     * @return
     */
    public  long removeValue(String key, Object... values){
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return  count;
        }catch (Exception e){
            log.error("Redis remove error, key:{} values:{}",key,values);
            e.printStackTrace();
            return  0;
        }
    }

}
