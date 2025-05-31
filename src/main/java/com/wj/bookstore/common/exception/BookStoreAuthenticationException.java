package com.wj.bookstore.common.exception;


import com.wj.bookstore.common.enums.StatusEnum;
import com.wj.bookstore.common.vo.Status;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-20-10:03
 **/
@Getter
public class BookStoreAuthenticationException extends AuthenticationException {
    private final Status status;
    public BookStoreAuthenticationException(StatusEnum statusEnum, Object...args) {
        super(statusEnum.getMsg());
        this.status = Status.newStatus(statusEnum,args);
    }
}
