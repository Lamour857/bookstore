package com.wj.bookstore.common.enums;

import com.wj.bookstore.common.utils.ExceptionUtil;
import lombok.Getter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-18-21:00
 **/
@Getter
public enum RefundStatusEnum {
    PROCESSING("待处理","processing"),
    REFUNDING("退款中","refunding"),
    SUCCESS("退款成功","success"),
    AGREED("已同意","agreed"),
    DELIVERED("已发货","delivered"),
    REFUSED("拒绝退款","refused"),
    APPEALING("申诉中","appealing"),
    REJECTED("已拒绝","rejected");

    private final String description;
    private final String code;

    RefundStatusEnum(String description,String code) {
        this.code = code;
        this.description = description;
    }


    public static String fromCode(String refundStatus) {
        for (RefundStatusEnum value : RefundStatusEnum.values()) {
            if (value.getCode().equals(refundStatus)) {
                return value.getDescription();
            }
        }
        throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"退款状态错误: "+ refundStatus);
    }
}
