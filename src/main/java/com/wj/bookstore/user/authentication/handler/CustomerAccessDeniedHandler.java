package com.wj.bookstore.user.authentication.handler;


import com.wj.bookstore.common.enums.StatusEnum;
import com.wj.bookstore.common.utils.JsonUtil;
import com.wj.bookstore.common.vo.ResVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-19-21:33
 **/
@Slf4j
@Component
public class CustomerAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("权限不足: ", accessDeniedException);
        log.info(Arrays.toString(accessDeniedException.getStackTrace()));
        log.info(accessDeniedException.toString());
        log.info(String.valueOf(accessDeniedException.getCause()));
        JsonUtil.writeResVoToResponse(response, ResVo.fail(StatusEnum.FORBID_ERROR));
    }
}
