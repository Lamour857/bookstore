package com.wj.bookstore.common.cache;



import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-14-13:50
 **/
@Service
@Slf4j
public class CommonCache {
    private static final String KEY_PREFIX="bookstore_";
    @Autowired
    private StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String generateKey(String key) {
        return KEY_PREFIX + key;
    }
    // 缓存对象
    public <T> void cacheObject(String key, T object) {
        try {
            String json = objectMapper.writeValueAsString(object);
            redisTemplate.opsForValue().set(generateKey(key), json);
        } catch (IOException  e) {
            log.warn("Failed to cache object: {}", e.getMessage());
        }
    }
    // 带过期时间的缓存对象
    public <T> void cacheObjectWithExpireTime(String key, T object, long expireTime) {
        try {
            String json = objectMapper.writeValueAsString(object);
            redisTemplate.opsForValue().set(generateKey(key), json, expireTime, TimeUnit.SECONDS);
        } catch (IOException e) {
            log.warn("Failed to cache object with expire time: {}", e.getMessage());
        }
    }
    // 缓存哈希对象
    public <T> void cacheHashObject(String key,String hashKey, T object) {
        try {
            String json = objectMapper.writeValueAsString(object);
            redisTemplate.opsForHash().put(generateKey(key),hashKey,json);
        } catch (IOException e) {
            log.warn("Failed to cache Hash object: {}", e.getMessage());
        }
    }
    public <T> void cacheSetObject(String key, T object) {
        try {
            String json = objectMapper.writeValueAsString(object);
            redisTemplate.opsForSet().add(generateKey(key),json);
        } catch (IOException e) {
            log.warn("Failed to cache Set object: {}", e.getMessage());
        }
    }
    public <T> void cacheListObject(String key, List<T> list) {
        list.forEach(
                object -> {
                    try {
                        String json = objectMapper.writeValueAsString(object);
                        redisTemplate.opsForList().rightPush(generateKey(key), json);
                    } catch (IOException e) {
                        log.warn("Failed to cache List object: {}", e.getMessage());
                    }
                }
        );
    }
    public <T> void cacheObjectIntoList(String key, Object object) {
        try {
            String json = objectMapper.writeValueAsString(object);
            redisTemplate.opsForList().rightPush(generateKey(key), json);
        } catch (IOException e) {
            log.warn("Failed to cache object into List: {}", e.getMessage());
        }

    }
    // 获取对象
    public <T> T getObject(String key, Class<T> clazz) {
        String json = redisTemplate.opsForValue().get(generateKey(key));
        if (json != null) {
            try {
                return objectMapper.readValue(json, clazz);
            } catch (IOException e) {
                log.warn("Failed to get object: {}", e.getMessage());
                return null;
            }

        }
        return null;
    }
    // 获取hash对象
    public Object getHashObject(String key, String hashKey) {
        return redisTemplate.opsForHash().get(generateKey(key),hashKey);
    }
    public <T> List<T> getListObject(String key, Class<T> clazz) {
        if (Boolean.FALSE.equals(redisTemplate.hasKey(generateKey(key)))) {
            log.info("Key bookstore_{} does not exist in Redis", key);
            return null;
        }
        List<String> jsonList = redisTemplate.opsForList().range(generateKey(key), 0, -1);
        List<T> resultList = new ArrayList<>();
        assert jsonList != null;
        for (String json : jsonList) {
            try {
                T object = objectMapper.readValue(json, clazz);
                resultList.add(object);
            } catch (IOException e) {
                log.warn("Failed to get object from list: {}", e.getMessage());
            }
        }
        return resultList;
    }
    public Set<String> getSetObject(String key) {
        return redisTemplate.opsForSet().members(generateKey(key));

    }

    // 获取对象（复杂类型）
    public <T> T getObject(String key, TypeReference<T> typeReference) {
        String json = redisTemplate.opsForValue().get(generateKey(key));
        if (json != null) {
            try {
                return objectMapper.readValue(json, typeReference);
            } catch (IOException e) {
                log.warn("Failed to get TypeReference object: {}", e.getMessage());
            }
        }
        return null;
    }

    // 删除缓存
    public void deleteCache(String key) {
        redisTemplate.delete(generateKey(key));
    }
    public void deleteObjectFromSet(String key,Object object) {
        try {
            String json = objectMapper.writeValueAsString(object);
            redisTemplate.opsForSet().remove(generateKey(key),json);
        } catch (IOException e) {
            log.warn("Failed to delete object from set: {}", e.getMessage());
        }
    }
    public void deleteObjectFromList(String key,Object object) {
        try{
            String json = objectMapper.writeValueAsString(object);
            redisTemplate.opsForList().remove(generateKey(key),0,json);
        }catch (IOException e){
            log.warn("Failed to delete object from list: {}", e.getMessage());
        }

    }
    public void deleteWithPrefix(String prefix){
        log.info("删除具有该前缀的缓存:{}",prefix);
        String bookstorePrefix=generateKey(prefix);
        Set<String> keys = redisTemplate.keys(bookstorePrefix + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }


    public Long increase(String countKey) {

        return redisTemplate.opsForValue().increment(countKey);
    }



}
