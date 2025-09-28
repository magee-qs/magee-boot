package com.magee.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.magee.framework.core.domain.SysLog;
import com.magee.system.service.ISysLogService;
import com.magee.system.mapper.SysLogMapper;
import org.springframework.stereotype.Service;

/**
* @author magee
* @description  sys_log(操作日志) Service实现
*/
@Service
public class SysLogService extends ServiceImpl<SysLogMapper, SysLog>
    implements ISysLogService {

}




