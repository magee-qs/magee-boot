package com.magee.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.magee.common.constant.CacheConstant;
import com.magee.system.domain.SysDepart;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
* @author magee
* @description sys_depart(部门)
*/
public interface ISysDepartService extends IService<SysDepart> {
    /**
     * 新增保存部门信息
     *
     * @param depart 部门信息
     */
    void addDepart(SysDepart depart);


    /**
     * 修改部门
     * @param  depart
     * */
    void updateDepart(SysDepart depart);


    /**
     * 缓存加载数据
     * */
    List<SysDepart> getDepartList();

    SysDepart getDepartById(Long departId);
}
