package com.wj.bookstore.main.boot;

import com.wj.bookstore.main.service.DataSyncService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-02-10-15:34
 **/
@Component
@Slf4j
public class CacheInitializer implements ApplicationRunner {
    @Autowired
    private DataSyncService dataSyncService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 清除缓存旧数据
        //dataSyncService.clearCache();
        // 同步分类到缓存中
        //dataSyncService.syncCategoryToCache();
    }
}
