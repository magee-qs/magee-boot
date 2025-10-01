package com.magee.system.mapper;

import com.magee.system.domain.SysDepart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author magee
* @description sys_depart(部门) 的数据库操作Mapper
* @Entity com.magee.system.domain.SysDepart
*/
public interface SysDepartMapper extends BaseMapper<SysDepart> {
    /**
     * 修改子元素关系
     *
     * @param children 子元素
     * @return 结果
     */
    int updateDepartChildren(@Param("children") List<SysDepart> children);

    /**
     * 修改部门状态
     * @param departIds 部门id
     * */
    int updateDepartStatus(@Param("ids") Long[] departIds);


    /**
     * 遍历获取当前部门和所有部门的上级
     * */
    List<Long> getDepartAncestors(List<Long> departIds);


    /**
     * 遍历获取当前部门和所有下级部门
     * */
    List<Long> getDepartChildren(@Param("departIds") List<Long> departIds);
}




