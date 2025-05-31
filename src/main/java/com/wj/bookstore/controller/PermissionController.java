package com.wj.bookstore.controller;

import com.wj.bookstore.common.vo.ResVo;
import com.wj.bookstore.user.account.entity.DTO.PermissionDTO;
import com.wj.bookstore.user.account.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-09-11:55
 **/
@RestController
@Slf4j
@Api(tags = "权限管理")
@RequestMapping(value = "/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @PreAuthorize("hasAuthority('RoleManage')")
    @ApiOperation(value = "获取权限列表")
    @GetMapping("/list")
    public ResVo<List<PermissionDTO>> list(){
        return ResVo.ok(permissionService.list());
    }
}
