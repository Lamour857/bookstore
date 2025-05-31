package com.wj.bookstore.user.authentication.entrypoint;


import com.wj.bookstore.common.exception.BookStoreAuthenticationException;
import com.wj.bookstore.common.utils.JsonUtil;
import com.wj.bookstore.common.vo.ResVo;
import com.wj.bookstore.common.vo.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-19-21:31
 **/
@Component
@Slf4j
public class CustomerAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("认证失败：{}", authException.getMessage());
        if(authException instanceof BookStoreAuthenticationException){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonUtil.writeResVoToResponse(response, ResVo.fail(((BookStoreAuthenticationException) authException).getStatus()));
            return;
        }
        JsonUtil.writeResVoToResponse(response, ResVo.fail(Status.newStatus(500,"认证失败")));
    }
}
