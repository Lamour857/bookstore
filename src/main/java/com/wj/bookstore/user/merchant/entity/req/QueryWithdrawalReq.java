package com.wj.bookstore.user.merchant.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-06-15:49
 **/
@Data
@ApiModel(value = "QueryWithdrawalReq",description = "取款查询请求对象")
public class QueryWithdrawalReq {
    @ApiModelProperty(value = "商家id")
    private Long merchantId;


    @ApiModelProperty(value = "最早创建时间")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant earliestCreateTime;

    @ApiModelProperty(value="最晚创建时间")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant latestCreateTime;

    @ApiModelProperty(value = "最小金额")
    private BigDecimal minAmount;

    @ApiModelProperty(value = "最大金额")
    private BigDecimal maxAmount;

    @ApiModelProperty(value = "状态")
    private String withdrawalState;

    @ApiModelProperty(value = "页码")
    private Integer pageNum;

    @ApiModelProperty(value = "页大小")
    private Integer pageSize;
}
