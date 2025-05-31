package com.wj.bookstore.common.enums;

import com.wj.bookstore.common.utils.ExceptionUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-09-19:54
 **/
@Getter
@AllArgsConstructor
public enum MerchantStatusEnum {
    PENDING("待审核","pending"),
    NORMAL("正常","normal"),
    FREEZE("冻结","freeze");

    private final String description;
    private final String code;


    public static String fromCode(String code) {
        for (MerchantStatusEnum statusEnum : MerchantStatusEnum.values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum.getDescription();
            }
        }
        throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "不支持的店铺状态");
    }
}
