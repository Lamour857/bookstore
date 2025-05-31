package com.wj.bookstore.statistic.entity.DTO;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-31-16:25
 **/
@Data
public class MerchantStatisticDTO {
    private int orderCompleteCount;
    private int soldBookCount;
    private BigDecimal transactionAmount;
}
