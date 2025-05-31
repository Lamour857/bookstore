package com.wj.bookstore.user.merchant.entity.req;

import com.wj.bookstore.common.validation.CreateWithdrawalReqGroup;
import com.wj.bookstore.common.validation.UpdateWithdrawalReqGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;


import java.math.BigDecimal;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-12-19:26
 **/
@Data
@ApiModel("提现请求")
public class WithdrawalReq {
    @ApiModelProperty(value = "提现请求id")
    @NotNull(message = "提现请求id不能为空", groups = {UpdateWithdrawalReqGroup.class})
    private String id;

    @ApiModelProperty(value = "姓名")
    @NotNull(message = "姓名不能为空", groups = {CreateWithdrawalReqGroup.class})
    private String name;

    @ApiModelProperty(value = "提现金额")
    @NotNull(message = "提现金额不能为空", groups = {CreateWithdrawalReqGroup.class})
    private BigDecimal amount;

    @ApiModelProperty(value = "证件号")
    private String certNumber;

    @ApiModelProperty(value = "证件类型")
    private String certType;

    @ApiModelProperty(value = "商家id")
    @NotNull(message = "商家id不能为空", groups = {CreateWithdrawalReqGroup.class})
    private Long merchantId;

    @ApiModelProperty(value = "账号")
    @NotNull(message = "账号不能为空", groups = {CreateWithdrawalReqGroup.class})
    private String accountIdentify;

    @ApiModelProperty(value = "账号类型")
    @NotNull(message = "账号类型不能为空", groups = {CreateWithdrawalReqGroup.class})
    private String accountIdentifyType;
}
