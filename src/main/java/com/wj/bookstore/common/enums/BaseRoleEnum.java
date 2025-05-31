package com.wj.bookstore.common.enums;

import lombok.Getter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-28-19:24
 **/
@Getter
public enum BaseRoleEnum {
    ADMIN(1,"管理员"),
    SELLER(2,"商家"),
    CUSTOMER(3,"普通用户");

    private final long code;
    private final String desc;

    BaseRoleEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
