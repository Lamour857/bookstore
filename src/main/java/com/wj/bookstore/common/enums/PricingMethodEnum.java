package com.wj.bookstore.common.enums;

import com.wj.bookstore.common.utils.ExceptionUtil;
import lombok.Getter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-09-13:13
 **/
@Getter
public enum PricingMethodEnum {
    WEIGHT_PRICE((short)1,"按重量"),
    PIECE_PRICE((short)2,"按件数"),
    FIXED_PRICE((short)0,"固定费用");

    private final short type;
    private final String description;

    PricingMethodEnum(short type, String description) {
        this.type = type;
        this.description = description;
    }

    public static String fromCode(Short pricingMethod) {
        for (PricingMethodEnum value : PricingMethodEnum.values()) {
            if (value.type == pricingMethod) {
                return value.description;
            }
        }
        throw ExceptionUtil.of(StatusEnum.RECORDS_NOT_EXISTS,"运费计费方式: "+pricingMethod);
    }
}
