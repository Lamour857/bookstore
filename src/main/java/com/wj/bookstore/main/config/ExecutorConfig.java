package com.wj.bookstore.main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-02-12-21:18
 **/
@Configuration
public class ExecutorConfig {
    @Bean
    public ScheduledExecutorService scheduledExecutorService(){
        return Executors.newScheduledThreadPool(1);
    }
}
