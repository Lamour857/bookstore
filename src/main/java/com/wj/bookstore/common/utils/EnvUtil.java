package com.wj.bookstore.common.utils;

import org.springframework.util.Assert;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-02-14-16:48
 **/
public class EnvUtil {
    private static volatile EnvEnum env;
    public enum EnvEnum {
        DEV("dev",false),
        TEST("test",false),
        PROD("prod",true),
        PRE("pre",false);
        private String env;
        private Boolean prod;
        EnvEnum(String env,Boolean prod){
            this.env = env;
            this.prod = prod;
        }
        public static EnvEnum nameOf(String name) {
            for (EnvEnum env : values()) {
                if (env.env.equalsIgnoreCase(name)) {
                    return env;
                }
            }
            return null;
        }
    }
    public static Boolean isProd() {
        return getEnv().prod;
    }
    public static EnvEnum getEnv() {
        // 单例懒加载
        if (env == null) {
            synchronized (EnvUtil.class) {
                if (env == null) {
                    env=EnvEnum.nameOf(SpringUtil.getConfig("env.name"));
                }
            }
        }
        Assert.isTrue(env != null, "env.name环境配置必须存在!");
        return env;
    }
}
