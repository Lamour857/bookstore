package com.wj.bookstore.common.utils;


import com.wj.bookstore.common.enums.StatusEnum;
import com.wj.bookstore.common.exception.BookStoreAuthenticationException;
import com.wj.bookstore.common.exception.BookStoreRunTimeException;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-14-11:40
 **/
public class ExceptionUtil {
    public static BookStoreRunTimeException of(StatusEnum status, Object...args){
        return new BookStoreRunTimeException(status,args);
    }
    public static BookStoreAuthenticationException ofAuthenticationException(StatusEnum status, Object...args){
        return new BookStoreAuthenticationException(status,args);
    }
}
