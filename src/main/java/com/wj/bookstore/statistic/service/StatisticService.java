package com.wj.bookstore.statistic.service;

import com.wj.bookstore.statistic.entity.DTO.MerchantStatisticDTO;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-31-16:13
 **/
public interface StatisticService {
    MerchantStatisticDTO getMerchantStatistics(Long merchantId);
}
