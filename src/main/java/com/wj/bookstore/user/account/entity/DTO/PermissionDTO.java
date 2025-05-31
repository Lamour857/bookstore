package com.wj.bookstore.user.account.entity.DTO;

import com.wj.bookstore.common.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-08-21:48
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class PermissionDTO extends BaseDTO {
    private Boolean deleted;
    private String path;
    private Short type;
    private String method;
    private String description;
}
