package com.wj.bookstore.user.authentication.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.wj.bookstore.common.utils.JsonUtil;
import com.wj.bookstore.user.authentication.cache.TokenCache;
import com.wj.bookstore.user.authentication.token.AuthenticationToken;
import com.wj.bookstore.user.account.entity.DO.UserDO;
import com.wj.bookstore.user.account.entity.DO.RoleDO;
import com.wj.bookstore.user.account.repository.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-14-13:16
 **/
@Component
@Slf4j
public class JwtService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private TokenCache tokenCache;
    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.issuer}")
    private String issuer;
    @PostConstruct
    private void init(){
        algorithm=Algorithm.HMAC256(secret);
        verifier=JWT.require(algorithm).withIssuer(issuer).build();
    }

    private Algorithm algorithm;
    private JWTVerifier verifier;
    public JwtService(){

    }

    // 生成 Token
    public String generateToken(UserDO user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("userId", user.getId());
        claims.put("roles", user.getRoles().getId());
        String token=createToken(claims);
        // token缓存 key: token ->value: userDO
        tokenCache.cacheToken(token,user,expiration);
        return token;
    }

    private String createToken(Map<String, Object> claims) {
        String token=JWT.create().withIssuer(issuer)
                .withExpiresAt(new Date(System.currentTimeMillis()+expiration))
                .withPayload(claims)
                .sign(algorithm);
        return token;
    }
    private void clearToken(String token){
        if(tokenCache.getUserByToken(token)!=null){
            tokenCache.deleteToken(token);
        }
    }
    public AuthenticationToken getAuthentication(String token){
        AuthenticationToken authenticationToken;
        // 若token非法或失效, 直接验签失败
        try{
            DecodedJWT decodedJWT=verifier.verify(token);
            String pay=new String(Base64Utils.decodeFromString(decodedJWT.getPayload()));
            String username=String.valueOf(JsonUtil.toObj(pay, HashMap.class).get("username"));
            // 从redis中获取userDO，解决用户登出，后台失效jwt token的问题
            UserDO userInRedis= tokenCache.getUserByToken(token);
            if(userInRedis==null||!Objects.equals(username,userInRedis.getUsername())){
                return null;
            }
            // 根据用户名读取用户信息
            userInRedis.setRoles(userDao.getUserRole(userInRedis.getRoleId()));
            Collection<GrantedAuthority> authorities = new ArrayList<>(userInRedis.getRoles().getPermissions());
            authenticationToken=new AuthenticationToken( authorities);
            authenticationToken.setAuthenticated(true);
            authenticationToken.setToken(token);
            authenticationToken.setUser(userInRedis);
            return authenticationToken;
        }catch (Exception e){
            log.info("jwt token校验失败! token: {}, msg: {}",token,e.getMessage());
            return null;
        }
    }
}
