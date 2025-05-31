package com.wj.bookstore.user.account.entity.DO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import com.wj.bookstore.common.dto.BaseDO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-15-13:17
 **/
@Data
@EqualsAndHashCode(callSuper= true)
@Accessors(chain = true)
@ToString
@TableName("user")
public class UserDO extends BaseDO implements Serializable, UserDetails {
    private static final long serialVersionUID = 15648231351L;
    // 第三方用户ID
    private String thirdAccountId;
    private String username;
    private String phone;
    @JsonIgnore
    private String password;
    private Long roleId;

    private boolean enabled;
    // 账户是否过期
    private boolean isAccountNonExpired=true;
    // 账户是否被锁定
    private boolean isAccountNonLocked=true;
    //密码是否过期
    private boolean isCredentialsNonExpired=true;
    private Short gender;
    private Short locationId;
    private String profile;
    private Date birthday;
    @JsonIgnore
    @TableField(exist = false)
    private RoleDO roles;



    // 返回用户所对应的权限列表
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
