package com.wj.bookstore.user.account.entity.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-05-19:53
 **/
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role_permission")
public class RolePermissionDO {
    private Long roleId;
    private Long permissionId;
}
