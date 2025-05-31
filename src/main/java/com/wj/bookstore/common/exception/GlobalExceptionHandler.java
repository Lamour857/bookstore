package com.wj.bookstore.common.exception;


import com.wj.bookstore.common.enums.StatusEnum;
import com.wj.bookstore.common.vo.ResVo;
import com.wj.bookstore.common.vo.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-14-22:23
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value= BookStoreRunTimeException.class)
    public ResVo<String> handleBookStoreException(BookStoreRunTimeException e){
        log.warn("BookStoreRuntimeException cause: {} \nmsg: {}",e.getCause(),e.getStatus().getMsg());
        return ResVo.fail(e.getStatus());
    }
    @ExceptionHandler(value=Exception.class)
    public ResVo<String> handleGlobalException(Exception e){
        log.warn("Exception occurred",e);
        //log.warn("Exception cause {}\nmsg: {}",e.getCause(),e.getMessage());
        return ResVo.fail(Status.newStatus(500,e.getMessage()));
    }
    @ExceptionHandler(value= RedisConnectionFailureException.class)
    public ResVo<String> handleRedisConnectionException(RedisConnectionFailureException e){
        log.warn("redis连接异常");
        return ResVo.fail(StatusEnum.UNEXPECT_ERROR,e.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResVo<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.warn("参数校验失败",e);
        return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }
}
