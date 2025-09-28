package com.magee.quartz.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.magee.common.utils.ExceptionUtils;
import com.magee.common.utils.StringUtils;
import com.magee.common.utils.bean.BeanUtils;
import com.magee.common.utils.reflect.ReflectUtils;
import com.magee.common.utils.spring.SpringUtils;
import com.magee.quartz.domain.SysJob;
import com.magee.quartz.domain.SysJobLog;
import com.magee.quartz.service.ISysJobLogService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;


/**
 * 功能描述: 任务工具类
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Slf4j
public class ReflectJob extends QuartzJobBean {
    /**
     * 线程本地变量
     */
    private static ThreadLocal<Date> threadLocal = new ThreadLocal<>();
    /**
     * 执行任务
     * */
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        before();
        SysJob sysJob = new SysJob();
        // 读取任务信息
        getSysJob(context, sysJob);
        try{
            JobDataMap map = context.getMergedJobDataMap();
            String clazzName = map.getString("clazz");
            String methodName = map.getString("method");
            String paramData = map.getString("param");

            ReflectUtils.invokeMethod(clazzName, methodName, paramData);

            after(context, sysJob, null);

        }catch (Exception ex){
            log.error("================任务执行异常===================", ex);
            after(context, sysJob, ex);
        }

    }

    private  void getSysJob(JobExecutionContext context, SysJob sysJob){
        String str =  context.getMergedJobDataMap().getString("sysJob");
        if(StringUtils.isNotEmpty(str)){
            SysJob job =  JSON.parseObject(str, SysJob.class);
            BeanUtils.copyProperties(job, sysJob);
        }
    }
    public void before(){
        threadLocal.set(new Date());
    }

    /**
     * 执行完成后
     * */
    public void after(JobExecutionContext context, SysJob sysJob, Exception ex){
        Date startTime = threadLocal.get();
        threadLocal.remove();
        SysJobLog sysJobLog = new SysJobLog();
        sysJobLog.setJobId(sysJob.getJobId());
        sysJobLog.setJobName(sysJob.getJobName());
        sysJobLog.setJobGroup(sysJob.getJobGroup());
        sysJobLog.setClazz(sysJob.getClazz());
        sysJobLog.setMethod(sysJob.getMethod());
        sysJobLog.setParam(sysJob.getParam());
        sysJobLog.setStart(startTime);

        // 计算耗时
        Date end = new Date();
        sysJobLog.setEnd(end);
        long sum = end.getTime() - startTime.getTime();
        sysJobLog.setTotal(sum);

        if(ex!= null){
            sysJob.setStatus(0);
            String message =StringUtils.substring(ExceptionUtils.getExceptionMessage(ex), 0, 2000);
            sysJobLog.setErrorInfo(message);
        }else{
            sysJobLog.setStatus(1);
        }

        // 写入日志
        SpringUtils.getBean(ISysJobLogService.class).save(sysJobLog);
    }


}
