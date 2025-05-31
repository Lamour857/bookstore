package com.wj.bookstore.user.account.service.impl;

import com.wj.bookstore.common.enums.PermissionTypeEnum;
import com.wj.bookstore.user.account.converter.RoleConverter;
import com.wj.bookstore.user.account.entity.DO.PermissionDO;
import com.wj.bookstore.user.account.entity.DO.PermissionViewDO;
import com.wj.bookstore.user.account.entity.DTO.PermissionDTO;
import com.wj.bookstore.user.account.entity.DTO.PermissionViewDTO;
import com.wj.bookstore.user.account.repository.dao.PermissionDao;
import com.wj.bookstore.user.account.repository.dao.PermissionViewDao;
import com.wj.bookstore.user.account.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-09-11:58
 **/
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private PermissionViewDao permissionViewDao;
    @Autowired
    private RoleConverter roleConverter;
    @Override
    public List<PermissionDTO> list() {
        List<PermissionDO> permissionDOList = permissionDao.list();
        if(permissionDOList!=null){
            return permissionDOList.stream().map(permissionDO -> {
                PermissionDTO permissionDTO = new PermissionDTO();
                permissionDTO.setId(permissionDO.getId());
                permissionDTO.setPath(permissionDO.getPath());
                permissionDTO.setType(permissionDO.getType());
                permissionDTO.setDescription(permissionDO.getDescription());
                return permissionDTO;
            }).toList();
        }
        return List.of();
    }

    @Override
    public List<PermissionViewDTO> getViewsByPermissionId(Long id) {
        List<PermissionViewDO> permissionDOList = permissionViewDao.getViewsByPermissionId(id);
        Map<Long, PermissionViewDTO> dtoMap = permissionDOList.stream()
                .map((permissionViewDO -> {
                    PermissionViewDTO dto = new PermissionViewDTO();
                    dto.setId(permissionViewDO.getId());
                    dto.setName(permissionViewDO.getName());
                    dto.setParent(permissionViewDO.getParent());
                    dto.setPath(permissionViewDO.getPath());
                    dto.setComponent(permissionViewDO.getComponent());
                    dto.setTitle(permissionViewDO.getTitle());
                    return dto;
                }))
                .collect(Collectors.toMap(PermissionViewDTO::getId, dto -> dto));
        List<PermissionViewDTO> tree=new ArrayList<>();
        for(PermissionViewDTO dto:dtoMap.values()){
            if(dto.getParent()==0){
                tree.add(dto);
            }else{
                PermissionViewDTO parent=dtoMap.get(dto.getParent());
                if(parent!=null){
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(dto);
                }
            }
        }
        return tree;
    }
}
