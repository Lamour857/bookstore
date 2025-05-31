package com.wj.bookstore.controller;

import com.wj.bookstore.common.vo.PageResult;
import com.wj.bookstore.common.vo.ResVo;
import com.wj.bookstore.user.account.entity.DTO.UserDTO;
import com.wj.bookstore.user.account.entity.req.ChangePasswordReq;
import com.wj.bookstore.user.account.entity.req.QueryUserReq;
import com.wj.bookstore.user.account.entity.req.UserReq;
import com.wj.bookstore.user.account.service.UserService;
import com.wj.bookstore.user.authentication.service.UserDetailsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-08-15:27
 **/
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('PersonalInfoManage')")
    @GetMapping("/info")
    public ResVo<UserDTO> getUserInfo() {

        return ResVo.ok(userService.getUserInfo());
    }

    @PreAuthorize("hasAuthority('PersonalInfoManage')")
    @GetMapping("/verifyCode/password")
    public ResVo<String> sendChangePasswordVerifyCode(
            @ApiParam(value = "手机号", required = true)
            @NotNull(message = "手机号不能为空")
            @Pattern(regexp = "^1[3-9]\\d{9}$" ,message = "手机号格式错误")
            @RequestParam String phone) {
        userService.sendVerifyCode(phone);
        return ResVo.ok();
    }

//    @PreAuthorize("hasAuthority('PersonalInfoManage')")
//    @PostMapping("password")
//    @ApiOperation(value = "修改密码")
//    public ResVo<String> changePassword(
//            @RequestBody ChangePasswordReq req) {
//        userService.changePassword(req);
//        return ResVo.ok();
//    }

    @PreAuthorize("hasAuthority('PersonalInfoManage')")
    @ApiOperation(value = "修改用户信息")
    @PostMapping("/update")
    public ResVo<String> updateUserInfo(
            @ApiParam(value = "用户更新请求参数",required = true)
            @RequestBody UserReq req) {
        userService.updateUserInfo(req);
        return ResVo.ok();
    }


    @PreAuthorize("hasAuthority('AccountManage')")
    @PostMapping("/query")
    @ApiOperation(value = "查询用户列表")
    public ResVo<PageResult<UserDTO>> queryUserList(
            @ApiParam(value = "用户查询请求参数",required = true)
            @RequestBody QueryUserReq req){
        return ResVo.ok(userService.queryUserList(req));
    }


    @PreAuthorize("hasAuthority('AccountManage')")
    @PostMapping("/status")
    @ApiOperation(value = "修改用户状态")
    public ResVo<String> updateUserStatus(
            @ApiParam(value = "用户id",required = true)
            @NotNull(message = "用户id不能为空")
            @RequestParam Long userId,
            @ApiParam(value = "是否启用",required = true)
            @NotNull(message = "是否启用不能为空")
            @RequestParam Boolean isEnable){
        userService.updateUserStatus(userId,isEnable);
        return ResVo.ok();
    }
    
}
