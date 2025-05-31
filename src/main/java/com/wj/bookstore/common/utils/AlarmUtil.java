package com.wj.bookstore.common.utils;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-14-10:35
 **/
@Slf4j
public class AlarmUtil extends AppenderBase<ILoggingEvent> {
    private static final long INTERVAL=10*1000*60;
    private long lastAlarmTime=0;
    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        if(canAlarm()){
            log.info("alarmUtil");
            EmailUtil.sendMail(iLoggingEvent.getLoggerName(),
                    SpringUtil.getConfig("alarm.user","1722699649@qq.com"),
                    iLoggingEvent.getFormattedMessage());
        }
    }
    private boolean canAlarm(){
        long now=System.currentTimeMillis();
        if(now-lastAlarmTime>INTERVAL){
            lastAlarmTime=now;
            return true;
        }else{
            return false;
        }
    }
}
