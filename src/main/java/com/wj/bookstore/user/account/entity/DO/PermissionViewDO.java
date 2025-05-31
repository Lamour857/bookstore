package com.wj.bookstore.user.account.entity.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-15-13:50
 **/
@Data
@TableName("sys_permission_view")
public class PermissionViewDO {
    private Long id;
    private Long permissionId;
    private Long parent;
    private String name;
    private String path;
    private String component;
    private String title;
}
