package com.wj.bookstore.common.enums;

import lombok.Getter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-09-12:05
 **/
@Getter
public enum PermissionTypeEnum {
    VIEW(1,"页面权限"),
    API(2,"API权限");
    private final Integer type;
    private final String description;
    PermissionTypeEnum(Integer type, String description) {
        this.type = type;
        this.description = description;
    }
}
