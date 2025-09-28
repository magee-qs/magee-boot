package com.magee.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.magee.common.constant.CacheConstant;
import com.magee.common.utils.StringUtils;
import com.magee.framework.core.domain.SysLog;
import com.magee.framework.core.query.SimpleQuery;
import com.magee.system.domain.*;
import com.magee.system.query.UserParam;
import com.magee.system.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 功能描述: 通用方法实现
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Slf4j
@Service
public class BaseCommonAPI implements IBaseCommonAPI {

    @Autowired
    private ISysDepartService departService;

    @Autowired
    private ISysLogService logService;

    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Override
    public String getDepartName(Long departId) {
        SysDepart depart = departService.getDepartById(departId);
        return depart == null ? null : depart.getName();
    }

    @Async
    @Override
    public void addLog(SysLog sysLog) {
        log.info("异步线程={}",Thread.currentThread().getName());
        logService.save(sysLog);
    }


    /**
     * 根据字典类型获取数据
     *
     * @return*/
    public  List<SysDictData>  getDictData(String dictType){
        if(StringUtils.isEmpty(dictType)){
            return  null;
        }
        return  dictDataService.getDict(dictType);
    }

    /**
     * 根据字典类型获取数据
     *
     * @return*/
    public List<SysDictData> getAllDictData(){
         return   dictDataService.getAllDict();
    }

    /**
     * 获取字典分类
     * */
    public List<SysDictType> getDictType(){
        return dictDataService.getDictType();
    }


    /**
     * 用户数据查询
     * */
    public IPage<SysUser> getUserList(UserParam userParam){
        QueryWrapper<SysUser> queryWrapper = SimpleQuery.toQueryWrapper(userParam);
        // 状态有效
        queryWrapper.eq("status", 1);
        IPage<SysUser> page = SimpleQuery.toPage();

        page = userService.page(page,queryWrapper);
        return page;
    }

    /**
     * 获取角色分页数据
     * */
    public IPage<SysRole> getRoleList(SysRole role){
        QueryWrapper<SysRole> queryWrapper = SimpleQuery.toQueryWrapper(role);
        // 状态有效
        queryWrapper.eq("status", 1);
        IPage<SysRole> page = SimpleQuery.toPage();

        page = roleService.page(page,queryWrapper);
        return page;
    }

    /**
     * 获取字典数据
     * */
    public String getDictText(String dictValue, String dictType){
        if(StringUtils.isEmpty(dictValue)){
            return StringUtils.EMPTY;
        }
        List<SysDictData> dictData = dictDataService.getDict(dictType);
        for(SysDictData dict : dictData){
            if(StringUtils.equals(dict.getDictValue(), dictValue)){
                return dict.getDictLabel();
            }
        }
        return dictValue;
    }


    /**
     * 缓存加载数据
     * */
    public List<SysDepart> getDepartList(){
        return departService.getDepartList();
    }
}
