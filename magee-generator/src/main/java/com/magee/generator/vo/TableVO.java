package com.magee.generator.vo;

import lombok.Data;

import java.util.List;

/**
 * 功能描述:
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Data
public class TableVO {
    /**
     * 表名
     * */
    private String tableName;
    /**
     * 表注释
     * */
    private String tableComment;

}
