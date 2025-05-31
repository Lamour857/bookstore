package com.wj.bookstore.user.merchant.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wj.bookstore.user.merchant.entity.DO.MerchantDO;
import com.wj.bookstore.user.merchant.entity.DO.WithdrawalReqDO;
import com.wj.bookstore.user.merchant.entity.req.QueryMerchantReq;
import com.wj.bookstore.user.merchant.entity.req.QueryWithdrawalReq;
import com.wj.bookstore.user.merchant.repository.mapper.MerchantMapper;
import org.springframework.stereotype.Repository;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-09-17:58
 **/
@Repository
public class MerchantDao extends ServiceImpl<MerchantMapper, MerchantDO> {
    public MerchantDO getByName(String storeName) {
        LambdaQueryWrapper<MerchantDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MerchantDO::getStoreName, storeName);
        return getBaseMapper().selectOne(queryWrapper);
    }

    public MerchantDO getByUserId(Long userId) {
        LambdaQueryWrapper<MerchantDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MerchantDO::getUserId, userId);
        return getBaseMapper().selectOne(queryWrapper);
    }

    public Page<MerchantDO> listByPage(int pageNum, int pageSize) {
        Page<MerchantDO> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<MerchantDO> queryCategory = Wrappers.lambdaQuery();
        return this.page(page, queryCategory);
    }


    public Page<MerchantDO> queryByPage(QueryMerchantReq req) {
        Page<MerchantDO> page = new Page<>(req.getPageNum(), req.getPageSize());
        LambdaQueryWrapper<MerchantDO> queryMerchant= Wrappers.lambdaQuery();
        if(req.getId()!=null){
            queryMerchant.eq(MerchantDO::getId, req.getId());
        }
        if(req.getStoreName()!=null){
            queryMerchant.like(MerchantDO::getStoreName, req.getStoreName());
        }
        if(req.getEarliestCreateTime()!=null){
            queryMerchant.ge(MerchantDO::getCreateTime, req.getEarliestCreateTime());
        }
        if(req.getLatestCreateTime()!=null){
            queryMerchant.le(MerchantDO::getCreateTime, req.getLatestCreateTime());
        }
        if(req.getStatus()!=null){
            queryMerchant.eq(MerchantDO::getStatus, req.getStatus());
        }
        if(req.getLocationId()!=null){
            queryMerchant.eq(MerchantDO::getAddressId, req.getLocationId());
        }
        return this.page(page, queryMerchant);
    }


}
