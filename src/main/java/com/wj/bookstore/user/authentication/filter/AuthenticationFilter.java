package com.wj.bookstore.user.authentication.filter;


import com.wj.bookstore.common.enums.StatusEnum;
import com.wj.bookstore.common.utils.ExceptionUtil;
import com.wj.bookstore.common.utils.JsonUtil;
import com.wj.bookstore.common.vo.req.UserReq;
import com.wj.bookstore.monitoring.RequestContextHolder;
import com.wj.bookstore.user.authentication.token.PhonePasswordAuthenticationToken;
import com.wj.bookstore.user.authentication.token.VerifyCodeAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wujia
 * @description: 对来自/auth/**认证请求进行过滤
 * @createTime: 2024-12-20-13:50
 **/
@Slf4j
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String PASSWORD_REG = "^[A-Za-z0-9]{6,12}$";
    private static final String VERIFY_CODE_REG = "^\\d{6}$";
    private static final String PHONE_REG = "^1[3-9]\\d{9}$";

    public AuthenticationFilter() {
        super("/auth/**");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        UserReq userReq = JsonUtil.toObj(RequestContextHolder.getContext().getPayload(), UserReq.class);
        String requestURI=request.getRequestURI();
        String phone = userReq.getPhone();
        if(StringUtils.isNotBlank(phone)&& !phone.matches(PHONE_REG)){
            throw ExceptionUtil.ofAuthenticationException(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"手机号格式错误");
        }

        String verifyCode=userReq.getVerifyCode();
        if(StringUtils.isNotBlank(verifyCode)&& !verifyCode.matches(VERIFY_CODE_REG)){
            throw ExceptionUtil.ofAuthenticationException(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"验证码格式错误");
        }

        String password=userReq.getPassword();

        if(StringUtils.isNotBlank(password)&& !password.matches(PASSWORD_REG)){
            throw ExceptionUtil.ofAuthenticationException(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"密码仅字母+数字，且长度为6-12位");
        }

        AbstractAuthenticationToken authRequest;
        phone = phone.trim();
        if(requestURI.equals("/auth/password")){
            authRequest= new PhonePasswordAuthenticationToken(phone, password);
        }else if(requestURI.equals("/auth/verifyCode")){
            authRequest= new VerifyCodeAuthenticationToken(phone, verifyCode);
        }else{
            throw ExceptionUtil.ofAuthenticationException(StatusEnum.UNSUPPORTED_AUTHENTICATION_REQUEST);
        }
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
    protected String obtainPhoneNumber(HttpServletRequest request) {
        return request.getParameter("phone")==null?"":request.getParameter("phone");
    }

    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter("password")==null?"":request.getParameter("password");
    }
    protected String obtainVerifyCode(HttpServletRequest request) {
        return request.getParameter("verifyCode")==null?"":request.getParameter("verifyCode");
    }
    protected void setDetails(HttpServletRequest request, AbstractAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}
