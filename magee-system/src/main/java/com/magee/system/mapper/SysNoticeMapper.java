package com.magee.system.mapper;

import com.magee.system.domain.SysNotice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author magee
* @description  sys_notice(通告) 的数据库操作Mapper
* @Entity com.magee.system.domain.SysNotice
*/
public interface SysNoticeMapper extends BaseMapper<SysNotice> {

    /**
     * 查询通告内容
     * */
    String getContentById(@Param("noticeId")Long noticeId);

    /**
     * 写入公告类容
     * */
    void updateContentById(@Param("noticeId")Long noticeId, @Param("content") String content);
}




