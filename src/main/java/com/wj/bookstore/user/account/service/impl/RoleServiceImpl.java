package com.wj.bookstore.user.account.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wj.bookstore.common.enums.BaseRoleEnum;
import com.wj.bookstore.common.enums.StatusEnum;
import com.wj.bookstore.common.utils.ExceptionUtil;
import com.wj.bookstore.common.vo.PageResult;
import com.wj.bookstore.user.account.converter.RoleConverter;
import com.wj.bookstore.user.account.entity.DO.*;
import com.wj.bookstore.user.account.entity.DTO.RoleDTO;
import com.wj.bookstore.user.account.entity.req.CreateRoleReq;
import com.wj.bookstore.user.account.entity.req.QueryRoleReq;
import com.wj.bookstore.user.account.entity.req.UpdateRolePermissionReq;
import com.wj.bookstore.user.account.repository.dao.PermissionDao;
import com.wj.bookstore.user.account.repository.dao.RoleDao;
import com.wj.bookstore.user.account.repository.dao.UserDao;
import com.wj.bookstore.user.account.service.RoleService;
import com.wj.bookstore.user.merchant.entity.DO.MerchantDO;
import com.wj.bookstore.user.merchant.entity.DTO.MerchantDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-08-21:46
 **/
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleConverter roleConverter;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private UserDao userDao;

    @Override
    public List<RoleDTO> getRoleList() {
        List<RoleDTO> result=new ArrayList<>();
        List<RoleDO> list=roleDao.listByStatus(1);
        if(list==null) {
            return List.of();
        }
        for(RoleDO roleDO:list){
            roleDO.setPermissions(roleDao.getPermissionByRoleId(roleDO.getId()));
            result.add(roleConverter.toRoleDTO(roleDO));
        }
        return result;
    }

    @Override
    public void updateRolePermission(UpdateRolePermissionReq req) {
        if(req==null){
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"请求参数为空");
        }
        Long roleId=req.getRoleId();
        if(roleId==null){
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"角色id为空");
        }
        // 清除该角色权限的旧数据
        roleDao.clearPermissionByRoleId(roleId);

        for(Long permissionId:req.getPermissionIds()){
            if(permissionId==null){
                throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"权限id为空");
            }
            PermissionDO permissionDO=permissionDao.getById(permissionId);
            if(permissionDO==null){
                throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"权限不存在");
            }
            roleDao.saveRolePermission(new RolePermissionDO(roleId, permissionId));
        }
    }

    @Override
    public void allocateRole(Long userId, Long roleId) {
        UserDO userDO=userDao.getById(userId);
        if(userDO==null){
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"用户不存在");
        }
        RoleDO roleDO=roleDao.getById(roleId);
        if(roleDO==null){
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"角色不存在");
        }
        if(userDO.getRoleId().equals(roleId)){
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"用户已拥有该角色");
        }
        userDO.setRoleId(roleId);
        userDao.updateById(userDO);
    }

    @Override
    public PageResult<RoleDTO> queryRoleList(QueryRoleReq req) {
        PageResult<RoleDTO> pageResult=new PageResult<>();
        // 从数据库中获取数据
        Page<RoleDO> page= roleDao.queryByPage(req);
        pageResult.setPageNum(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        pageResult.setTotal(page.getTotal());
        List<RoleDTO> roleDTOList = new ArrayList<>();
        for (RoleDO role : page.getRecords()) {
            roleDTOList.add(roleConverter.toRoleDTO(role));
        }
        pageResult.setList(roleDTOList);
        pageResult.setPages(page.getPages());
        return pageResult;
    }

    @Transactional
    @Override
    public void createRole(CreateRoleReq req) {
        RoleDO roleDO=roleDao.getByName(req.getName());
        if(roleDO!=null){
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"角色已存在");
        }
        roleDO=new RoleDO();
        roleDO.setName(req.getName());
        roleDO.setDescription(req.getDescription());
        roleDao.save(roleDO);
        for(Long permissionId:req.getPermissionIds()){
            PermissionDO permissionDO=permissionDao.getById(permissionId);
            if(permissionDO==null){
                throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"权限不存在");
            }
            roleDao.saveRolePermission(new RolePermissionDO(roleDO.getId(), permissionId));
        }
    }

    @Override
    @Transactional
    public void deleteRole(Long roleId) {
        RoleDO roleDO=roleDao.getById(roleId);
        if(roleDO==null){
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"角色不存在");
        }
        roleDO.setStatus((short) 0);
        roleDao.updateById(roleDO);
        List<UserDO> userDOList=userDao.listByRoleId(roleId);
        for(UserDO userDO:userDOList){
            userDO.setRoleId(BaseRoleEnum.CUSTOMER.getCode());
            userDao.updateById(userDO);
        }
    }

    @Override
    public void enableRole(Long roleId) {
        RoleDO roleDO=roleDao.getById(roleId);
        if(roleDO==null){
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"角色不存在");
        }
        roleDO.setStatus((short) 1);
        roleDao.updateById(roleDO);
    }
}
