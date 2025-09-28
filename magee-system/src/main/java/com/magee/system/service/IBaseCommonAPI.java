package com.magee.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.magee.framework.core.service.CommonAPI;
import com.magee.system.domain.*;
import com.magee.system.query.UserParam;

import java.util.List;

/**
 * 功能描述: 通用方法实现
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public interface IBaseCommonAPI extends CommonAPI {

    /**
     * 根据字典类型获取数据
     *
     * @return*/
     List<SysDictData> getAllDictData();
    /**
     * 根据字典类型获取数据
     *
     * @return*/
     List<SysDictData> getDictData(String dictType);


     /**
      * 获取字典分类
      * */
     List<SysDictType> getDictType();

     /**
      * 用户数据查询
      * */
     IPage<SysUser> getUserList(UserParam userParam);

      /**
       * 获取角色分页数据
       * */
     IPage<SysRole> getRoleList(SysRole role);

    /**
     * 缓存加载数据
     * */
    List<SysDepart> getDepartList();
}
