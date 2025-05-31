package com.wj.bookstore.user.account.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-27-10:50
 **/
@Data
@ApiModel(value = "QueryUserReq",description = "用户查询请求对象")
@ToString
public class QueryUserReq {

    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "最早创建时间")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant earliestCreateTime;

    @ApiModelProperty(value="最晚创建时间")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant latestCreateTime;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value="角色")
    private Long roleId;

    @ApiModelProperty(value = "是否启用")
    private String enable;

    @ApiModelProperty(value = "地址id")
    private Short locationId;

    @ApiModelProperty(value = "页码")
    private Integer pageNum;

    @ApiModelProperty(value = "页大小")
    private Integer pageSize;
}
