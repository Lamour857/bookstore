package com.wj.bookstore.user.account.entity.DO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wj.bookstore.common.dto.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-05-18:31
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class RoleDO extends BaseDO {
    private String name;
    private String description;
    private short status;
    @TableField(exist = false)
    private Set<PermissionDO> permissions;
}
