package com.wj.bookstore.user.merchant.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-27-15:31
 **/
@Data
@ApiModel(value = "MerchantReq",description = "商家查询请求对象")
public class QueryMerchantReq {
    @ApiModelProperty(value = "商家id")
    private Long id;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;


    @ApiModelProperty(value = "最早创建时间")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant earliestCreateTime;

    @ApiModelProperty(value="最晚创建时间")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant latestCreateTime;

    @ApiModelProperty(value = "是否启用")
    private String status;

    @ApiModelProperty(value = "地址id")
    private Short locationId;

    @ApiModelProperty(value = "页码")
    private Integer pageNum;

    @ApiModelProperty(value = "页大小")
    private Integer pageSize;
}
