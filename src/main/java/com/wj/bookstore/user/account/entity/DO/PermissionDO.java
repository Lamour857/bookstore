package com.wj.bookstore.user.account.entity.DO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wj.bookstore.common.dto.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-05-18:35
 **/
@TableName("sys_permission")
@Data
@EqualsAndHashCode(callSuper = true)
public class PermissionDO extends BaseDO implements GrantedAuthority {
    private String path;
    private Short type;
    private String description;

    @Override
    public String getAuthority() {
        if(type==2){
            return path;
        }
        return "";
    }
}
