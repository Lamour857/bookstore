package com.wj.bookstore.common.utils;

import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;



/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-14-11:00
 **/
@Slf4j
public class EmailUtil {
    private static volatile String from;

    public static String getFrom(){
        if(from == null){
            synchronized (EmailUtil.class){
                if(from == null){
                    from=SpringUtil.getConfig("spring.mail.from", "1722699649@qq.com");
                }
            }
        }
        return from;
    }
    public static boolean sendMail(String title, String to, String content){
        try{
            JavaMailSender javaMailSender=SpringUtil.getBean(JavaMailSender.class);
            MimeMessage mimeMailMessage=javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMailMessage,true);
            mimeMessageHelper.setFrom(getFrom());
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(title);
            // true支持html模板
            mimeMessageHelper.setText(content,true);
            Thread.currentThread().setContextClassLoader(EmailUtil.class.getClassLoader());
            javaMailSender.send(mimeMailMessage);
            return true;
        }catch (Exception e){
            log.warn("sendEmail error {}@{}",title,to,e);
            return false;
        }
    }
}
