package com.magee.generator.service;

import com.magee.generator.mapper.GenTableMapper;
import com.magee.generator.vo.ColumnVO;
import com.magee.generator.vo.TableVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 功能描述:
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Service
public class GenTableService implements IGenTableService{

    @Autowired
    private GenTableMapper tableMapper;

    public List<TableVO> selectTable(){
        return tableMapper.selectTable();
    }


    /**
     * 查询列名
     * */
    public List<ColumnVO> selectColumn(@Param("tableName")String tableName){
        return tableMapper.selectColumn(tableName);
    }
}
