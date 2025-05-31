package com.wj.bookstore.core.sms.impl;


import com.wj.bookstore.common.enums.StatusEnum;
import com.wj.bookstore.common.utils.ExceptionUtil;
import com.wj.bookstore.core.sms.SendMessageService;
import com.wj.bookstore.core.sms.SmsService;
import com.wj.bookstore.core.sms.cache.SmsCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author wujia
 * @description: 短信服务类
 * 一个手机号登录和注册一天分别只能发送三次验证码
 * 验证码五分钟内有效
 * 发送间隔为一分钟, 可在前端和短信api设置该限制
 * @createTime: 2024-12-14-15:22
 **/
@Service
public class SmsServiceImpl implements SmsService {
    @Autowired
    private SmsCache smsCache;
    @Autowired
    private SendMessageService sendMessageService;

    @Override
    public void sendVerifyCode(String phone, Integer type) {
        String verifyCode= String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        if(smsCache.getVerifyCodeCount(phone,type)==null){
            // 首次发送验证码
            smsCache.cacheVerifyCodeCount(phone,type);
            smsCache.cacheVerifyCode(phone,type, verifyCode);
            // 发送短信
            sendMessageService.sendMessage(phone,verifyCode,type);
        }else{
            int count=smsCache.getVerifyCodeCount(phone,type);
            if(count>3){
                // 一天发送验证码次数超过三次
                throw ExceptionUtil.of(StatusEnum.VERIFY_CODE_SEND_FREQUENT,phone);
            }else{
                // 验证码次数少于三次
                smsCache.increaseVerifyCodeCount(phone,type);
                smsCache.cacheVerifyCode(phone,type, verifyCode);
                // 发送短信
                sendMessageService.sendMessage(phone,verifyCode,type);
            }
        }
    }


}
