package com.wj.bookstore.core.sms.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.wj.bookstore.common.enums.StatusEnum;
import com.wj.bookstore.common.enums.VerifyCodeTypeEnum;
import com.wj.bookstore.common.utils.ExceptionUtil;
import com.wj.bookstore.common.utils.SpringUtil;
import com.wj.bookstore.core.sms.SendMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * @author wujia
 * @description: 发送短信验证码
 * @createTime: 2024-12-14-15:23
 **/
@Slf4j
@Service
public class SendMessageServiceImpl implements SendMessageService {

    @Value("${message.accessKey}")
    private String accessKeyId;
    @Value("${message.accessKeySecret}")
    private String accessKeySecret;
    @Value("${message.endpoint}")
    private String endPoint;
    @Value("${message.signName}")
    private  String signName;
    private  Client client;
    @PostConstruct
    public void init()  {
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret)
                .setEndpoint(endPoint);
        try {
            this.client = new Client(config);
        } catch (Exception e) {
            throw ExceptionUtil.of(StatusEnum.UNEXPECT_ERROR,"短信客户端创建失败");
        }
    }
    @Override
    public void sendMessage(String phone,String verifyCode , Integer templateType) {
        // 将整数值转为枚举类型
        VerifyCodeTypeEnum type=VerifyCodeTypeEnum.fromCode(templateType);
        SendSmsRequest smsRequest=new SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName(signName)
                .setTemplateParam("{\"code\":\""+verifyCode+"\"}");
        switch(Objects.requireNonNull(type)){
            case LOGIN:
                smsRequest.setTemplateCode(SpringUtil.getConfig("message.template.login"));
                break;
            case REGISTER:
                smsRequest.setTemplateCode(SpringUtil.getConfig("message.template.register"));
                break;
            case CHANGE_PASSWORD:
                smsRequest.setTemplateCode(SpringUtil.getConfig("message.template.password"));
                break;
            default:
                throw ExceptionUtil.of(StatusEnum.VERIFY_CODE_TYPE_ERROR);
        }
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = client.sendSms(smsRequest);
        } catch (Exception e) {
            throw ExceptionUtil.of(StatusEnum.SEND_MESSAGE_FAIL,e.getMessage());
        }
        log.info("smsResponse:{}", sendSmsResponse.body.getMessage());
        String message = sendSmsResponse.body.getMessage();
        if(message!=null){
            if(message.startsWith("触发分钟级流控")){
                throw ExceptionUtil.of(StatusEnum.VERIFY_CODE_SEND_FREQUENT,phone);
            }
        }else{
            throw ExceptionUtil.of(StatusEnum.SEND_MESSAGE_FAIL);
        }
    }
}
