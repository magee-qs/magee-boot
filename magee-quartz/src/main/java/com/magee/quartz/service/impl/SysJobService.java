package com.magee.quartz.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.magee.quartz.domain.SysJob;
import com.magee.quartz.mapper.SysJobMapper;
import com.magee.quartz.service.ISysJobService;
import com.magee.quartz.utils.QuartzUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;


/**
 * @author magee
 * @description  sys_job(定时任务) 的数据库操作Service实现
 * @version: v1.0
 */
@Service
@Slf4j
public class SysJobService extends ServiceImpl<SysJobMapper,SysJob>
        implements ISysJobService {

    @Autowired
    private  Scheduler scheduler;

    @PostConstruct
    public void init(){
        try{
            log.info("======定时任务组件初始化======");
            scheduler.clear();
            List<SysJob> list = list();
            for(SysJob sysJob: list){
                if(sysJob.getStatus().equals(0)){
                    continue;
                }
                // 启动定时任务
                QuartzUtils.createScheduleJob(sysJob, scheduler);

                // 修改运行状态
                changeRunningState(sysJob.getJobId(), 1);

                log.info("加载定时任务{}",sysJob.getJobName());
            }
            log.info("======定时任务组件初始化完成======");
        }catch (Exception ex){
            log.error("初始化定时任务失败:{}", ex.getMessage());
        }


    }

    /**
     * 修改运行状态
     * */
    public void changeRunningState(Long jobId, Integer state){
        log.info("修改任务运行状态, id: {}, state: {}", jobId, state);
        LambdaUpdateWrapper<SysJob> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysJob::getJobId, jobId)
                .set(SysJob::getRunning, state);

        // 设置运行状态
        update(updateWrapper);
    }




}