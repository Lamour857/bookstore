package com.wj.bookstore.user.merchant.service;

import com.wj.bookstore.common.vo.PageResult;
import com.wj.bookstore.user.merchant.entity.DTO.MerchantDTO;
import com.wj.bookstore.user.merchant.entity.DTO.WithdrawalRequestDTO;
import com.wj.bookstore.user.merchant.entity.req.QueryWithdrawalReq;
import com.wj.bookstore.user.merchant.entity.req.SaveMerchantReq;
import com.wj.bookstore.user.merchant.entity.req.WithdrawalReq;
import com.wj.bookstore.user.merchant.entity.req.QueryMerchantReq;
import org.json.JSONException;

import java.util.List;
import java.util.Map;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-09-16:14
 **/
public interface MerchantService {
    Map<String, String> addStore(SaveMerchantReq saveMerchantReq);

    void saveImageUrl(List<String> notify);


    void updateStoreState(Long storeId, String state);


    MerchantDTO getById(Long merchantId);

    PageResult<MerchantDTO> queryMerchant(QueryMerchantReq req);

    Map<String, String> updateStore(SaveMerchantReq req);

    void withdrawal(WithdrawalReq req);

    PageResult<WithdrawalRequestDTO> queryWithdrawalRequest(QueryWithdrawalReq req);

    PageResult<WithdrawalRequestDTO> getWithdrawalList(Long merchantId,int pageNum,int pageSize);

    void handleWithdrawal(String withdrawalId, Boolean isAgree);

    void updateWithdrawal(WithdrawalReq req);
}
