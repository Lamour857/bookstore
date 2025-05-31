package com.wj.bookstore.user.merchant.entity.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wj.bookstore.common.dto.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-09-16:16
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("merchant")
public class MerchantDO extends BaseDO {
    private Long userId;
    private String storeName;
    private String ownerName;
    private BigDecimal balance;
    private String status;
    private Short addressId;
    private String storeDescription;
    private String businessLicenseImageUrl;
    private String publicationBusinessLicenseImageUrl;
}

