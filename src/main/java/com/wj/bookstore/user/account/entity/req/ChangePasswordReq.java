package com.wj.bookstore.user.account.entity.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-22-23:12
 **/
@Data
@ApiModel("修改密码请求")
public class ChangePasswordReq {
    @ApiModelProperty(value = "新密码")
    private String password;
    @ApiModelProperty(value = "验证码")
    private String verifyCode;
    @ApiModelProperty(value = "确认密码")
    private String rePassword;
}
