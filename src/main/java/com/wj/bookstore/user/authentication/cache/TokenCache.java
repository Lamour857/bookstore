package com.wj.bookstore.user.authentication.cache;

import com.wj.bookstore.common.cache.CommonCache;
import com.wj.bookstore.user.account.entity.DO.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-02-10-11:21
 **/
@Component
public class TokenCache {
    @Autowired
    private CommonCache commonCache;
    public static final String TOKEN_CACHE_KEY = "token_";
    private String generateKey(String key){
        return TOKEN_CACHE_KEY + key;
    }
    // // key: token value: userDO
    public void cacheToken(String token, UserDO user, long expiration) {
        commonCache.cacheObjectWithExpireTime(generateKey(token), user, expiration/1000);
    }
    public UserDO getUserByToken(String token) {
        return commonCache.getObject(generateKey(token),UserDO.class);
    }
    public void deleteToken(String token) {
        commonCache.deleteCache(generateKey(token));
    }

}
