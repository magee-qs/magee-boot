package com.magee.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.magee.system.domain.SysPost;
import com.magee.system.service.ISysPostService;
import com.magee.system.mapper.SysPostMapper;
import org.springframework.stereotype.Service;

/**
* @author magee
* @description  sys_post(岗位) 的数据库操作Service实现
*/
@Service
public class SysPostService extends ServiceImpl<SysPostMapper, SysPost>
    implements ISysPostService {

}




