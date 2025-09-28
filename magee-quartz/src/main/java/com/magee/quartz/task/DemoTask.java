package com.magee.quartz.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 功能描述:
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Component
@Slf4j
public class DemoTask {
    public void run(){
        log.info("执行无参数任务");
    }

    public void runParam(String param){
        log.info("执行一个参数任务:{}", param);
    }

    public void runMultipleParam(String s, Boolean b, Double d, Long l, Integer i){
        log.info("执行一个多参数任务,字符串:{},布尔:{},浮点:{},长整型:{},整数:{}", s,b, d, l, i);
    }
}
