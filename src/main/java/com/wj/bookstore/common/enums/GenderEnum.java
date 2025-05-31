package com.wj.bookstore.common.enums;

import lombok.Getter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-08-18:00
 **/
@Getter
public enum GenderEnum {
    MALE((short) 1,"男"),
    FEMALE((short) 2,"女"),
    UNKNOWN((short) 3,"保密")
    ;
    private final Short code;
    private final String name;
    GenderEnum(short code, String name) {
        this.code = code;
        this.name = name;
    }

    public static short getCode(String name) {
        for (GenderEnum genderEnum : GenderEnum.values()) {
            if (genderEnum.getName().equals(name)) {
                return genderEnum.getCode();
            }
        }
        return 3;
    }

    public static String fromCode(Short gender) {
        if(gender==null){
            return "保密";
        }
        for (GenderEnum genderEnum : GenderEnum.values()) {
            if (genderEnum.getCode().equals(gender)) {
                return genderEnum.getName();
            }
        }
        return "保密";
    }
}
