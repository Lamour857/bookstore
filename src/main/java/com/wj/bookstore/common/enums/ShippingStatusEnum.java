package com.wj.bookstore.common.enums;

import com.wj.bookstore.common.utils.ExceptionUtil;
import lombok.Getter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-12-14:08
 **/
@Getter
public enum ShippingStatusEnum {
    UNSHIPPED("未发货","unshipped"),
    SHIPPED("已发货","shipped"),
    DELIVERED("已送达","delivered"),
    CANCELED("已取消","canceled");

    private final String description;
    private final String code;
    ShippingStatusEnum(String description, String code) {
        this.description = description;
        this.code = code;
    }

    public static String getByCode(String shippingStatus) {
        for (ShippingStatusEnum shippingStatusEnum : ShippingStatusEnum.values()) {
            if (shippingStatusEnum.getCode().equals(shippingStatus)) {
                return shippingStatusEnum.getDescription();
            }
        }
        return null;
    }
}
