package com.wj.bookstore.user.merchant.repository.dao;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wj.bookstore.common.enums.WithdrawalStatusEnum;
import com.wj.bookstore.user.merchant.entity.DO.WithdrawalReqDO;
import com.wj.bookstore.user.merchant.entity.req.QueryWithdrawalReq;
import com.wj.bookstore.user.merchant.repository.mapper.WithdrawalMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-12-19:41
 **/
@Repository
public class WithdrawalDao extends ServiceImpl<WithdrawalMapper, WithdrawalReqDO> {
    public WithdrawalReqDO getByMerchantId(Long id) {
        LambdaQueryWrapper<WithdrawalReqDO> queryWrapper = Wrappers.lambdaQuery();
        if(id!=null){
            queryWrapper.eq(WithdrawalReqDO::getMerchantId, id);
        }
        queryWrapper.eq(WithdrawalReqDO::getStatus, WithdrawalStatusEnum.PENDING.getStatus());
        return this.baseMapper.selectOne(queryWrapper);
    }
    public Page<WithdrawalReqDO> queryByPage(QueryWithdrawalReq req) {
        Page<WithdrawalReqDO> page = new Page<>(req.getPageNum(), req.getPageSize());
        LambdaQueryWrapper<WithdrawalReqDO> queryWrapper= Wrappers.lambdaQuery();
        if(req.getMerchantId()!=null){
            queryWrapper.eq(WithdrawalReqDO::getMerchantId, req.getMerchantId());
        }
        if(req.getEarliestCreateTime()!=null){
            queryWrapper.ge(WithdrawalReqDO::getCreateTime, req.getEarliestCreateTime());
        }
        if(req.getLatestCreateTime()!=null){
            queryWrapper.le(WithdrawalReqDO::getCreateTime, req.getLatestCreateTime());
        }
        if(req.getMinAmount()!=null){
            queryWrapper.ge(WithdrawalReqDO::getAmount, req.getMinAmount());
        }
        if(req.getMaxAmount()!=null){
            queryWrapper.le(WithdrawalReqDO::getAmount, req.getMaxAmount());
        }
        if(req.getWithdrawalState()!=null){
            queryWrapper.eq(WithdrawalReqDO::getStatus, req.getWithdrawalState());
        }
        return this.page(page, queryWrapper);
    }

    public List<WithdrawalReqDO> listByMerchantId(Long merchantId) {
        LambdaQueryWrapper<WithdrawalReqDO> queryWrapper = Wrappers.lambdaQuery();
        if(merchantId!=null){
            queryWrapper.eq(WithdrawalReqDO::getMerchantId, merchantId);
        }
        return this.baseMapper.selectList(queryWrapper);
    }

    public Page<WithdrawalReqDO> pageByMerchantId(Long merchantId, int pageNum, int pageSize) {
        Page<WithdrawalReqDO> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<WithdrawalReqDO> queryWrapper = Wrappers.lambdaQuery();
        if(merchantId!=null){
            queryWrapper.eq(WithdrawalReqDO::getMerchantId, merchantId);
        }
        return this.page(page, queryWrapper);
    }
}
