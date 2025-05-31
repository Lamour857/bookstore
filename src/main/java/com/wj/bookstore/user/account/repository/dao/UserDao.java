package com.wj.bookstore.user.account.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.wj.bookstore.common.enums.GenderEnum;
import com.wj.bookstore.common.enums.YesOrNoEnum;
import com.wj.bookstore.user.account.entity.DO.*;
import com.wj.bookstore.user.account.entity.req.QueryUserReq;
import com.wj.bookstore.user.account.repository.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-15-13:25
 **/
@Repository
public class UserDao extends ServiceImpl<UserMapper, UserDO> {

    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public List<UserDO> list(){
        LambdaQueryWrapper<UserDO> queryUser = Wrappers.lambdaQuery();
        queryUser.eq(UserDO::isEnabled, YesOrNoEnum.YES.getCode());
        List<UserDO> userDOs = getBaseMapper().selectList(queryUser);
        for(UserDO userDO:userDOs){
            userDO.setRoles(getUserRole(userDO.getRoleId()));
        }
       return userDOs;
    }

    public UserDO getUserByPhone(String phone){
        LambdaQueryWrapper<UserDO> queryUser = Wrappers.lambdaQuery();
        queryUser.eq(UserDO::getPhone, phone)
                .eq(UserDO::isEnabled, YesOrNoEnum.YES.getCode())
                .last("limit 1");
        UserDO userDO =getBaseMapper().selectOne(queryUser);
        userDO.setRoles(getUserRole(userDO.getRoleId()));
        return userDO;
    }

    public RoleDO getUserRole(Long roleId) {

        // 查询用户对应的角色id

        RoleDO roleDO = getRoleById(roleId);
        LambdaQueryWrapper<RolePermissionDO> queryRolePermission = Wrappers.lambdaQuery();
        if (roleDO.getPermissions() == null) {
            roleDO.setPermissions(new HashSet<>());
        }
        // 查询角色对应的权限id
        queryRolePermission.eq(RolePermissionDO::getRoleId, roleDO.getId());
        // 该角色对应的所有权限id
        List<RolePermissionDO> rolePermissions = rolePermissionMapper.selectList(queryRolePermission);
        rolePermissions.forEach(rolePermissionDO -> {
            LambdaQueryWrapper<PermissionDO> queryPermission = Wrappers.lambdaQuery();
            queryPermission.eq(PermissionDO::getId, rolePermissionDO.getPermissionId());
            // 查询权限信息
            List<PermissionDO> permissions = permissionMapper.selectList(queryPermission);
            roleDO.getPermissions().addAll(permissions);
        });
        return roleDO;
    }

    public RoleDO getRoleById(Long roleId){
        LambdaQueryWrapper<RoleDO> queryRole = Wrappers.lambdaQuery();
        queryRole.eq(RoleDO::getId, roleId)
                .last("limit 1");
        return roleMapper.selectOne(queryRole);
    }
    public UserDO getUserByUserName(String userName){
        LambdaQueryWrapper<UserDO> queryUser = Wrappers.lambdaQuery();
        queryUser.eq(UserDO::getUsername, userName)
                .eq(UserDO::isEnabled, YesOrNoEnum.YES.getCode())
                .last("limit 1");
        UserDO userDO = getBaseMapper().selectOne(queryUser);
        userDO.setRoles(getUserRole(userDO.getRoleId()));
        return userDO;
    }


    public Page<UserDO> listByPage(YesOrNoEnum yesOrNoEnum, int pageNum, int pageSize) {
        Page<UserDO> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<UserDO> queryCategory = Wrappers.lambdaQuery();
        Page<UserDO> userDOPage = this.page(page, queryCategory);
        for(UserDO userDO:userDOPage.getRecords()){
            userDO.setRoles(getUserRole(userDO.getRoleId()));
        }
        return userDOPage;
    }

    public Page<UserDO> queryByPage(QueryUserReq req) {
        Page<UserDO> page = new Page<>(req.getPageNum(), req.getPageSize());
        LambdaQueryWrapper<UserDO> queryUser= Wrappers.lambdaQuery();
        if(req.getId()!=null){
            queryUser.eq(UserDO::getId, req.getId());
        }
        if(req.getRoleId()!=null){
            queryUser.eq(UserDO::getRoleId, req.getRoleId());
        }

        if(req.getUsername()!=null){
            queryUser.like(UserDO::getUsername, req.getUsername());
        }
        if(req.getPhone()!=null){
            queryUser.eq(UserDO::getPhone, req.getPhone());
        }
        if(req.getEnable()!=null){
            queryUser.eq(UserDO::isEnabled, req.getEnable());
        }
        if(req.getLocationId()!=null){
            queryUser.eq(UserDO::getLocationId, req.getLocationId());
        }
        if(req.getGender()!=null){
            queryUser.eq(UserDO::getGender, GenderEnum.getCode(req.getGender()));
        }
        if(req.getEarliestCreateTime()!=null){
            queryUser.ge(UserDO::getCreateTime, req.getEarliestCreateTime());
        }
        if(req.getLatestCreateTime()!=null){
            queryUser.le(UserDO::getCreateTime, req.getLatestCreateTime());
        }
        Page<UserDO> userDOPage = this.page(page, queryUser);
        for(UserDO userDO:userDOPage.getRecords()){
            userDO.setRoles(getUserRole(userDO.getRoleId()));
        }
        return userDOPage;
    }

    public List<UserDO> listByRoleId(Long roleId) {
        LambdaQueryWrapper<UserDO> queryUser = Wrappers.lambdaQuery();
        queryUser.eq(UserDO::getRoleId, roleId);
        return getBaseMapper().selectList(queryUser);
    }
}
