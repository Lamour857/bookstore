package com.wj.bookstore.user.merchant.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wj.bookstore.common.enums.BaseRoleEnum;
import com.wj.bookstore.common.enums.WithdrawalStatusEnum;
import com.wj.bookstore.common.enums.StatusEnum;
import com.wj.bookstore.common.enums.MerchantStatusEnum;
import com.wj.bookstore.common.utils.ExceptionUtil;
import com.wj.bookstore.common.vo.PageResult;
import com.wj.bookstore.core.oss.service.OssService;
import com.wj.bookstore.delivery.template.repository.dao.AreaDao;
import com.wj.bookstore.payment.alipay.config.AlipayConfig;
import com.wj.bookstore.payment.alipay.service.AlipayService;
import com.wj.bookstore.user.account.entity.DO.RoleDO;
import com.wj.bookstore.user.account.entity.DO.UserDO;
import com.wj.bookstore.user.authentication.token.AuthenticationToken;
import com.wj.bookstore.user.merchant.converter.MerchantConverter;
import com.wj.bookstore.user.merchant.entity.DO.WithdrawalReqDO;
import com.wj.bookstore.user.merchant.entity.DO.MerchantDO;
import com.wj.bookstore.user.merchant.entity.DTO.MerchantDTO;
import com.wj.bookstore.user.merchant.entity.DTO.WithdrawalRequestDTO;
import com.wj.bookstore.user.merchant.entity.req.QueryWithdrawalReq;
import com.wj.bookstore.user.merchant.entity.req.SaveMerchantReq;
import com.wj.bookstore.user.merchant.entity.req.WithdrawalReq;
import com.wj.bookstore.user.merchant.entity.req.QueryMerchantReq;
import com.wj.bookstore.user.merchant.repository.dao.WithdrawalDao;
import com.wj.bookstore.user.merchant.repository.dao.MerchantDao;
import com.wj.bookstore.user.merchant.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-09-16:14
 **/
@Service
@Slf4j
public class MerchantServiceImpl implements MerchantService {
    @Autowired
    private MerchantConverter merchantConverter;
    @Autowired
    private MerchantDao merchantDao;
    @Autowired
    private OssService ossService;
    @Autowired
    private WithdrawalDao withdrawalDao;
    @Autowired
    private AreaDao areaDao;
    @Autowired
    private AlipayService alipayService;

    @Override
    public Map<String, String> addStore(SaveMerchantReq saveMerchantReq) {
        AuthenticationToken authenticationToken= (AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDO user= authenticationToken.getUser();
        RoleDO roles=user.getRoles();

        if(roles.getId().equals(BaseRoleEnum.SELLER.getCode())) {
            throw ExceptionUtil.of(StatusEnum.RECORDS_EXISTS, "用户已成为商户: "+user.getUsername());
        }

        Map<String,String> urls=new HashMap<>();
        MerchantDO store=merchantDao.getByUserId(user.getId());
        if(store!=null){
            throw ExceptionUtil.of(StatusEnum.RECORDS_EXISTS,"已申请成为商家，店铺状态: "+MerchantStatusEnum.fromCode(store.getStatus()));
        }
        store= merchantDao.getByName(saveMerchantReq.getStoreName());
        if(store!=null){
            throw ExceptionUtil.of(StatusEnum.RECORDS_EXISTS,"店铺名称已被注册: "+saveMerchantReq.getStoreName());
        }

        store=new MerchantDO();
        store.setUserId(user.getId());
        store.setStoreName(saveMerchantReq.getStoreName());
        store.setAddressId(user.getLocationId());
        store.setOwnerName(saveMerchantReq.getOwnerName());
        store.setStatus(MerchantStatusEnum.PENDING.getCode());
        urls.put(saveMerchantReq.getBusinessLicenseImageName(),ossService.getSignature("store/businessLicenseImage", saveMerchantReq.getBusinessLicenseImageName()));
        urls.put(saveMerchantReq.getPublicationBusinessLicenseImageName(),ossService.getSignature("store/publicationBusinessLicenseImage", saveMerchantReq.getPublicationBusinessLicenseImageName()));
        merchantDao.save(store);
        return urls;
    }

    @Override
    public void saveImageUrl(List<String> notify) {
        AuthenticationToken authenticationToken= (AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDO user= authenticationToken.getUser();

        MerchantDO store= merchantDao.getByUserId(user.getId());
        if(store==null){
            throw ExceptionUtil.of(StatusEnum.RECORDS_NOT_EXISTS,"店铺不存在");
        }
        for (String urls : notify) {
            int index=urls.indexOf("?");
            if(index!=-1){
                String url=urls.substring(0,index);
                if(url.contains("businessLicenseImage")){
                    store.setBusinessLicenseImageUrl(url);
                }else if(url.contains("publicationBusinessLicenseImage")){
                    store.setPublicationBusinessLicenseImageUrl(url);
                }
            }
        }
        merchantDao.updateById(store);

    }

    @Override
    public void updateStoreState(Long storeId, String state) {
        MerchantDO merchantDO = merchantDao.getById(storeId);
        if(merchantDO ==null){
            throw ExceptionUtil.of(StatusEnum.RECORDS_NOT_EXISTS,"店铺不存在");
        }
        boolean isValid=false;
        for(MerchantStatusEnum merchantStatusEnum : MerchantStatusEnum.values()){
            if(merchantStatusEnum.getCode().equals(state)){
                isValid=true;
                merchantDO.setStatus(state);
                merchantDao.updateById(merchantDO);
            }
        }
        if(!isValid){
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"状态值: {}",state);
        }
    }

    @Override
    public MerchantDTO getById(Long merchantId) {
        MerchantDO merchantDO = merchantDao.getById(merchantId);
        if(merchantDO==null){
            throw ExceptionUtil.of(StatusEnum.RECORDS_NOT_EXISTS,"店铺不存在");
        }
        return merchantConverter.toDTO(merchantDO);
    }

    @Override
    public PageResult<MerchantDTO> queryMerchant(QueryMerchantReq req) {
        PageResult<MerchantDTO> pageResult=new PageResult<>();
        // 从数据库中获取数据
        Page<MerchantDO> page= merchantDao.queryByPage(req);
        pageResult.setPageNum(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        pageResult.setTotal(page.getTotal());
        List<MerchantDTO> merchantDTOList = new ArrayList<>();
        for (MerchantDO merchantDO : page.getRecords()) {
            merchantDTOList.add(merchantConverter.toDTO(merchantDO));
        }
        pageResult.setList(merchantDTOList);
        pageResult.setPages(page.getPages());
        return pageResult;
    }

    @Override
    public Map<String,String> updateStore(SaveMerchantReq req) {
        MerchantDO merchantDO = merchantDao.getById(req.getId());
        Map<String,String> signedUrlMap=new HashMap<>();
        if(merchantDO==null){
            throw ExceptionUtil.of(StatusEnum.RECORDS_NOT_EXISTS,"店铺不存在");
        }
        if(req.getStoreName()!=null&& !req.getStoreName().isEmpty()){
            merchantDO.setStoreName(req.getStoreName());
        }
        if(req.getOwnerName()!=null&& !req.getOwnerName().isEmpty()){
            merchantDO.setOwnerName(req.getOwnerName());
        }
        if(req.getStoreDescription()!=null&& !req.getStoreDescription().isEmpty()){
            merchantDO.setStoreDescription(req.getStoreDescription());
        }
        if(req.getAddressId()!=null&&areaDao.getById(req.getAddressId())!=null){
            merchantDO.setAddressId(req.getAddressId());
        }
        if(!req.getBusinessLicenseImageName().equals(merchantDO.getBusinessLicenseImageUrl())){
             String signedUrl=ossService.getSignature("store/businessLicenseImage",req.getBusinessLicenseImageName());
             signedUrlMap.put(req.getBusinessLicenseImageName(),signedUrl);
        }
        if(!req.getPublicationBusinessLicenseImageName().equals(merchantDO.getPublicationBusinessLicenseImageUrl())){
            String signedUrl=ossService.getSignature("store/publicationBusinessLicenseImage",req.getPublicationBusinessLicenseImageName());
            signedUrlMap.put(req.getPublicationBusinessLicenseImageName(),signedUrl);
        }
        merchantDao.updateById(merchantDO);
        return signedUrlMap;
    }

    @Override
    public void withdrawal(WithdrawalReq req) {
        MerchantDO merchantDO = merchantDao.getById(req.getMerchantId());
        if(merchantDO==null){
            throw ExceptionUtil.of(StatusEnum.RECORDS_NOT_EXISTS,"店铺不存在");
        }
        if (!merchantDO.getStatus().equals(MerchantStatusEnum.NORMAL.getCode())){
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"店铺状态: "+merchantDO.getStatus());
        }
        if(!(req.getAmount().compareTo(merchantDO.getBalance()) <=0)){
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"提现金额超出限额: "+req.getAmount());
        }
        WithdrawalReqDO withdrawalReqDO = withdrawalDao.getByMerchantId(merchantDO.getId());
        if(withdrawalReqDO!=null){
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"已经申请提现，申请状态: "+ WithdrawalStatusEnum.fromCode(withdrawalReqDO.getStatus()));
        }
        withdrawalReqDO =new WithdrawalReqDO();
        withdrawalReqDO.setId("WD"+UUID.randomUUID());
        withdrawalReqDO.setAccountIdentify(req.getAccountIdentify());
        withdrawalReqDO.setName(req.getName());
        withdrawalReqDO.setCertNumber(req.getCertNumber());
        withdrawalReqDO.setCertType(req.getCertType());
        withdrawalReqDO.setAccountIdentifyType(req.getAccountIdentifyType());
        withdrawalReqDO.setMerchantId(req.getMerchantId());
        withdrawalReqDO.setAmount(req.getAmount());
        withdrawalReqDO.setStatus(WithdrawalStatusEnum.PENDING.getStatus());
        withdrawalDao.save(withdrawalReqDO);
    }

    @Override
    public PageResult<WithdrawalRequestDTO> queryWithdrawalRequest(QueryWithdrawalReq req) {
        PageResult<WithdrawalRequestDTO> pageResult=new PageResult<>();
        Page<WithdrawalReqDO> page= withdrawalDao.queryByPage(req);
        pageResult.setPageNum(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        pageResult.setTotal(page.getTotal());
        List<WithdrawalRequestDTO> withdrawalRequestDTOS=new ArrayList<>();
        for (WithdrawalReqDO withdrawalReqDO : page.getRecords()) {
            WithdrawalRequestDTO withdrawalRequestDTO=merchantConverter.toDTO(withdrawalReqDO);
            withdrawalRequestDTOS.add(withdrawalRequestDTO);
        }
        pageResult.setList(withdrawalRequestDTOS);

        pageResult.setPages(page.getPages());
        return pageResult;
    }

    @Override
    public PageResult<WithdrawalRequestDTO> getWithdrawalList(Long merchantId, int pageNum, int pageSize) {
        PageResult<WithdrawalRequestDTO> pageResult=new PageResult<>();
        Page<WithdrawalReqDO> page= withdrawalDao.pageByMerchantId(merchantId, pageNum, pageSize);
        pageResult.setPageNum(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        pageResult.setTotal(page.getTotal());
        List<WithdrawalRequestDTO> withdrawalRequestDTOS=new ArrayList<>();
        for (WithdrawalReqDO withdrawalReqDO : page.getRecords()) {
            WithdrawalRequestDTO withdrawalRequestDTO=merchantConverter.toDTO(withdrawalReqDO);
            withdrawalRequestDTOS.add(withdrawalRequestDTO);
        }
        pageResult.setList(withdrawalRequestDTOS);

        pageResult.setPages(page.getPages());
        return pageResult;
    }

    @Override
    public void handleWithdrawal(String withdrawalId, Boolean isAgree) {
        AuthenticationToken authenticationToken = (AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDO user = authenticationToken.getUser();

        WithdrawalReqDO withdrawalReqDO = withdrawalDao.getById(withdrawalId);
        if(withdrawalReqDO ==null){
            throw ExceptionUtil.of(StatusEnum.RECORDS_NOT_EXISTS,"提现请求不存在");
        }

        MerchantDO merchantDO = merchantDao.getById(withdrawalReqDO.getMerchantId());

        if(merchantDO==null){
            throw ExceptionUtil.of(StatusEnum.RECORDS_NOT_EXISTS,"店铺不存在");
        }
        // 处理人信息
        withdrawalReqDO.setHandleTime(new Date(System.currentTimeMillis()));
        withdrawalReqDO.setHandleAdminId(user.getId());

        if(isAgree){

            // 更新商家销售额
            if(merchantDO.getBalance().compareTo(withdrawalReqDO.getAmount())<0){
                throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"提现金额超出限额: "+withdrawalReqDO.getAmount());
            }
            try{

                alipayService.transfer(withdrawalReqDO.getId(),withdrawalReqDO.getName(),withdrawalReqDO.getCertNumber(),withdrawalReqDO.getCertType(),
                        "提现",withdrawalReqDO.getAccountIdentify(),withdrawalReqDO.getAccountIdentifyType(),withdrawalReqDO.getAmount());
                merchantDO.setBalance(merchantDO.getBalance().subtract(withdrawalReqDO.getAmount()));
                merchantDao.updateById(merchantDO);
                withdrawalReqDO.setStatus(WithdrawalStatusEnum.COMPLETED.getStatus());
            }catch (Exception e){
                // 转账失败设置转账状态为拒绝
                withdrawalReqDO.setStatus(WithdrawalStatusEnum.REFUSED.getStatus());
                withdrawalReqDO.setRefuseReason(e.getMessage());
                withdrawalDao.updateById(withdrawalReqDO);
                throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,e.getMessage());
            }
        }else{
            withdrawalReqDO.setStatus(WithdrawalStatusEnum.REFUSED.getStatus());
        }
        withdrawalDao.updateById(withdrawalReqDO);

    }

    @Override
    public void updateWithdrawal(WithdrawalReq req) {
        WithdrawalReqDO withdrawalReqDO = withdrawalDao.getById(req.getId());

        if(withdrawalReqDO==null){
            throw ExceptionUtil.of(StatusEnum.RECORDS_NOT_EXISTS,"提现请求不存在");
        }
        MerchantDO merchantDO = merchantDao.getById(withdrawalReqDO.getMerchantId());
        if(merchantDO==null){
            throw ExceptionUtil.of(StatusEnum.RECORDS_NOT_EXISTS,"店铺不存在");
        }
        if(!withdrawalReqDO.getStatus().equals(WithdrawalStatusEnum.PENDING.getStatus())){
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"提现请求状态错误");
        }

        if(req.getAccountIdentify()!=null){
            withdrawalReqDO.setAccountIdentify(req.getAccountIdentify());
        }
        if(req.getAccountIdentifyType()!=null){
            withdrawalReqDO.setAccountIdentifyType(req.getAccountIdentifyType());
        }
        if(req.getCertNumber()!=null){
            withdrawalReqDO.setCertNumber(req.getCertNumber());
        }
        if(req.getCertType()!=null){
            withdrawalReqDO.setCertType(req.getCertType());
        }
        if(req.getName()!=null){
            withdrawalReqDO.setName(req.getName());
        }
        if(req.getAmount()!=null&&req.getAmount().compareTo(merchantDO.getBalance())<0){
            withdrawalReqDO.setAmount(req.getAmount());
        }
        withdrawalDao.updateById(withdrawalReqDO);

    }
}
