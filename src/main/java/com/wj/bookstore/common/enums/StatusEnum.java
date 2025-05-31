package com.wj.bookstore.common.enums;

import lombok.Getter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-14-11:37
 **/
@Getter
public enum StatusEnum {
    SUCCESS(0,"OK"),

    // 全局传参异常
    ILLEGAL_ARGUMENTS(100_400_001,"参数异常"),
    ILLEGAL_ARGUMENTS_MIXED(100_400_002,"参数异常:%s"),

    //  全局权限异常
    FORBID_ERROR(100_403_001,"无权限"),
    FORBID_ERROR_MIXED(100_403_002,"无权限:%s"),
    FORBID_NOT_LOGIN(100_403_003,"未登录"),

    // 数据不存在
    RECORDS_NOT_EXISTS(100_404_001,"记录不存在:%s"),
    RECORDS_EXISTS(100_404_002,"记录已存在:%s"),

    // 系统异常
    SEND_MESSAGE_FAIL(100_500_002,"消息发送失败" ),
    UNEXPECT_ERROR(100_500_001,"非预期异常: %s"),

    //用户相关异常
    USER_UNABLE(400_403_001,"用户被禁用"),
    LOGIN_FAILED_MIXED(400_403_001,"登陆失败: %s"),
    USER_NOT_EXISTS(400_404_001,"用户不存在"),
    USER_EXISTS(400_404_002,"用户已存在: %s"),
    USER_NAME_REPEAT(400_404_003,"用户名重复"),
    VERIFY_CODE_SEND_FREQUENT(400_405_001,"验证码发送频繁: %s"),
    VERIFY_CODE_NOT_EXIST(400_405_002, "验证码不存在"),
    VERIFY_CODE_TYPE_ERROR(400_405_003,"验证码类型传参错误" ),
    USER_PWD_ERROR(400_500_002,"用户名或密码错误"),
    UNSUPPORTED_AUTHENTICATION_REQUEST(400_500_001,"不支持的认证请求");



    private int code;
    private String msg;

    StatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
