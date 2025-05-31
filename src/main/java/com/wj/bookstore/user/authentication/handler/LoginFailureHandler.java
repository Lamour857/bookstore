package com.wj.bookstore.user.authentication.handler;



import com.wj.bookstore.common.exception.BookStoreAuthenticationException;
import com.wj.bookstore.common.utils.JsonUtil;
import com.wj.bookstore.common.vo.ResVo;
import com.wj.bookstore.common.vo.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-19-21:29
 **/
@Component
@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof BookStoreAuthenticationException){
            BookStoreAuthenticationException e= (BookStoreAuthenticationException) exception;
            log.info("认证失败：{}", e.getStatus().getMsg());
            JsonUtil.writeResVoToResponse(response,ResVo.fail(e.getStatus()) );
        }

        JsonUtil.writeResVoToResponse(response, ResVo.fail(Status.newStatus(500,exception.toString())));
    }
}
