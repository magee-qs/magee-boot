package com.magee.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.magee.common.constant.CacheConstant;
import com.magee.common.exception.ServiceException;
import com.magee.common.utils.ConvertUtils;
import com.magee.common.utils.ObjectUtils;
import com.magee.common.utils.StringUtils;
import com.magee.system.domain.SysDepart;
import com.magee.system.service.ISysDepartService;
import com.magee.system.mapper.SysDepartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author magee
* @description  sys_depart(部门) Service实现
*/
@Service
public class SysDepartService extends ServiceImpl<SysDepartMapper, SysDepart>
    implements ISysDepartService {

    @Autowired
    private SysDepartMapper sysDepartMapper;
    /**
     * 新增保存部门信息
     *
     * @param depart 部门信息
     * @return 结果
     */
    @CacheEvict(value = CacheConstant.SYS_DEPART_CACHE, allEntries = true)
    public void addDepart(SysDepart depart){
        SysDepart info = getById(depart.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if(ObjectUtils.equal(0, info.getStatus())){
            throw new ServiceException("部门停用，不能新增");
        }
        depart.setAncestors(info.getAncestors() + "," + depart.getParentId() );
        save(depart);
    }

    /**
     * 修改部门
     * */
    @CacheEvict(value = CacheConstant.SYS_DEPART_CACHE, allEntries = true)
    public void updateDepart(SysDepart depart){
        SysDepart newParentDepart = getById(depart.getParentId());
        SysDepart oldDepart = getById(depart.getDepartId());

        // 存在父节点
        if(ObjectUtils.isNotEmpty(newParentDepart) && ObjectUtils.isNotEmpty(oldDepart)){
            String newAncestors = newParentDepart.getAncestors() + "," + newParentDepart.getDepartId();
            String oldAncestors = oldDepart.getAncestors();

            depart.setAncestors(newAncestors);
            // 修改子元素
            updateDepartChildren(depart.getDepartId(), newAncestors, oldAncestors);
        }
        updateById(depart);
        // 如果启用部门，则启用所有上级部门
        if(ObjectUtils.equal(1, depart.getStatus()) && StringUtils.isNotEmpty(depart.getAncestors())){
            updateParentDepartStatus(depart);
        }
    }

    /**
     * 缓存加载数据
     * */
    @Cacheable(value = CacheConstant.SYS_DEPART_CACHE, key = "'ALL'", sync = true)
    public List<SysDepart> getDepartList(){
        return list();
    }

    /**
     * 缓存加载数据
     * */
    @Cacheable(value = CacheConstant.SYS_DEPART_CACHE, key = "#departId", sync = true)
    public SysDepart getDepartById(Long departId){
        return getById(departId);
    }

    /**
     * 修改子元素的层级关系
     * */
    private void updateDepartChildren(Long departId,  String newAncestors, String oldAncestors){
        LambdaQueryWrapper<SysDepart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.apply("FIND_IN_SET({0}, {1})", departId, "ancestors");
        List<SysDepart> children = list(queryWrapper);
        for(SysDepart child :children){
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if(children.size() > 0){
            sysDepartMapper.updateDepartChildren(children);
        }
    }

    /**
     *  修改父级部门状态
     * */
    private void updateParentDepartStatus(SysDepart depart){
        String ancestors = depart.getAncestors();
        Long[] departId = ConvertUtils.toLongArray(ancestors);
        sysDepartMapper.updateDepartStatus(departId);
    }
}




