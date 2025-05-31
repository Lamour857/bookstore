package com.wj.bookstore.core.sms;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-02-10-12:56
 **/
public interface SendMessageService {
    void sendMessage(String phone,String verifyCode , Integer templateType);
}
