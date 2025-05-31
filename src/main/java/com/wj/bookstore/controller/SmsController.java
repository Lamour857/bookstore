package com.wj.bookstore.controller;


import com.wj.bookstore.common.enums.StatusEnum;
import com.wj.bookstore.common.enums.VerifyCodeTypeEnum;
import com.wj.bookstore.common.vo.ResVo;
import com.wj.bookstore.core.sms.SmsService;
import com.wj.bookstore.user.account.repository.dao.UserDao;
import com.wj.bookstore.user.account.entity.DO.UserDO;
import io.swagger.annotations.*;
import javax.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-14-15:10
 **/
@RestController
@RequestMapping("/public/verifyCode")
@Api(tags = "短信api")
@Validated
@Slf4j
public class SmsController {
    @Autowired
    SmsService smsService;
    @Autowired
    UserDao userDao;
    @PostMapping("/login")
    @ApiOperation(value = "获取登录验证码")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "获取成功"),
            @ApiResponse(code = 400, message = "用户不存在,获取失败")
    })
    public ResVo<Boolean> getLoginVerifyCode(
            @Pattern(regexp = "^1[3-9]\\d{9}$" ,message = "手机号格式错误")
            @RequestParam(name="phone")
            @ApiParam(name = "phone", required = true) String phone){
        UserDO userDO=userDao.getUserByPhone(phone);
        if(userDO==null){
            return ResVo.fail(StatusEnum.USER_NOT_EXISTS);
        }
        smsService.sendVerifyCode(phone, VerifyCodeTypeEnum.LOGIN.getType());
        return ResVo.ok(true);
    }
    @PostMapping("/register")
    @ApiOperation(value = "获取注册验证码")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "获取成功"),
            @ApiResponse(code = 400, message = "用户不存在,获取失败")
    })
    public ResVo<Boolean> getRegisterVerifyCode(
            @Pattern(regexp = "^1[3-9]\\d{9}$" ,message = "手机号格式错误")
            @RequestParam(name="phone")
            @ApiParam(name = "phone", required = true) String phone){
        // 发送注册验证码时, 检查手机号是否被注册
        UserDO userDO=userDao.getUserByPhone(phone);
        if(userDO!=null){
           return ResVo.fail(StatusEnum.USER_EXISTS,"手机号已被注册");
        }
        smsService.sendVerifyCode(phone, VerifyCodeTypeEnum.REGISTER.getType());
        return ResVo.ok(true);
    }
}
