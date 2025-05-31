package com.wj.bookstore.user.merchant.entity.DTO;

import com.wj.bookstore.common.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-09-20:33
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "店铺信息")
public class MerchantDTO extends BaseDTO {
    private Long userId;
    private String storeName;
    private String ownerName;
    private String status;
    private BigDecimal balance;

    private String province;
    private String city;
    private String county;
    private Short addressId;
    private String storeDescription;
    private String businessLicenseImageUrl;
    private String publicationBusinessLicenseImageUrl;
}
