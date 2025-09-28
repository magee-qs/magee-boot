package com.magee.quartz.utils;

import com.alibaba.fastjson.JSON;
import com.magee.quartz.domain.SysJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.quartz.*;

/**
 * 功能描述:
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Slf4j
public class QuartzUtils {

    public static JobDataMap getJobDataMap(SysJob sysJob){
        JobDataMap map = new JobDataMap();
        map.put("clazz", sysJob.getClazz());
        map.put("method", sysJob.getMethod());
        map.put("param", sysJob.getParam());
        // 缓存sysJob信息
        map.put("sysJob", JSON.toJSONString(sysJob));
       return map;
    }

    /**
     * 创建定时任务
     * */
    public static void createScheduleJob(SysJob sysJob, Scheduler scheduler){
        try{
            scheduler.scheduleJob(buildJobDetail(sysJob), buildTrigger(sysJob));
            log.info("加载任务 name: {}, class:{}, method: {}, param: {},corn:{}",sysJob.getJobName(),
                    sysJob.getClazz(), sysJob.getMethod(), sysJob.getParam(), sysJob.getCron());
        }catch (Exception ex){
            log.error("加载任务失败,name: {}, class:{}, method: {}, param: {},corn:{}, message: {}",sysJob.getJobName(),
                    sysJob.getClazz(), sysJob.getMethod(),sysJob.getParam(), sysJob.getCron(), ex.getMessage(),ex);
        }
    }

    /**
     * 启动定时任务
     * */
    public static void startJob(SysJob sysJob, Scheduler scheduler){
        log.info("======================启动任务======================");
        JobKey jobKey = getJobKey(sysJob);
        TriggerKey triggerKey = getTriggerKey(sysJob);
        try{
            // 任务不存在
            if(!scheduler.checkExists(jobKey)){
                JobDetail jobDetail = buildJobDetail(sysJob);
                Trigger trigger = buildTrigger(sysJob);

                scheduler.scheduleJob(jobDetail, trigger);
            }else{
                // 存在则恢复触发器
                if(scheduler.getTriggerState(triggerKey) == Trigger.TriggerState.PAUSED){
                    scheduler.resumeTrigger(triggerKey);
                }
            }

            /* 3. 立即执行一次（不影响 cron） */
            scheduler.triggerJob(jobKey);
        }catch (Exception ex){

        }

    }
    /**
     * 添加任务但是不创建触发器
     * */
    public static JobKey addJob(SysJob sysJob, Scheduler scheduler){
        JobKey  jobKey = getJobKey(sysJob);
        try {
            if(!scheduler.checkExists(jobKey)){
                JobDetail jobDetail = buildJobDetail(sysJob);
                scheduler.addJob(jobDetail, true);
            }
        } catch (SchedulerException e) {
           log.error("定时任务启动失败");
        }
        return jobKey;
    }



    /**
     * 执行任务一次
     * */
    public static void runJob(SysJob sysJob, Scheduler scheduler){
        try{
            log.info("======================执行一次任务======================");
            JobKey jobKey = addJob(sysJob, scheduler);
            scheduler.triggerJob(jobKey);
        }catch (Exception ex){
            log.error("执行一次任务：", sysJob.toString(),ex);
        }
    }

    /**
     * 暂停任务
     * */
    public static void pauseJob(SysJob sysJob, Scheduler scheduler){
        try{
            log.info("======================暂停任务======================");
            JobKey jobKey = addJob(sysJob, scheduler);
            scheduler.pauseJob(jobKey);
        }catch (Exception ex){
            log.error("暂停任务失败：", sysJob.toString(),ex);
        }
    }

    public static void removeJob(SysJob sysJob, Scheduler scheduler){
        try{
            log.info("======================删除任务======================");
            JobKey  jobKey = getJobKey(sysJob);
            scheduler.deleteJob(jobKey);
        }catch (Exception ex){
            log.error("删除任务失败：", sysJob.toString(),ex);
        }
    }

    public static void resumeJob(SysJob sysJob, Scheduler scheduler){
        try{
            log.info("======================恢复任务======================");
            JobKey  jobKey =  getJobKey(sysJob);
            if(!scheduler.checkExists(jobKey)){
                log.warn("任务已经删除,无法恢复");
            }

            TriggerKey triggerKey = getTriggerKey(sysJob);
            if(scheduler.getTriggerState(triggerKey) == Trigger.TriggerState.PAUSED){
                scheduler.resumeJob(jobKey);
                log.info("======================恢复任务成功======================");
            }else{
                log.info("======================任务状态不是 PAUSED，无需恢复======================");
            }
        }catch (Exception ex){
            log.error("恢复任务失败：", sysJob.toString(),ex);
        }
    }

    private static JobDetail buildJobDetail(SysJob sysJob){
        JobDataMap map = QuartzUtils.getJobDataMap(sysJob);

        return JobBuilder.newJob(ReflectJob.class)
                .withIdentity(getJobKey(sysJob))
                .usingJobData(map)
                .storeDurably()
                .build();
    }

    private static Trigger buildTrigger(SysJob sysJob){

        // 表达式调度构建器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(sysJob.getCron());
        cronScheduleBuilder = handleCronScheduleMisfirePolicy(sysJob, cronScheduleBuilder);

        return TriggerBuilder.newTrigger()
                .withIdentity(getTriggerKey(sysJob))
                .withSchedule(cronScheduleBuilder)
                .build();
    }


    private static CronScheduleBuilder handleCronScheduleMisfirePolicy(SysJob sysJob, CronScheduleBuilder cb){
        switch (sysJob.getPolicy()){
            case 1:
                return cb.withMisfireHandlingInstructionIgnoreMisfires();
            case 2:
                return cb.withMisfireHandlingInstructionFireAndProceed();
            case 3:
                return cb.withMisfireHandlingInstructionDoNothing();
            default:
                return cb;
        }
    }

    public static JobKey getJobKey(SysJob sysJob){
        return  new JobKey("task_" + sysJob.getJobId(), sysJob.getJobGroup()) ;
    }

    public static TriggerKey getTriggerKey(SysJob sysJob){
        return  new TriggerKey("task_" + sysJob.getJobId(), sysJob.getJobGroup());
    }
}
