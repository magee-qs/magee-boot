package com.magee.system.service;

import com.magee.system.domain.SysNotice;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
* @author magee
* @description  sys_notice(通告) 的数据库操作Service
*/
public interface ISysNoticeService extends IService<SysNotice> {
    /**
     * 查询通告内容
     * */
    String getContentById(Long noticeId);


    /**
     * 添加公告
     * */
    void addNotice(SysNotice notice);

    /**
     * 修改公告
     * */
    void editNotice(SysNotice notice);
}
