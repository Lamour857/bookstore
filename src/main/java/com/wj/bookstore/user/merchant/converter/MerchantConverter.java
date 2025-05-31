package com.wj.bookstore.user.merchant.converter;

import com.wj.bookstore.common.enums.MerchantStatusEnum;
import com.wj.bookstore.common.enums.WithdrawalStatusEnum;
import com.wj.bookstore.common.utils.DateUtil;
import com.wj.bookstore.delivery.template.entity.DO.AreaDO;
import com.wj.bookstore.delivery.template.repository.dao.AreaDao;
import com.wj.bookstore.user.merchant.entity.DO.MerchantDO;
import com.wj.bookstore.user.merchant.entity.DO.WithdrawalReqDO;
import com.wj.bookstore.user.merchant.entity.DTO.MerchantDTO;
import com.wj.bookstore.user.merchant.entity.DTO.WithdrawalRequestDTO;
import com.wj.bookstore.user.merchant.repository.dao.MerchantDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-09-20:42
 **/
@Component
public class MerchantConverter {
    @Autowired
    private AreaDao areaDao;
    @Autowired
    private MerchantDao merchantDao;
    public MerchantDTO toDTO(MerchantDO merchantDO) {
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setId(merchantDO.getId());
        merchantDTO.setUserId(merchantDO.getUserId());
        merchantDTO.setStoreName(merchantDO.getStoreName());
        merchantDTO.setOwnerName(merchantDO.getOwnerName());
        merchantDTO.setStatus(MerchantStatusEnum.fromCode(merchantDO.getStatus()));
        merchantDTO.setBalance(merchantDO.getBalance());
        AreaDO address= areaDao.getById(merchantDO.getAddressId());
        Short level=address.getLevel();
        switch (level){
            case 1:
                // 省级
                merchantDTO.setProvince(address.getName());
                break;
            case 2:
                // 市级
                AreaDO city = areaDao.getById(address.getParent());
                merchantDTO.setProvince(address.getName());
                merchantDTO.setCity(city.getName());
                break;
            case 3:
                // 区级
                AreaDO parent = areaDao.getById(address.getParent());
                AreaDO province= areaDao.getById(parent.getParent());
                merchantDTO.setCity(parent.getName());
                merchantDTO.setProvince(province.getName());
                merchantDTO.setCounty(address.getName());
                break;
        }
        merchantDTO.setAddressId(merchantDO.getAddressId());
        merchantDTO.setStoreDescription(merchantDO.getStoreDescription());
        merchantDTO.setBusinessLicenseImageUrl(merchantDO.getBusinessLicenseImageUrl());
        merchantDTO.setPublicationBusinessLicenseImageUrl(merchantDO.getPublicationBusinessLicenseImageUrl());
        merchantDTO.setCreateTime(DateUtil.convert(merchantDO.getCreateTime()));
        merchantDTO.setUpdateTime(DateUtil.convert(merchantDO.getUpdateTime()));
        return merchantDTO;
    }
    public WithdrawalRequestDTO toDTO(WithdrawalReqDO withdrawalReqDO) {
        WithdrawalRequestDTO withdrawalRequestDTO = new WithdrawalRequestDTO();
        withdrawalRequestDTO.setId(withdrawalReqDO.getId());
        withdrawalRequestDTO.setMerchantId(withdrawalReqDO.getMerchantId());
        MerchantDO merchantDO = merchantDao.getById(withdrawalReqDO.getMerchantId());
        withdrawalRequestDTO.setMerchantName(merchantDO.getStoreName());
        withdrawalRequestDTO.setName(withdrawalReqDO.getName());
        withdrawalRequestDTO.setRefuseReason(withdrawalReqDO.getRefuseReason());
        withdrawalRequestDTO.setCertType(withdrawalReqDO.getCertType());
        withdrawalRequestDTO.setCertNumber(withdrawalReqDO.getCertNumber());
        withdrawalRequestDTO.setOwnerName(merchantDO.getOwnerName());
        withdrawalRequestDTO.setHandleAdminId(withdrawalReqDO.getHandleAdminId());
        withdrawalRequestDTO.setHandleTime(DateUtil.convert(withdrawalReqDO.getHandleTime()));
        withdrawalRequestDTO.setStatus(WithdrawalStatusEnum.fromCode(withdrawalReqDO.getStatus()));
        withdrawalRequestDTO.setAmount(withdrawalReqDO.getAmount());
        withdrawalRequestDTO.setCreateTime(DateUtil.convert(withdrawalReqDO.getCreateTime()));
        withdrawalRequestDTO.setUpdateTime(DateUtil.convert(withdrawalReqDO.getUpdateTime()));
        withdrawalRequestDTO.setAccountIdentify(withdrawalReqDO.getAccountIdentify());
        withdrawalRequestDTO.setAccountIdentifyType(withdrawalReqDO.getAccountIdentifyType());
        return withdrawalRequestDTO;
    }
}
