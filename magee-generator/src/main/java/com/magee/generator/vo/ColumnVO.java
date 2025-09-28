package com.magee.generator.vo;

import lombok.Data;

/**
 * 功能描述:
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Data
public class ColumnVO {
    private String name;
    private Integer nullable;
    private String comment;
    private String columnKey;
    private String dataType;
    private String columnType;
    private Integer maxLength;


    /** 字段类型 */
    private String fieldType;

    /** 字段名 驼峰 */
    private String fieldName;

    private Boolean isKey;

    private Integer max;

    private Integer min;

    private Boolean ignore;


}
