package com.wj.bookstore.statistic.service.impl;

import com.wj.bookstore.common.enums.StatusEnum;
import com.wj.bookstore.common.utils.ExceptionUtil;
import com.wj.bookstore.statistic.entity.DTO.MerchantStatisticDTO;
import com.wj.bookstore.statistic.repository.dao.StatisticDao;
import com.wj.bookstore.statistic.service.StatisticService;
import com.wj.bookstore.user.merchant.entity.DO.MerchantDO;
import com.wj.bookstore.user.merchant.repository.dao.MerchantDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-31-16:15
 **/
@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    private StatisticDao statisticDao;
    @Autowired
    private MerchantDao merchantDao;
    @Override
    public MerchantStatisticDTO getMerchantStatistics(Long merchantId) {
        MerchantDO merchantDO = merchantDao.getById(merchantId);
        if(merchantDO==null){
            throw ExceptionUtil.of(StatusEnum.RECORDS_NOT_EXISTS,"店铺不存在");
        }

        MerchantStatisticDTO statisticDTO = new MerchantStatisticDTO();
        Map<String,Object> map =statisticDao.getMerchantStatistics(merchantId);
        statisticDTO.setSoldBookCount((Integer) map.get("soldBookCount"));
        statisticDTO.setTransactionAmount((BigDecimal) map.get("transactionAmount"));
        statisticDTO.setOrderCompleteCount((Integer) map.get("orderCompleteCount"));
        return statisticDTO;
    }
}
