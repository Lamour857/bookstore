package com.wj.bookstore.controller;

import com.wj.bookstore.common.vo.PageResult;
import com.wj.bookstore.common.vo.ResVo;
import com.wj.bookstore.user.account.entity.DTO.RoleDTO;
import com.wj.bookstore.user.account.entity.req.CreateRoleReq;
import com.wj.bookstore.user.account.entity.req.QueryRoleReq;
import com.wj.bookstore.user.account.entity.req.UpdateRolePermissionReq;
import com.wj.bookstore.user.account.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-08-21:45
 **/
@RestController
@RequestMapping("/role")
@Api(tags = "角色管理")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "获取角色列表")
    @PreAuthorize("hasAuthority('RoleManage')")
    @GetMapping("/list")
    public ResVo<List<RoleDTO>> getRoleList() {
        return ResVo.ok(roleService.getRoleList());
    }

    @ApiOperation(value = "获取角色列表")
    @PostMapping("/query")
    public ResVo<PageResult<RoleDTO>> queryRoleList(
            @ApiParam(value = "角色查询请求参数",required = true)
            @RequestBody QueryRoleReq req) {
        return ResVo.ok(roleService.queryRoleList(req));
    }

    @ApiOperation(value = "创建角色")
    @PreAuthorize("hasAuthority('RoleManage')")
    @PostMapping("/create")
    public ResVo<String> createRole(
            @ApiParam(value = "角色创建请求参数",required = true)
            @Valid
            @RequestBody CreateRoleReq req) {
        roleService.createRole(req);
        return ResVo.ok();
    }

    @ApiOperation(value = "禁用角色")
    @PreAuthorize("hasAuthority('RoleManage')")
    @DeleteMapping("/delete")
    public ResVo<String> deleteRole(
            @ApiParam(value = "角色id",required = true)
            @NotNull(message = "角色id不能为空")
            @RequestParam Long roleId) {
        roleService.deleteRole(roleId);
        return ResVo.ok();
    }

    @ApiOperation(value = "解禁")
    @PreAuthorize("hasAuthority('RoleManage')")
    @PostMapping("/enable")
    public ResVo<String> enableRole(
            @ApiParam(value = "角色id",required = true)
            @NotNull(message = "角色id不能为空")
            @RequestParam Long roleId) {
        roleService.enableRole(roleId);
        return ResVo.ok();
    }

    @ApiOperation(value = "更新角色权限")
    @PreAuthorize("hasAuthority('RoleManage')")
    @PostMapping("/updatePermission")
    public ResVo<String> updateRolePermission(
            @ApiParam(value = "更新角色权限请求参数",required = true)
            @Valid
            @RequestBody UpdateRolePermissionReq req) {
        roleService.updateRolePermission(req);
        return ResVo.ok();
    }

    @ApiOperation(value = "分配角色")
    @PreAuthorize("hasAuthority('RoleManage')")
    @PostMapping("/allocate")
    public ResVo<String> allocateRole(
            @ApiParam(value = "用户id",required = true)
            @NotNull(message = "用户id不能为空")
            @RequestParam Long userId,
            @ApiParam(value = "角色id",required = true)
            @NotNull(message = "角色id不能为空")
            @RequestParam Long roleId) {
        roleService.allocateRole(userId, roleId);
        return ResVo.ok();
    }
}
