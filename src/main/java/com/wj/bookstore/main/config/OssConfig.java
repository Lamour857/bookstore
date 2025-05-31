package com.wj.bookstore.main.config;

import com.aliyun.credentials.provider.DefaultCredentialsProvider;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-02-28-21:50
 **/
@Configuration
public class OssConfig {
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret}")
    private String secretAccessKey;

    @Value("${aliyun.oss.region}")
    private String region;

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Bean
    public OSS ossClient() throws ClientException {
        return new OSSClientBuilder().build(
               endpoint,accessKeyId,secretAccessKey
        );
    }
}
