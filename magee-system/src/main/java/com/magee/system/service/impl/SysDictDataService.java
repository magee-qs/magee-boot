package com.magee.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.magee.common.constant.CacheConstant;
import com.magee.system.domain.SysDictData;
import com.magee.system.domain.SysDictType;
import com.magee.system.mapper.SysDictTypeMapper;
import com.magee.system.service.ISysDictDataService;
import com.magee.system.mapper.SysDictDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author magee
* @description  sys_dict_data(字典数据表) 的数据库操作Service实现
*/
@Service
public class SysDictDataService extends ServiceImpl<SysDictDataMapper, SysDictData>
    implements ISysDictDataService {

    @Autowired
    private SysDictTypeMapper dictTypeMapper;

    /**
     * 查询数据字典
     * @return 数据集
     * */
    @Cacheable(value = CacheConstant.SYS_DICT_CACHE, key = "'ALL'" , sync = true)
    public List<SysDictData> getAllDict(){
        List<SysDictData> list = list();
        return list;
    }

    /**
     * 查询数据字典
     * @return 数据集
     * */
    /**
     * 查询数据字典
     * @return 数据集
     * */
    @Cacheable(value = CacheConstant.SYS_DICT_CACHE, key = "#dictType" , sync = true)
    public List<SysDictData> getDict(String dictType){
         LambdaQueryWrapper<SysDictData> queryWrapper = new LambdaQueryWrapper<>();
         queryWrapper.eq(SysDictData::getDictType, dictType);
         return list(queryWrapper);
    }

    /**
     * 删除字典
     * @param dictId 主键
     * */
    @CacheEvict(value = CacheConstant.SYS_DICT_CACHE, allEntries = true)
    public void removeDict(Long dictId){
         removeById(dictId);
    }

    /**
     * 添加字典数据
     * */
    @CacheEvict(value = CacheConstant.SYS_DICT_CACHE, allEntries = true)
    public void addDict(SysDictData dictData){
        save(dictData);
    }
    /**
     * 修改字典数据
     * */
    @CacheEvict(value = CacheConstant.SYS_DICT_CACHE, allEntries = true)
    public void updateDict(SysDictData dictData){
        updateById(dictData);
    }

    /**
     * 修改字典数据状态
     * */
    @CacheEvict(value = CacheConstant.SYS_DICT_CACHE, allEntries = true)
    public void changeStatus(Long dictCode){
        LambdaUpdateWrapper<SysDictData> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysDictData:: getDictCode, dictCode)
                .setSql("status = 1 - status");
        update(updateWrapper);
    }


    /**
     * 获取字典分类
     * */
    @Cacheable(value = CacheConstant.SYS_DICT_TYPE_CACHE, key = "'ALL'", sync = true)
    public List<SysDictType> getDictType(){
        return dictTypeMapper.selectList(null);
    }
}




