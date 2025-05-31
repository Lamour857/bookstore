package com.wj.bookstore.user.account.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-27-16:36
 **/
@Data
@ApiModel("角色查询请求对象")
public class QueryRoleReq {
    @ApiModelProperty(value = "角色id")
    private Long id;
    @ApiModelProperty(value = "角色名称")
    private String name;
    @ApiModelProperty(value = "角色描述")
    private String description;
    @ApiModelProperty(value = "最早创建时间")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant earliestCreateTime;

    @ApiModelProperty(value="最晚创建时间")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant latestCreateTime;

    @ApiModelProperty(value = "页码")
    private Integer pageNum;

    @ApiModelProperty(value = "页大小")
    private Integer pageSize;
}
