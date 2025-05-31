package com.wj.bookstore.user.merchant.entity.req;

import com.wj.bookstore.common.validation.CreateMerchantReqGroup;
import com.wj.bookstore.common.validation.UpdateMerchantReqGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-09-17:52
 **/
@Data
@ApiModel(description = "添加店铺请求参数")
public class SaveMerchantReq {

    @ApiModelProperty(value = "店铺id")
    @NotNull(message = "店铺id不能为空", groups = {UpdateMerchantReqGroup.class})
    private Long id;

    @ApiModelProperty(value = "店铺名称")
    @NotNull(message = "店铺名称不能为空", groups = {CreateMerchantReqGroup.class})
    private String storeName;

    @ApiModelProperty(value = "店铺描述")
    @NotNull(message = "店铺描述不能为空", groups = {CreateMerchantReqGroup.class})
    private String storeDescription;

    @ApiModelProperty(value = "店铺地址id")
    private Short addressId;

    @ApiModelProperty(value = "店主名称")
    @NotNull(message = "店主名称不能为空", groups = {CreateMerchantReqGroup.class})
    private String ownerName;

    @ApiModelProperty(value = "营业执照文件名")
    @NotNull(message = "营业执照文件名不能为空", groups = {CreateMerchantReqGroup.class})
    private String businessLicenseImageName;

    @ApiModelProperty(value = "店铺头像文件名")
    @NotNull(message = "店铺头像文件名不能为空", groups = {CreateMerchantReqGroup.class})
    private String publicationBusinessLicenseImageName;
}
