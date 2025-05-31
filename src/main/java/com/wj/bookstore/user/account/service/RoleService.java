package com.wj.bookstore.user.account.service;

import com.wj.bookstore.common.vo.PageResult;
import com.wj.bookstore.user.account.entity.DTO.RoleDTO;
import com.wj.bookstore.user.account.entity.req.CreateRoleReq;
import com.wj.bookstore.user.account.entity.req.QueryRoleReq;
import com.wj.bookstore.user.account.entity.req.UpdateRolePermissionReq;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-08-21:46
 **/
public interface RoleService {
    List<RoleDTO> getRoleList();

    void updateRolePermission(UpdateRolePermissionReq req);

    void allocateRole(Long userId, Long roleId);

    PageResult<RoleDTO> queryRoleList(QueryRoleReq req);

    void createRole(CreateRoleReq req);

    void deleteRole(Long roleId);

    void enableRole(Long roleId);
}
