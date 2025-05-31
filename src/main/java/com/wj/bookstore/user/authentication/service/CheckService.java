package com.wj.bookstore.user.authentication.service;


import com.wj.bookstore.common.cache.CommonCache;
import com.wj.bookstore.common.enums.StatusEnum;
import com.wj.bookstore.common.enums.VerifyCodeTypeEnum;
import com.wj.bookstore.common.utils.ExceptionUtil;

import com.wj.bookstore.core.sms.cache.SmsCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-15-14:36
 **/
@Service
public class CheckService {
    @Autowired
    private SmsCache smsCache;

    public  boolean checkVerifyCode(String phone, int type, String verifyCode) {
        VerifyCodeTypeEnum codeType=VerifyCodeTypeEnum.fromCode(type);
        if(codeType==null){
            throw ExceptionUtil.ofAuthenticationException(StatusEnum.VERIFY_CODE_TYPE_ERROR);
        }
        String codePrefix=codeType.getCodePrefix()+phone;
        String code= smsCache.getVerifyCode(codePrefix, type);
        if(code!=null&&code.equals(verifyCode)){
            return true;
        }else{
            throw ExceptionUtil.ofAuthenticationException(StatusEnum.VERIFY_CODE_NOT_EXIST);
        }
    }
}
