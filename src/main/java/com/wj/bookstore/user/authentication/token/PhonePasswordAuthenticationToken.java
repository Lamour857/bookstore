package com.wj.bookstore.user.authentication.token;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-19-21:56
 **/
public class PhonePasswordAuthenticationToken extends AuthenticationToken {
    private final Object phone;
    private Object password;
    public PhonePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.phone = principal;
        this.password = credentials;
        setAuthenticated(true);
    }
    public PhonePasswordAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.phone = principal;
        this.password = credentials;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return this.password;
    }

    @Override
    public Object getPrincipal() {
        return this.phone;
    }
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        super.setAuthenticated(isAuthenticated);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.password = null;
    }
}
