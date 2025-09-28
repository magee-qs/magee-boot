package com.magee.generator.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 功能描述:
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Data
public class FormModel {

    @NotEmpty(message = "表名不能为空")
    private String name;

    private String comment;

    @NotEmpty(message = "功能模块不能为空")
    private String entity;

    @NotEmpty(message = "包名不能为空")
    private String packageName;

    @NotEmpty(message = "API地址不能为空")
    private String url;
}
