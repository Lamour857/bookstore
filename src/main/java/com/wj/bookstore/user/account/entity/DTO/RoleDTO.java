package com.wj.bookstore.user.account.entity.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wj.bookstore.common.dto.BaseDTO;
import com.wj.bookstore.user.account.entity.DO.PermissionDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-08-21:47
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleDTO extends BaseDTO {
    private String name;
    private String description;
    private String status;
    private List<PermissionDTO> permissions;
}
