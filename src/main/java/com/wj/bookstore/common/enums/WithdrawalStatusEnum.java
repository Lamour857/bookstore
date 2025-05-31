package com.wj.bookstore.common.enums;

import com.wj.bookstore.common.utils.ExceptionUtil;
import lombok.Getter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-12-19:44
 **/
@Getter
public enum WithdrawalStatusEnum {
    PENDING("pending","待处理"),
    AGREED("agreed","已同意"),
    REFUSED("refused","已拒绝"),
    COMPLETED("completed","已完成");

    private final String status;
    private final String desc;

    WithdrawalStatusEnum(String status, String desc) {
        this.status = status;
        this.desc = desc;
    }


    public static String fromCode(String status) {
        for (WithdrawalStatusEnum item : WithdrawalStatusEnum.values()) {
            if (item.status.equals(status)) {
                return item.desc;
            }
        }
        throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "提现状态: {}", status);
    }
}
