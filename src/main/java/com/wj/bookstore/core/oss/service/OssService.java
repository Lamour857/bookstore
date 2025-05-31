package com.wj.bookstore.core.oss.service;


import java.io.IOException;
import java.util.Map;


/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-02-28-22:00
 **/

public interface OssService {

    String getImageUrl(String prefix, String fileName) ;

    String getSignature(String prefix, String fileName);


    Map<String,String> getUrlInfo(String signedUrl);

    void deleteByName(String id);
}
