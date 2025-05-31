package com.wj.bookstore.main.service;


/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-02-10-17:49
 **/
public interface DataSyncService {
    void syncCategoryToCache();

    void clearCache();
}
