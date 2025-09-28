package com.magee.system.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.magee.common.constant.CacheConstant;
import com.magee.system.domain.SysDictData;
import com.baomidou.mybatisplus.extension.service.IService;
import com.magee.system.domain.SysDictType;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
* @author magee
* @description  sys_dict_data(字典数据表) 的数据库操作Service
*/
public interface ISysDictDataService extends IService<SysDictData> {

    /**
     * 查询数据字典
     * @return 数据集
     * */
    List<SysDictData> getAllDict();

    /**
     * 查询数据字典
     * @return 数据集
     * */
    List<SysDictData> getDict(String dictType);


    /**
     * 删除字典
     * @param dictId 主键
     * */
    void removeDict(Long dictId);

    /**
     * 获取字典分类
     * */
    List<SysDictType> getDictType();


    /**
     * 添加字典数据
     * */
    void addDict(SysDictData dictData) ;
    /**
     * 修改字典数据
     * */
    void updateDict(SysDictData dictData);

    /**
     * 修改字典数据状态
     * */
    void changeStatus(Long dictCode);

}
