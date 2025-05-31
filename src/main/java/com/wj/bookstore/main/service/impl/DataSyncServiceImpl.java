package com.wj.bookstore.main.service.impl;

import com.wj.bookstore.common.cache.CommonCache;
import com.wj.bookstore.common.enums.YesOrNoEnum;
import com.wj.bookstore.delivery.template.cache.AreaCache;
import com.wj.bookstore.delivery.template.repository.dao.AreaDao;
import com.wj.bookstore.main.service.DataSyncService;
import com.wj.bookstore.product.category.cache.CategoryCache;
import com.wj.bookstore.product.category.repository.dao.CategoryDao;
import com.wj.bookstore.product.category.entity.CategoryDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-02-10-17:52
 **/
@Service
@Slf4j
public class DataSyncServiceImpl implements DataSyncService {
    @Autowired
    private CategoryCache categoryCache;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private CommonCache commonCache;
    @Autowired
    private AreaDao areaDao;
    @Autowired
    private AreaCache areaCache;
    @Override
    public void syncCategoryToCache() {
        log.info("开始缓存分类");
        List<CategoryDO> categoryDOList = categoryDao.list(null, YesOrNoEnum.NO);
        //categoryDOList.add(new CategoryDO(0L,"",0,""));
        buildCategoryTreeInCache(categoryDOList);
        categoryCache.cacheCategoryDOList(categoryDOList);
        log.info("缓存分类完成");
    }


    @Override
    public void clearCache() {
        // 删除所有缓存
        log.info("开始清除所有缓存");
        commonCache.deleteWithPrefix("");
        log.info("清除所有缓存完成");
    }

    private void buildCategoryTreeInCache(List<CategoryDO> list){
        for(CategoryDO categoryDO:list){
            // 存储分类元数据
            categoryCache.cacheMetaCategory(categoryDO);
            // 将自身添加进子分类集合
            categoryCache.cacheCategoryIntoSet(categoryDO.getPath(),categoryDO);
            for(CategoryDO it:list){
                // 遍历列表,将categoryDO的子分类添加进子分类集合
                if(it.getParent()==categoryDO.getId()){
                    categoryCache.cacheCategoryIntoSet(categoryDO.getPath(),it);
                }
            }
        }
    }
}
