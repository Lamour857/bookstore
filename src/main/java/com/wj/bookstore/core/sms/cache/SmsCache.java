package com.wj.bookstore.core.sms.cache;

import com.wj.bookstore.common.cache.CommonCache;
import com.wj.bookstore.common.enums.VerifyCodeTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-02-10-11:42
 **/
@Component
public class SmsCache {
    @Autowired
    private CommonCache commonCache;
    // 单位s
    private final long CODE_EXPIRE_TIME=300L;

    private final long COUNT_EXPIRE_TIME=86400L;

    public void cacheVerifyCode(String phone, Integer type, String code)
    {
        VerifyCodeTypeEnum codeType=VerifyCodeTypeEnum.fromCode(type);
        assert codeType != null;
        String codeKey=codeType.getCodePrefix()+phone;
        commonCache.cacheObjectWithExpireTime(codeKey,code,CODE_EXPIRE_TIME);
    }
    public void cacheVerifyCodeCount(String phone, Integer type)
    {
        VerifyCodeTypeEnum codeType=VerifyCodeTypeEnum.fromCode(type);
        assert codeType != null;
        String countKey=codeType.getCountPrefix()+phone;
        commonCache.cacheObjectWithExpireTime(countKey,0,COUNT_EXPIRE_TIME);
    }
    public void increaseVerifyCodeCount(String phone, Integer type)
    {
        VerifyCodeTypeEnum codeType=VerifyCodeTypeEnum.fromCode(type);
        assert codeType != null;
        String countKey=codeType.getCountPrefix()+phone;
        commonCache.increase(countKey);
    }
    public String getVerifyCode(String phone, Integer type)
    {
        VerifyCodeTypeEnum codeType=VerifyCodeTypeEnum.fromCode(type);
        assert codeType != null;
        String codeKey=codeType.getCodePrefix()+phone;
        return commonCache.getObject(codeKey,String.class);
    }
    public Integer getVerifyCodeCount(String phone, Integer type)
    {
        VerifyCodeTypeEnum codeType=VerifyCodeTypeEnum.fromCode(type);
        assert codeType != null;
        String countKey=codeType.getCountPrefix()+phone;
        return commonCache.getObject(countKey,Integer.class);
    }
}
