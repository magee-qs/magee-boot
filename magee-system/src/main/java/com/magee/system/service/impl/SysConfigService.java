package com.magee.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.magee.system.domain.SysConfig;
import com.magee.system.service.ISysConfigService;
import com.magee.system.mapper.SysConfigMapper;
import org.springframework.stereotype.Service;

/**
* @author magee
* @description  sys_config(系统配置) 的数据库操作Service实现
*/
@Service
public class SysConfigService extends ServiceImpl<SysConfigMapper, SysConfig>
    implements ISysConfigService {

}




