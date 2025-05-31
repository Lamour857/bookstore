package com.wj.bookstore.common.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


import java.io.Serial;
import java.io.Serializable;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-14-13:03
 **/
@Data
@Accessors(chain = true)
@ToString
@ApiModel(value = "RegisterRequestVO", description = "注册请求对象")
public class UserReq implements Serializable {
    @Serial
    private static final long serialVersionUID= 2139742660700910738L;

    @ApiModelProperty(value = "密码", required = true)
    @Pattern(regexp = "^[A-Za-z0-9]{6,12}$", message = "密码仅字母+数字，且长度为6-12位")
    private String password;
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @Pattern(regexp = "^1[3-9]\\d{9}$" ,message = "手机号格式错误")
    @ApiModelProperty(value = "手机号", required = true)
    private String phone;
    @Pattern(regexp = "^\\d{6}$", message = "验证码格式错误")
    @ApiModelProperty(value = "验证码", required = true)
    private String verifyCode;
}
