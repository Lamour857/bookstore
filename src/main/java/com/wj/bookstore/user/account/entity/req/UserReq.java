package com.wj.bookstore.user.account.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-08-17:50
 **/
@Data
@ApiModel(value = "UserReq",description = "用户请求对象")
public class UserReq {
    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "确认密码")
    private String rePassword;

    @ApiModelProperty(value="个人简介")
    private String profile;

    @ApiModelProperty(value = "第三方账号id")
    private String thirdAccountId;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "地址id")
    private Short locationId;


    @ApiModelProperty(value = "生日")
    private Date birthday;
}
