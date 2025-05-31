package com.wj.bookstore.main.config;


import com.wj.bookstore.user.authentication.filter.AuthenticationFilter;
import com.wj.bookstore.user.authentication.provider.AuthenticationProvider;
import com.wj.bookstore.user.authentication.filter.JwtFilter;
import com.wj.bookstore.user.authentication.entrypoint.CustomerAuthenticationEntryPoint;
import com.wj.bookstore.user.authentication.handler.CustomerAccessDeniedHandler;
import com.wj.bookstore.user.authentication.handler.LoginFailureHandler;
import com.wj.bookstore.user.authentication.handler.LoginSuccessHandler;
import com.wj.bookstore.user.authentication.handler.LogoutSuccessHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-19-16:46
 **/
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    // 循环依赖, 不能autowired, 通过构造函数注入
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private CustomerAuthenticationEntryPoint customizeAuthenticationEntryPoint;
    @Autowired
    private CustomerAccessDeniedHandler customerAccessDeniedHandler;
    @Autowired
    private LogoutSuccessHandler customerLogoutSuccessHandler;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    CorsConfigurationSource corsConfigurationSource;
    @Autowired
    private JwtFilter jwtFilter;
    private static final String[] AUTH_WHITELIST = {
            "/alipay/**",
            "/public/**",
            "/doc.html",
            "/webjars/**",
            "/swagger-resources/**",
            "/v2/**",
            "/favicon.ico"
    };
    private void commonSecurity(HttpSecurity http) throws Exception{
        http.csrf().disable()
                .cors().configurationSource(corsConfigurationSource)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customizeAuthenticationEntryPoint)
                .accessDeniedHandler(customerAccessDeniedHandler)
                .and()
                .logout().logoutUrl("/auth/logout").logoutSuccessHandler(customerLogoutSuccessHandler);
    }
    @Bean
    @Order(1)
    public SecurityFilterChain phonePasswordFilterChain(HttpSecurity http) throws Exception {
        commonSecurity(http);
        http.antMatcher("/auth/**")
                .authorizeRequests(request -> request.
                        anyRequest().permitAll()
                ).addFilterAt(phonePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    @Order(3)
    public SecurityFilterChain otherSecurityFilterChain(HttpSecurity http) throws Exception {
        commonSecurity(http);
        http.authorizeRequests(authorize->authorize
                .antMatchers("/public/**").permitAll()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class));
        return http.build();
    }
    public AuthenticationFilter phonePasswordAuthenticationFilter()  {
        AuthenticationFilter filter = new AuthenticationFilter();
        filter.setAuthenticationManager(authenticationProvider::authenticate);
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
        filter.setAuthenticationFailureHandler(loginFailureHandler);
        return filter;
    }
}
