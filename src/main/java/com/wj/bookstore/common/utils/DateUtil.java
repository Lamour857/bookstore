package com.wj.bookstore.common.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-02-15:18
 **/
public class DateUtil {
    public static String convert(Date date){
        if(date==null){
            return null;
        }

        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault()) // 使用系统默认时区
                .toLocalDateTime();

        // 定义目标格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 格式化 LocalDateTime 为字符串
       return localDateTime.format(formatter);

    }
}
