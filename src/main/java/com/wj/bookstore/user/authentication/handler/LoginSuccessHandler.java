package com.wj.bookstore.user.authentication.handler;


import com.wj.bookstore.common.enums.BaseRoleEnum;
import com.wj.bookstore.common.enums.StatusEnum;
import com.wj.bookstore.common.exception.BookStoreAuthenticationException;
import com.wj.bookstore.common.utils.ExceptionUtil;
import com.wj.bookstore.common.utils.JsonUtil;
import com.wj.bookstore.common.vo.ResVo;
import com.wj.bookstore.user.account.converter.UserConverter;
import com.wj.bookstore.user.account.entity.DO.PermissionDO;
import com.wj.bookstore.user.account.entity.DO.PermissionViewDO;
import com.wj.bookstore.user.account.entity.DO.RoleDO;
import com.wj.bookstore.user.account.entity.DTO.LoginResponseDTO;
import com.wj.bookstore.user.account.entity.DTO.PermissionViewDTO;
import com.wj.bookstore.user.account.service.PermissionService;
import com.wj.bookstore.user.authentication.service.JwtService;
import com.wj.bookstore.user.authentication.token.AuthenticationToken;
import com.wj.bookstore.user.account.entity.DO.UserDO;
import com.wj.bookstore.user.merchant.converter.MerchantConverter;
import com.wj.bookstore.user.merchant.entity.DO.MerchantDO;
import com.wj.bookstore.user.merchant.entity.DTO.MerchantDTO;
import com.wj.bookstore.user.merchant.repository.dao.MerchantDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-19-21:26
 **/
@Component
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private MerchantConverter merchantConverter;

    @Autowired
    private MerchantDao merchantDao;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserConverter userConverter;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        response.setContentType("application/json;charset=UTF-8");

        // 生成token
        UserDO user=((AuthenticationToken) authentication).getUser();
        log.info("用户 {}: 登录成功",user.getUsername());
        LoginResponseDTO loginResponseDTO=new LoginResponseDTO();
        try {
            String token = jwtService.generateToken(user);
            loginResponseDTO.setToken(token);
            // 设置路由权限
            Set<PermissionViewDTO> permissions = new HashSet<>();
            RoleDO role = user.getRoles();
            for (PermissionDO permission : role.getPermissions()) {
                List<PermissionViewDTO> permissionViews = permissionService.getViewsByPermissionId(permission.getId());
                permissions.addAll(permissionViews);
            }
            loginResponseDTO.setPermissions(permissions.stream().toList());
            // 设置用户信息
            loginResponseDTO.setUserInfo(userConverter.toUserDTO(user));
            if(user.getRoleId().equals(BaseRoleEnum.SELLER.getCode())){
                MerchantDO merchant = merchantDao.getByUserId(user.getId());
                if(merchant!=null){
                    MerchantDTO merchantDTO = merchantConverter.toDTO(merchant);
                    loginResponseDTO.setMerchantInfo(merchantDTO);
                }
            }
            JsonUtil.writeResVoToResponse(response, ResVo.ok(loginResponseDTO));

        }catch(RedisConnectionFailureException e){
            throw new BookStoreAuthenticationException(StatusEnum.UNEXPECT_ERROR,"Redis连接失败");
        }catch (IOException e) {
            throw ExceptionUtil.ofAuthenticationException(StatusEnum.UNEXPECT_ERROR,e.getMessage());
        }

    }
}
