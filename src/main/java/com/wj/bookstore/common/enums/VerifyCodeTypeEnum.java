package com.wj.bookstore.common.enums;

import lombok.Getter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-14-15:17
 **/
@Getter
public enum VerifyCodeTypeEnum {
    LOGIN(0,"登录","login_code_","login_count_"),
    REGISTER(1,"注册","register_code_","register_count_"),
    CHANGE_PASSWORD(2,"修改密码","change_password_code_","change_password_count_");

    private final int type;
    private final String msg;
    private final String codePrefix;
    private final String countPrefix;

    VerifyCodeTypeEnum(int type, String msg,String codePrefix,String countPrefix) {
        this.type = type;
        this.msg = msg;
        this.codePrefix=codePrefix;
        this.countPrefix=countPrefix;
    }
    public static VerifyCodeTypeEnum fromCode(Integer code) {
        for (VerifyCodeTypeEnum value : VerifyCodeTypeEnum.values()) {
            if (value.getType()==code) {
                return value;
            }
        }
        return null;
    }
}
