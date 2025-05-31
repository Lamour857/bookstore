package com.wj.bookstore.main.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-17-17:36
 **/
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = {
        "com.wj.bookstore.user.account.repository.mapper",
        "com.wj.bookstore.product.collection.repository.mapper",
        "com.wj.bookstore.user.merchant.repository.mapper",
        "com.wj.bookstore.shopping.cart.repository.mapper",
        "com.wj.bookstore.shopping.order.repository.mapper",
        "com.wj.bookstore.product.category.repository.mapper",
        "com.wj.bookstore.product.book.repository.mapper",
        "com.wj.bookstore.delivery.template.repository.mapper",
        "com.wj.bookstore.delivery.shipping.repository.mapper",
})
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
