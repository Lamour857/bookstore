package com.wj.bookstore.user.account.entity.req;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-09-13:35
 **/

@ApiModel(value = "UpdateRolePermissionReq",description = "更新角色权限请求参数")
@Data
public class UpdateRolePermissionReq {


    @ApiModelProperty(value = "权限id数组")
    private List<Long> permissionIds;

    @NotNull(message = "角色id不能为空")
    @ApiModelProperty(value = "角色id")
    private Long roleId;
}
