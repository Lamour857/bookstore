package com.wj.bookstore.user.account.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wj.bookstore.user.account.entity.DO.PermissionDO;
import com.wj.bookstore.user.account.entity.DO.RoleDO;
import com.wj.bookstore.user.account.entity.DO.RolePermissionDO;
import com.wj.bookstore.user.account.entity.req.QueryRoleReq;
import com.wj.bookstore.user.account.repository.mapper.PermissionMapper;
import com.wj.bookstore.user.account.repository.mapper.RoleMapper;
import com.wj.bookstore.user.account.repository.mapper.RolePermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.security.Permission;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-08-21:51
 **/
@Repository
public class RoleDao extends ServiceImpl<RoleMapper, RoleDO> {
    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    public Set<PermissionDO> getPermissionByRoleId(Long roleId) {
        LambdaQueryWrapper<RolePermissionDO> queryRolePermission  = Wrappers.lambdaQuery();
        queryRolePermission.eq(RolePermissionDO::getRoleId, roleId);
        List<RolePermissionDO> rolePermissions = rolePermissionMapper.selectList(queryRolePermission);
        Set<PermissionDO> permissions = new HashSet<>();
        for(RolePermissionDO rolePermissionDO : rolePermissions){
            LambdaQueryWrapper<PermissionDO> queryPermission = Wrappers.lambdaQuery();
            queryPermission.eq(PermissionDO::getId, rolePermissionDO.getPermissionId());
            permissions.add(permissionMapper.selectOne(queryPermission));
        }
        return permissions;
    }

    public void clearPermissionByRoleId(Long roleId) {
        LambdaQueryWrapper<RolePermissionDO> queryRolePermission  = Wrappers.lambdaQuery();
        queryRolePermission.eq(RolePermissionDO::getRoleId, roleId);
        rolePermissionMapper.delete(queryRolePermission);
    }


    public void saveRolePermission(RolePermissionDO rolePermissionDO) {
        rolePermissionMapper.insert(rolePermissionDO);
    }

    public Page<RoleDO> queryByPage(QueryRoleReq req) {
        if(req.getPageSize()==null){
            req.setPageSize(10000);
        }
        if(req.getPageNum()==null){
            req.setPageNum(1);
        }
        Page<RoleDO> page = new Page<>(req.getPageNum(), req.getPageSize());
        LambdaQueryWrapper<RoleDO> queryRole= Wrappers.lambdaQuery();
        if(req.getId()!=null){
            queryRole.eq(RoleDO::getId, req.getId());
        }
        if(req.getName()!=null){
            queryRole.like(RoleDO::getName, req.getName());
        }
        if(req.getDescription()!=null){
            queryRole.like(RoleDO::getDescription, req.getDescription());
        }
        if(req.getEarliestCreateTime()!=null){
            queryRole.ge(RoleDO::getCreateTime, req.getEarliestCreateTime());
        }
        if(req.getLatestCreateTime()!=null){
            queryRole.le(RoleDO::getCreateTime, req.getLatestCreateTime());
        }
        Page<RoleDO> pageRole = this.page(page, queryRole);
        for(RoleDO roleDO:pageRole.getRecords()){
            roleDO.setPermissions(getPermissionByRoleId(roleDO.getId()));
        }
        return pageRole;
    }

    public RoleDO getByName(String name) {
        LambdaQueryWrapper<RoleDO> queryRole = Wrappers.lambdaQuery();
        queryRole.eq(RoleDO::getName, name);
        return getBaseMapper().selectOne(queryRole);
    }



    public List<RoleDO> listByStatus(int i) {
        LambdaQueryWrapper<RoleDO> queryRole = Wrappers.lambdaQuery();
        queryRole.eq(RoleDO::getStatus, i);
        return getBaseMapper().selectList(queryRole);
    }
}
