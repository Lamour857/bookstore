package com.wj.bookstore.user.account.converter;

import com.wj.bookstore.common.enums.YesOrNoEnum;
import com.wj.bookstore.common.utils.DateUtil;
import com.wj.bookstore.user.account.entity.DO.RoleDO;
import com.wj.bookstore.user.account.entity.DTO.PermissionDTO;
import com.wj.bookstore.user.account.entity.DTO.RoleDTO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-08-22:12
 **/
@Component
public class RoleConverter {
    public RoleDTO  toRoleDTO(RoleDO roleDO) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(roleDO.getId());
        roleDTO.setName(roleDO.getName());
        roleDTO.setCreateTime(DateUtil.convert(roleDO.getCreateTime()));
        roleDTO.setStatus(roleDO.getStatus() == YesOrNoEnum.YES.getCode()?"正常":"禁用");
        roleDTO.setUpdateTime(DateUtil.convert(roleDO.getUpdateTime()));
        roleDTO.setDescription(roleDO.getDescription());
        List<PermissionDTO> permissionDTOList = roleDO.getPermissions().stream().map(permissionDO -> {
            if (permissionDO == null) {
                return null;
            }
            PermissionDTO permissionDTO = new PermissionDTO();
            permissionDTO.setId(permissionDO.getId());
            permissionDTO.setCreateTime(DateUtil.convert(permissionDO.getCreateTime()));
            permissionDTO.setUpdateTime(DateUtil.convert(permissionDO.getUpdateTime()));
            permissionDTO.setType(permissionDO.getType());
            permissionDTO.setPath(permissionDO.getPath());
            permissionDTO.setDescription(permissionDO.getDescription());
            return permissionDTO;
        }).toList();
        roleDTO.setPermissions(permissionDTOList);
        return roleDTO;
    }
}
