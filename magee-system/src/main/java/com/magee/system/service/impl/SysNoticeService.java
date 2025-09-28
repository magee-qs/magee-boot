package com.magee.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.magee.system.domain.SysNotice;
import com.magee.system.service.ISysNoticeService;
import com.magee.system.mapper.SysNoticeMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author magee
* @description  sys_notice(通告) 的数据库操作Service实现
*/
@Service
public class SysNoticeService extends ServiceImpl<SysNoticeMapper, SysNotice>
    implements ISysNoticeService {

    @Autowired
    private SysNoticeMapper noticeMapper;

    /**
     * 查询通告内容
     * */
    public String getContentById(Long noticeId){
        return noticeMapper.getContentById(noticeId);
    }


    /**
     * 添加公告
     * */
    @Transactional
    public void addNotice(SysNotice notice){
        save(notice);
        // 写入内容
        noticeMapper.updateContentById(notice.getNoticeId(), notice.getContent());
    }

    /**
     * 修改公告
     * */
    @Transactional
    public void editNotice(SysNotice notice){
        updateById(notice);
        // 写入内容
        noticeMapper.updateContentById(notice.getNoticeId(), notice.getContent());
    }

}




