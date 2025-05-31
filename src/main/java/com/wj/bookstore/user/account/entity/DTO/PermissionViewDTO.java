package com.wj.bookstore.user.account.entity.DTO;

import lombok.Data;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-15-14:45
 **/
@Data
public class PermissionViewDTO {
    private Long id;
    private Long permissionId;
    private Long parent;
    private String name;
    private String path;
    private String component;
    private String title;
    private List<PermissionViewDTO> children;
}
