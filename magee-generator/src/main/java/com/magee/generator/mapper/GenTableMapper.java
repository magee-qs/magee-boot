package com.magee.generator.mapper;

import com.magee.generator.vo.ColumnVO;
import com.magee.generator.vo.TableVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能描述:
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public interface GenTableMapper {

    /**
     * 查询表名
     * */
    List<TableVO> selectTable();

    /**
     * 查询列名
     * */
    List<ColumnVO> selectColumn(@Param("tableName")String tableName);
}
