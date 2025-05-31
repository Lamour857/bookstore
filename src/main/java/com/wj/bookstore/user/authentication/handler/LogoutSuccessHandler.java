package com.wj.bookstore.user.authentication.handler;


import com.wj.bookstore.common.utils.JsonUtil;
import com.wj.bookstore.common.vo.ResVo;
import com.wj.bookstore.user.authentication.cache.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-19-21:37
 **/
@Component
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {
    @Autowired
    private TokenCache tokenCache;


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 清除用户的redis缓存
        String token= request.getHeader("Authorization");
        if(tokenCache.getUserByToken(token)!=null){
            // 可以在这里设置单点登录
            tokenCache.deleteToken(token);
        }
        //
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            System.out.println("有数据");
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        JsonUtil.writeResVoToResponse(response, ResVo.ok());
    }
}
