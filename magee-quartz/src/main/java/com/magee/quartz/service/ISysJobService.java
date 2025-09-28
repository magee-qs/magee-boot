package com.magee.quartz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.magee.quartz.domain.SysJob;

/**
 * @author magee
 * @description 定时任务服务接口
 * @version: V1.0
 */
public interface ISysJobService extends IService<SysJob> {

    /**
     * 修改运行状态
     * */
    void changeRunningState(Long jobId, Integer state);

}
