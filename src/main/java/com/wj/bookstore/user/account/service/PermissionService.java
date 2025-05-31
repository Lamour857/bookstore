package com.wj.bookstore.user.account.service;

import com.wj.bookstore.user.account.entity.DO.PermissionViewDO;
import com.wj.bookstore.user.account.entity.DTO.PermissionDTO;
import com.wj.bookstore.user.account.entity.DTO.PermissionViewDTO;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-09-11:58
 **/
public interface PermissionService {
    List<PermissionDTO> list();

    List<PermissionViewDTO> getViewsByPermissionId(Long id);
}
