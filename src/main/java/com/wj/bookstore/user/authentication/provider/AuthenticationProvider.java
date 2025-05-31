package com.wj.bookstore.user.authentication.provider;



import com.wj.bookstore.common.enums.StatusEnum;
import com.wj.bookstore.common.enums.VerifyCodeTypeEnum;
import com.wj.bookstore.common.utils.ExceptionUtil;
import com.wj.bookstore.user.authentication.service.CheckService;
import com.wj.bookstore.user.authentication.service.UserDetailsService;
import com.wj.bookstore.user.authentication.token.PhonePasswordAuthenticationToken;
import com.wj.bookstore.user.authentication.token.VerifyCodeAuthenticationToken;
import com.wj.bookstore.user.account.entity.DO.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author wujia
 * @description: 手机号密码登录验证
 * @createTime: 2024-12-19-21:39
 **/
@Slf4j
@Component
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private CheckService checkService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDO userDetails = null;
        // 系统现有登录方式的用户principal为电话
        String phone=(String) authentication.getPrincipal();

        String role=null;

        userDetails= userDetailsService.loadUserByPhone(phone);
        if(userDetails==null){
            throw ExceptionUtil.ofAuthenticationException(StatusEnum.USER_NOT_EXISTS);
        }
        try{
            if(authentication instanceof PhonePasswordAuthenticationToken){
                // 密码匹配
                PhonePasswordAuthenticationToken authenticationToken=(PhonePasswordAuthenticationToken) authentication;
                String password=authentication.getCredentials().toString();
                if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
                    throw ExceptionUtil.ofAuthenticationException(StatusEnum.USER_PWD_ERROR);
                }
                // 匹配成功返回token
                authenticationToken.setAuthenticated(true);
                authenticationToken.setUser(userDetails);
                return authentication;
            }else if(authentication instanceof VerifyCodeAuthenticationToken){
                String verifyCode=(String)authentication.getCredentials();
                VerifyCodeAuthenticationToken authenticationToken=(VerifyCodeAuthenticationToken) authentication;
                // 验证码匹配
                if(userDetails==null|| !checkService.checkVerifyCode(phone, VerifyCodeTypeEnum.LOGIN.getType(), verifyCode)){
                    throw ExceptionUtil.ofAuthenticationException(StatusEnum.VERIFY_CODE_NOT_EXIST);
                }
                // 匹配成功返回token
                authenticationToken.setAuthenticated(true);
                authenticationToken.setUser(userDetails);
                return authentication;

            }
        }catch (RedisConnectionFailureException e){
            // redis服务未启动返回异常
            throw ExceptionUtil.ofAuthenticationException(StatusEnum.UNEXPECT_ERROR,"Redis连接失败");
        }
        throw ExceptionUtil.ofAuthenticationException(StatusEnum.UNEXPECT_ERROR,"认证失败");

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PhonePasswordAuthenticationToken.class);
    }
}
