package com.wj.bookstore.core.oss.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.wj.bookstore.common.enums.StatusEnum;
import com.wj.bookstore.common.utils.ExceptionUtil;
import com.wj.bookstore.core.oss.service.OssService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-03-01-14:28
 **/
@Slf4j
@Service
public class OssServiceImpl implements OssService {
    @Value("${oss.book.bucket.name}")
    private  String BOOKSTORE_BOOK_IMAGE_BUCKET;

    @Value("${oss.book.bucket.region}")
    private String BOOKSTORE_BOOK_IMAGE_REGION;

    @Autowired
    private OSS ossClient;
    @Override
    public String getImageUrl(String prefix, String fileName)  {
        if(StringUtils.isBlank(fileName)||StringUtils.isBlank(prefix)){
            return null;
        }
        return "https://"+BOOKSTORE_BOOK_IMAGE_BUCKET+".oss-"+BOOKSTORE_BOOK_IMAGE_REGION+".aliyuncs.com/"+prefix+"_"+fileName;
    }
    public String getSignature(String prefix ,String fileName) {
        fileName= UUID.randomUUID() +"_"+fileName;
        if(StringUtils.isNotBlank(prefix)){
            fileName=prefix+"_"+fileName;
        }
        Date expiration = new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(BOOKSTORE_BOOK_IMAGE_BUCKET, fileName);
        request.setMethod(HttpMethod.PUT);
        String contentType = getContentTypeFromFileName(fileName);
        request.setContentType(contentType);
        request.setExpiration(expiration);

        try{
            URL signedUrl = ossClient.generatePresignedUrl(request);
            log.info("预签名URL {}",signedUrl);

            return signedUrl.toString();
        }catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
       return null;
    }

    private String getContentTypeFromFileName(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            // 可以添加更多类型
            default -> "image/*"; // 或者返回 null 表示不设置
        };
    }
    @Override
    public Map<String,String> getUrlInfo(String signedUrl){
        if(!signedUrl.startsWith("https://"+BOOKSTORE_BOOK_IMAGE_BUCKET+".oss-"+BOOKSTORE_BOOK_IMAGE_REGION+".aliyuncs.com/")){
            log.warn("签名URL格式不正确");
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"签名URL格式不正确");
        }
        Map<String,String> result=new HashMap<>();
        // "https://bookstore-book-image.oss-cn-nanjing.aliyuncs.com/11_image_f0954be0-0715-4e21-9d59-68655e9d426e_b_ee0231830f7e28869c865ac937db7119.jpg?Expires=1740896401&OSSAccessKeyId=LTAI5tF4NpaWayCLfyMDDG7g&Signature=ZP3EG4BWCltryn%2FJePi74CEaoJg%3D";
        int index=signedUrl.indexOf("?");
        String url= signedUrl.substring(0,index); //https://bookstore-book-image.oss-cn-nanjing.aliyuncs.com/11_image_filename.jpg
        int lastSharpIndex=url.lastIndexOf("/");
        String fileName=url.substring(lastSharpIndex+1);// 11_image_f0954be0-0715-4e21-9d59-68655e9d426e_b_ee0231830f7e28869c865ac937db7119.jpg
        int first_index=fileName.indexOf("_")+1;
        String type=fileName.substring(first_index,first_index+5);
        String id=fileName.substring(0,fileName.indexOf("_")); // 11
        result.put("fileName",fileName);
        result.put("type",type);
        result.put("id",id);
        result.put("url",url);
        return result;
    }

    @Override
    public void deleteByName(String fileName) {
        if(StringUtils.isBlank(fileName)){
            return;
        }
        try{
            ossClient.deleteObject(BOOKSTORE_BOOK_IMAGE_BUCKET,fileName);
        }catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
