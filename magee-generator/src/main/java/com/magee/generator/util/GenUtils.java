package com.magee.generator.util;

import com.magee.common.utils.*;
import com.magee.generator.config.GenConfig;
import com.magee.generator.vo.ColumnVO;
import com.magee.generator.vo.FormModel;
import lombok.extern.slf4j.Slf4j;


import java.util.*;

/**
 * 功能描述: 代码生成工具类
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Slf4j
public class GenUtils {
    private static Map<String,String> fieldMapper = new LinkedHashMap<>();
    private static List<String> baseColumns = Arrays.asList("create_by","create_time","update_by","update_time");
    static  {
        fieldMapper.put("bigint","Long");
        fieldMapper.put("varchar","String");
        fieldMapper.put("int","Integer");
        fieldMapper.put("char","Boolean");
        fieldMapper.put("datetime","Date");
        fieldMapper.put("date","Date");
        fieldMapper.put("longblob","Byte[]");
    }

    /***
     * 数据库字段类型到实体对象类型映射
     * 无映射的默认转换为String类型
     * */
    public static String getFieldType(String dataType){
        String field = fieldMapper.get(dataType);
        return ConvertUtils.toStr(field,"String");
    }

    /**
     * 生成代码
     * */
    public static Map<String,String> generator(FormModel formModel, List<ColumnVO> columns){
        Map<String,Object> objectMap = new LinkedHashMap<>();

        Map<String,String> result = new LinkedHashMap<>();

        //初始化列
        columns = initColumns(columns);

        // 表名转驼峰
        String entityName = toEntityName(formModel.getEntity());
        String className =  StringUtils.toCamelCase(formModel.getName());
        String keyField =  StringUtils.upperFirst(getKeyField(columns, className));
        String keyParam = StringUtils.lowerFirst(keyField);


        objectMap.put("entityName",  entityName);
        objectMap.put("className",  className);
        objectMap.put("keyField", keyField);
        objectMap.put("keyParam", keyParam);
        objectMap.put("tableName",formModel.getName());
        objectMap.put("comment", ConvertUtils.toStr(formModel.getComment(),"功能描述"));
        objectMap.put("packageName", ConvertUtils.toStr(formModel.getPackageName(),"com.magee.system"));


        objectMap.put("author", GenConfig.getAuthor());
        objectMap.put("columns", columns);
        objectMap.put("url", formModel.getUrl());
        objectMap.put("perm", formModel.getUrl().replaceAll("/",":").substring(1));

        // 处理entity
        String entity = FreeMarkerUtils.process(objectMap,GenUtils.class.getClassLoader()  ,GenConfig.getTemplatePath(), "entity.ftl");
        result.put("entity", entity);

        // 处理xml
        String xml = FreeMarkerUtils.process(objectMap,GenUtils.class.getClassLoader()  ,GenConfig.getTemplatePath(), "xml.ftl");
        result.put("xml", xml);

        // 处理mapper
        String mapper = FreeMarkerUtils.process(objectMap,GenUtils.class.getClassLoader()  ,GenConfig.getTemplatePath(), "mapper.ftl");
        result.put("mapper", mapper);

        // 处理iservice
        String iservice = FreeMarkerUtils.process(objectMap,GenUtils.class.getClassLoader()  ,GenConfig.getTemplatePath(), "iservice.ftl");
        result.put("iservice", iservice);

        // 处理service
        String service = FreeMarkerUtils.process(objectMap,GenUtils.class.getClassLoader()  ,GenConfig.getTemplatePath(), "service.ftl");
        result.put("service", service);

        // 处理controller
        String controller = FreeMarkerUtils.process(objectMap,GenUtils.class.getClassLoader()  ,GenConfig.getTemplatePath(), "controller.ftl");
        result.put("controller", controller);

        // 处理list
        String list = FreeMarkerUtils.process(objectMap,GenUtils.class.getClassLoader()  ,GenConfig.getTemplatePath(), "list.ftl");
        result.put("list", list);

        // 处理list
        String form = FreeMarkerUtils.process(objectMap,GenUtils.class.getClassLoader()  ,GenConfig.getTemplatePath(), "form.ftl");
        result.put("form", form);

        // 处理api
        String api = FreeMarkerUtils.process(objectMap,GenUtils.class.getClassLoader()  ,GenConfig.getTemplatePath(), "api.ftl");
        result.put("api", api);
        return result;
    }

    /**
     * 初始化字段信息
     * */
    private static List<ColumnVO> initColumns(List<ColumnVO> columnVOS){
        List<ColumnVO> list = new ArrayList<>();
        for(ColumnVO columnVO: columnVOS){
            if(CollUtils.contains(baseColumns, columnVO.getName())){
               continue;
            }

            columnVO.setMin(0);
            columnVO.setFieldName(StringUtils.toCamelCase(columnVO.getName()));

            columnVO.setComment(ConvertUtils.toStr(columnVO.getComment(),""));

            if(ObjectUtils.isNotNull(columnVO.getMaxLength())){
                columnVO.setMax(columnVO.getMaxLength());
            }else{
                columnVO.setMax(0);
            }


            String fieldType = GenUtils.getFieldType(columnVO.getDataType());
            columnVO.setFieldType(fieldType);

            if("PRI".equalsIgnoreCase(columnVO.getColumnKey())){
                columnVO.setIsKey(true);
            }else{
                columnVO.setIsKey(false);
            }

            list.add(columnVO);
            log.debug("column转换:{}", columnVO.toString());
        }
        return list;
    }

    private static String toEntityName(String tableName){
         return StringUtils.convertToCamelCase(tableName);
    }
    /**
     * 主键字段
     * */
    private static String getKeyField(List<ColumnVO> columns,String className){
        ColumnVO column = columns.stream().filter(item -> item.getIsKey() == true).findFirst().get();
        if(column == null){
            return className;
        }else{
            return   column.getFieldName();
        }
    }

}
