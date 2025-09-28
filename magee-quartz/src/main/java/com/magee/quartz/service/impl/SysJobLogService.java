package com.magee.quartz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.magee.quartz.domain.SysJobLog;
import com.magee.quartz.mapper.SysJobLogMapper;
import com.magee.quartz.service.ISysJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * @author magee
 * @description  sys_job_log(定时任务日志) 的数据库操作Service实现
 * @version: v1.0
 */
@Service
public class SysJobLogService extends ServiceImpl<SysJobLogMapper,SysJobLog>
        implements ISysJobLogService {
}