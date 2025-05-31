package com.wj.bookstore.common.enums;

import com.wj.bookstore.common.utils.ExceptionUtil;
import lombok.Getter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-07-12:37
 **/
@Getter
public enum OrderStatusEnum {
    PENDING(0,"pending","待付款"),
    PAID(1,"paid","已付款"),
    DEALING(2,"dealing","交易中"),
    CLOSED(3, "closed", "交易关闭");
    private final int code;
    private final String state;
    private final String desc;

    OrderStatusEnum(int code, String state, String desc) {
        this.code = code;
        this.state = state;
        this.desc = desc;
    }

    public static boolean checkState(String orderState) {
        for (OrderStatusEnum value : OrderStatusEnum.values()) {
            if (value.getState().equals(orderState)) {
                return true;
            }
        }
        return false;
    }

    public static String fromCode(String itemState) {
        for (OrderStatusEnum value : OrderStatusEnum.values()) {
            if (value.getState().equals(itemState)) {
                return value.getDesc();
            }
        }
        throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"订单状态错误");
    }
}
