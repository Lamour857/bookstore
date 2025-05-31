package com.wj.bookstore.common.enums;

import com.wj.bookstore.common.utils.ExceptionUtil;
import lombok.Getter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-09-10:30
 **/
@Getter
public enum RefundTypeEnum {
    REFUND_TYPE_ENUM("仅退款","refund_type_enum"),
    RETURN_TYPE_ENUM("退货","return_type_enum");

    private final String description;
    private final String code;

    RefundTypeEnum(String description,String code) {
        this.code = code;
        this.description = description;
    }


    public static String fromCode(String refundStatus) {
        for (RefundTypeEnum value : RefundTypeEnum.values()) {
            if (value.getCode().equals(refundStatus)) {
                return value.getDescription();
            }
        }
        throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"退款类型错误: "+ refundStatus);
    }
}
