package com.point.clddock.component;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ScheduledTask {

    @Scheduled(fixedRate = 60*60*1000)
    public void execTimerTask(){
        System.out.println("执行定时任务" + LocalDateTime.now());
    }
}
