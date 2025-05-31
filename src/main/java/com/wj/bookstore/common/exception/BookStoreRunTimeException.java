package com.wj.bookstore.common.exception;


import com.wj.bookstore.common.enums.StatusEnum;
import com.wj.bookstore.common.vo.Status;
import lombok.Getter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-14-11:41
 **/
@Getter
public class BookStoreRunTimeException extends RuntimeException{
    private Status status;

    public BookStoreRunTimeException(Status status) {this.status = status;}

    public BookStoreRunTimeException(int code, String msg) {this.status = Status.newStatus(code,msg);}

    public BookStoreRunTimeException(StatusEnum statusEnum, Object...args) {
        this.status = Status.newStatus(statusEnum,args);
    }
}