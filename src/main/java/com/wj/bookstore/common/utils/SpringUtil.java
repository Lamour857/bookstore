package com.wj.bookstore.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-14-10:57
 **/
@Component
public class SpringUtil implements ApplicationContextAware, EnvironmentAware {
    private static ApplicationContext context;
    private static Environment environment;

    private static Binder binder;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.context = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        SpringUtil.environment=environment;
        binder=Binder.get(environment);
    }
    public static ApplicationContext getContext() {return context;}
    // 获取Bean
    public static <T> T getBean(Class<T> bean) {
        return context.getBean(bean);
    }
    public static <T> T getBeanOrrNull(Class<T> bean){
        try{
            return context.getBean(bean);
        }catch(Exception e){
            return null;
        }
    }
    public static Object getBean(String beanName){
        return context.getBean(beanName);
    }

    public static Object getBeanOrNull(String beanName){
        try{
            return context.getBean(beanName);
        }catch (Exception e) {
            return null;
        }
    }
    // 获取配置
    public static String getConfig(String key){
        return environment.getProperty(key);
    }
    public static String getConfigOrElse(String mainKey,String slaveKey){
        String ans=environment.getProperty(mainKey);
        if(ans==null){
            return environment.getProperty(slaveKey);
        }
        return ans;
    }
    public static String getConfig(String key,String val){
        return environment.getProperty(key,val);
    }
    // 发布事件消息
    public static void publishEvent(ApplicationEvent event){
        context.publishEvent(event);
    }
    // 配置绑定类
    public static Binder getBinder(){return binder;}

}