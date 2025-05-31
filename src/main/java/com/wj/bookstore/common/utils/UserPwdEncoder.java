package com.wj.bookstore.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-15-14:21
 **/
@Component
public class UserPwdEncoder {
    @Value("${security.salt}")
    private String salt;

    @Value("${security.salt-index}")
    private Integer saltIndex;


    public boolean match(String plainPwd,String encPwd){
        return Objects.equals(encPwd(plainPwd),encPwd);
    }
    // 密码明文处理
    public String encPwd(String plainPwd){
        if(plainPwd.length()>saltIndex){
            plainPwd=plainPwd.substring(0,saltIndex)+salt+plainPwd.substring(saltIndex);
        }else{
            plainPwd=plainPwd+salt;
        }
        return DigestUtils.md5DigestAsHex(plainPwd.getBytes(StandardCharsets.UTF_8));
    }
}
