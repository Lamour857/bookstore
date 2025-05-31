package com.wj.bookstore.core.sms;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-14-15:14
 **/
public interface SmsService {
    // 一天最大发送次数
    int MAX_SEND_TIMES=3;

    void sendVerifyCode(String phone,Integer type);

}
