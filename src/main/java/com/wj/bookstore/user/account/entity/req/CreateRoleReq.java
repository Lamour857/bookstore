package com.wj.bookstore.user.account.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-28-18:14
 **/
@Data
@ApiModel(value = "创建角色请求参数")
public class CreateRoleReq {

    @NotNull(message = "角色名称不能为空")
    @ApiModelProperty(value = "角色名称")
    private String name;

    @NotNull(message = "角色描述不能为空")
    @ApiModelProperty(value = "角色描述")
    private String description;

    @ApiModelProperty(value = "权限id列表")
    private List<Long> permissionIds;
}
