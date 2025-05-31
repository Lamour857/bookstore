package com.wj.bookstore.controller;


import com.wj.bookstore.common.vo.ResVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import javax.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-14-11:29
 **/
@RestController
@RequestMapping("/auth")
@Api(tags = "登录管理")
@Validated
@Slf4j
public class LoginController {

    @PostMapping("/password")
    @ApiOperation(value = "密码登录")
    public ResVo<String> loginByPassword(
            @Pattern(regexp = "^1[3-9]\\d{9}$" ,message = "手机号格式错误")
            @RequestParam(name="phone")
            @ApiParam(value="手机号",required = true) String phone,
            @RequestParam(name="password")
            @Pattern(regexp="^[A-Za-z0-9]{6,12}$",message = "密码仅字母+数字，且长度为6-12位")
            @ApiParam(value="密码",required = true) String password){

        log.info("登录请求");
        return ResVo.ok();
    }

    @PostMapping("/verifyCode")
    @ApiOperation(value = "验证码登录")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "登录成功"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "登录失败")
    })
    public ResVo<String> loginByVerifyCode(
            @Pattern(regexp = "^1[3-9]\\d{9}$" ,message = "手机号格式错误")
            @RequestParam(name="phone")
            @ApiParam(value="手机号",required = true) String phone,
            @Pattern(regexp = "^\\d{6}$" ,message = "验证码格式错误")
            @RequestParam(name="password")
            @ApiParam(value="验证码",required = true)String verifyCode){
        return ResVo.ok();
    }

    @PostMapping("/logout")
    @ApiOperation(value = "退出登录")
    public ResVo<String> logout(){
        return ResVo.ok();
    }





}
