package com.wj.bookstore.common.enums;

import com.wj.bookstore.common.utils.ExceptionUtil;
import lombok.Getter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-25-16:20
 **/
@Getter
public enum BookStatusEnum {
    UNDER_REVIEW("under_review","待审核"),
    SOLD_OUT("sold_out","已告罄"),
    PUBLISHED("published","已上架"),
    REMOVED("removed","已下架");

    private final String status;
    private final String desc;

    BookStatusEnum(String status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static String fromStatus(String bookStatus) {
        for (BookStatusEnum value : BookStatusEnum.values()) {
            if (value.getStatus().equals(bookStatus)) {
                return value.getDesc();
            }
        }
        throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"书籍状态: {}",bookStatus);
    }

    public static String fromDesc(String bookStatus) {
        for(BookStatusEnum value : BookStatusEnum.values()){
            if(value.getDesc().equals(bookStatus)){
                return value.getStatus();
            }
        }
        throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"书籍状态: {}",bookStatus);
    }
}
