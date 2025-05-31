package com.wj.bookstore.statistic.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wj.bookstore.common.enums.OrderItemStatusEnum;
import com.wj.bookstore.common.enums.OrderStatusEnum;
import com.wj.bookstore.shopping.order.entity.DO.OrderItemDO;
import com.wj.bookstore.shopping.order.repository.mapper.OrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-31-16:15
 **/
@Repository
public class StatisticDao{
    @Autowired
    private OrderItemMapper orderItemMapper;
    public Map<String,Object> getMerchantStatistics(Long merchantId) {
        Map<String,Object> result=new HashMap<>();
        LambdaQueryWrapper<OrderItemDO> queryOrder = new LambdaQueryWrapper<>();
        queryOrder.eq(OrderItemDO::getMerchantId, merchantId);
        queryOrder.eq(OrderItemDO::getItemState, OrderItemStatusEnum.CLOSED.getState());
        List<OrderItemDO> orderItemDOList = orderItemMapper.selectList(queryOrder);
        int soldBookCount=0;
        int orderCompleteCount=0;
        BigDecimal transactionAmount=new BigDecimal(0);

        for(OrderItemDO orderItemDO:orderItemDOList){
            orderCompleteCount++;
            soldBookCount+=orderItemDO.getQuantity();
            transactionAmount=transactionAmount.add(orderItemDO.getSellingPrice().multiply(new BigDecimal(orderItemDO.getQuantity())));
        }
        result.put("soldBookCount",soldBookCount);
        result.put("orderCompleteCount",orderCompleteCount);
        result.put("transactionAmount",transactionAmount);
        return result;
    }
}
