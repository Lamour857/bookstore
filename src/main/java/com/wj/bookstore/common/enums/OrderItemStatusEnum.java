package com.wj.bookstore.common.enums;

import com.wj.bookstore.common.utils.ExceptionUtil;
import lombok.Getter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-08-20:53
 **/
@Getter
public enum OrderItemStatusEnum {
    PENDING(0,"pending","待付款"),
    PAID(1,"paid","待发货"),
    DELIVERED(2,"delivered","已发货"),
    COMPLETED(3, "completed", "已收货"),
    CANCELED(4,"canceled","已取消"),
    REFUNDING(5,"refunding","退款中"),
    CLOSED(6,"closed","交易关闭");
    private final int code;
    private final String state;
    private final String desc;

    OrderItemStatusEnum(int code, String state, String desc) {
        this.code = code;
        this.state = state;
        this.desc = desc;
    }

    public static boolean checkState(String orderState) {
        for (OrderItemStatusEnum value : OrderItemStatusEnum.values()) {
            if (value.getState().equals(orderState)) {
                return true;
            }
        }
        return false;
    }

    public static String fromCode(String itemState) {
        for (OrderItemStatusEnum value : OrderItemStatusEnum.values()) {
            if (value.getState().equals(itemState)) {
                return value.getDesc();
            }
        }
        throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"订单条目状态错误");
    }
}
