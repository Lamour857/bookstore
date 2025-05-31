package com.wj.bookstore.controller;


import com.wj.bookstore.common.vo.ResVo;
import com.wj.bookstore.common.vo.req.UserReq;
import com.wj.bookstore.user.account.service.RegisterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-14-11:29
 **/
@RestController
@RequestMapping("/public/register")
@Slf4j
@Api(tags = "注册管理")
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    @PostMapping("/phone")
    @ApiOperation(value = "手机注册")
    public ResVo<Boolean> registerByPhone(
            @ApiParam(value = "注册请求参数",required = true)
            @Valid
            @RequestBody UserReq userReq) {
        registerService.registerByPhone(userReq);
        return ResVo.ok(true);
    }

}
