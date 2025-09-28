package com.magee.framework.core.query;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.magee.common.exception.BaseException;
import com.magee.common.utils.CollUtils;
import com.magee.common.utils.ConvertUtils;
import com.magee.common.utils.ServletUtils;
import com.magee.common.utils.StringUtils;
import com.magee.common.utils.reflect.ReflectUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 功能描述:
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Slf4j
public  class SimpleQuery {

    /**
     * 查询参数化
     * @param searchObj 查询参数
     * */
    public static  <T> QueryWrapper<T> toQueryWrapper(Object searchObj){
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        toQueryWrapper(queryWrapper, searchObj, null);
        return queryWrapper;
    }

    /**
     * 查询参数化
     * @param searchObj 查询参数
     * @param prefix 拼接前缀 例如 prefix: u  create_time => u.create_time
     * @param ignoreFields 忽略字段 , 不参查询条件
     * */
    public static  <T> QueryWrapper<T> toQueryWrapper(Object searchObj,String prefix, String... ignoreFields){
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        toQueryWrapper(queryWrapper,searchObj,prefix, ignoreFields);
        return queryWrapper;
    }

    /**
     * 查询参数化
     * @param searchObj 查询参数
     * @param queryWrapper 查询结构体
     * */
    public static void toQueryWrapper(QueryWrapper<?>queryWrapper, Object searchObj){
        toQueryWrapper(queryWrapper,searchObj,null);
    }

    /**
     * 查询参数化
     * @param searchObj 查询参数
     * @param prefix 拼接前缀 例如 prefix: u  create_time => u.create_time
     * @param ignoreFields 忽略字段 , 不参查询条件
     * */
    public static void toQueryWrapper(QueryWrapper<?>queryWrapper, Object searchObj, String prefix, String... ignoreFields){
        long start = System.currentTimeMillis();
        initQueryWrapper(queryWrapper,searchObj, prefix, ignoreFields);
        log.debug("======查询条件构造器初始化完成,耗时:{}毫秒======", System.currentTimeMillis()-start);
    }

    /**
     * 组装查询条件
     * @param searchObj 查询参数
     * @param prefix 拼接前缀 例如 prefix: u  create_time => u.create_time
     * @param ignoreFields 忽略字段 , 不参查询条件
     * */
    private static void initQueryWrapper(QueryWrapper<?>queryWrapper, Object searchObj, String prefix, String... ignoreFields){
        String name ="" , type ="" , column ="";
        // 获取查询字段
        Field[] fields = ReflectUtils.getAllFields(searchObj);
        // 忽略字段集合
        List<String> arr= CollUtils.toList(ignoreFields);
        List<QueryField> list = new ArrayList<>();
        for(int i = 0 ; i < fields.length ; i++){
            Field field = fields[i];
            //不存在实体属性 不用处理别名的情况
            name = field.getName();
            // 跳过忽略查询字段
            if(arr.contains(name)){
                continue;
            }
            type = field.getType().toString();
            try{
                if(QueryUtils.isUsingField(name)){
                    continue;
                }
                // 获取表字段名 prefix 默认为空
                // prefix有值的 例如 column = u.create_time
                column = QueryUtils.getTableColumn(field,  prefix);
                if(column == null){
                    //column为null 添加了注解@TableField(exist = false)
                    continue;
                }
                // 数据权限查询
                // TODO: 2025/9/16

                // 构造查询条件
                List<QueryField> queryFields = new ArrayList<>();
                if(searchObj != null){
                    // 查询对象进行查询
                    queryFields = QueryUtils.doEasyQuery(searchObj, type, name, column);
                }
                list.addAll(queryFields);
            }catch (Exception e){
                log.error("构造查询条件出错:field: {}, type: {}, column: {},message: {}", name, type, column, e.getMessage(), e);
            }
        }

        if(list != null && list.size() > 0){
            for(QueryField field : list){
                field.doQuery(queryWrapper);
            }
        }
    }

    /**
     * 生成分页参数
     * 只能选一个表字段进行排序
     * 后期优化为连接查询排序
     * */
    public static  <T> IPage<T> toPage(){
        return toPage(null);
    }
    /**
     * 生成分页参数
     * @param prefix 字段前缀
     * */
    public static  <T> IPage<T> toPage(String prefix){
        HttpServletRequest request = ServletUtils.getRequest();
        // 默认第一页
        Integer pageNum = ConvertUtils.toInt(request.getParameter(QueryConstant.PageNum) ,1) ;
        // 默认每页10条记录
        Integer pageSize = ConvertUtils.toInt(request.getParameter(QueryConstant.PageSize) ,10) ;

        Map<String,String[]> parameterMap = request.getParameterMap();

        List<OrderItem> orders = new ArrayList<>();
        for(int i = 0 ; i< 100 ; i++){
            String columnKey = "orders[" + i + "].column";
            String orderByKey = "orders[" + i + "].asc";

            // 排序列名
            String column = QueryUtils.getParameterValue(parameterMap,columnKey);
            // 排序方式
            Boolean asc = ConvertUtils.toBool(QueryUtils.getParameterValue(parameterMap,orderByKey), true);

            // 没有参数退出循环
            if(StringUtils.isEmpty(column)){
                break;
            }

            if(column.contains(",")){
                // 多字段排序
                List<OrderItem> orderItems;
                String[] splits = column.split(",");
                List<String> columns = new ArrayList<>();
                for(int j = 0 ; j< splits.length ; j++ ){
                    splits[j] = QueryUtils.getTableColumn(splits[j], prefix);
                }
                if(asc.equals(true)){
                    orderItems = OrderItem.ascs(splits);
                }else{
                    orderItems = OrderItem.descs(splits);
                }
                orders.addAll(orderItems);
            }else{
                OrderItem orderItem;
                if(asc.equals(true)){
                    orderItem = OrderItem.asc(QueryUtils.getTableColumn(column, prefix));
                }else{
                    orderItem = OrderItem.desc(QueryUtils.getTableColumn(column, prefix));
                }
                orders.add(orderItem);
            }
        }
        if(orders.size() == 0){
            // 添加默认参数
            orders.add(OrderItem.asc("create_time"));
        }
        // 初始化参数
        IPage page = new Page(pageNum,pageSize);
        page.orders().addAll(orders);
        return page;
    }


    /**
     *  转换为更新结构查询
     * */
    public static <T> UpdateWrapper<T> toUpdateWrapper(Object updateObj, String ...ignoreFields){
        UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
        toUpdateWrapper(updateWrapper, updateObj, ignoreFields);
        return  updateWrapper;
    }

    /**
     * 转换为更新语句
     * */
    public static void toUpdateWrapper(UpdateWrapper<?> updateWrapper, Object updateObj, String... ignoreFields){
        Field[] fields = ReflectUtils.getAllFields(updateObj.getClass());
        List<String> names = Arrays.asList(ignoreFields);
        for(Field field: fields){
            String name = field.getName();
            // 不更新字段忽略
            if(CollUtils.contains(names, name)){
                continue;
            }
            // 数据库字段
            String column = QueryUtils.getTableColumn(field, null) ;
            if(StringUtils.isEmpty(column)){
                continue;
            }
            Object value =null ;
            try{
                value = ReflectUtil.getFieldValue(updateObj, field);
                // 判断是否主键
                Object annotation = field.getAnnotation(TableId.class);
                if(annotation != null){
                    // 主键生成查询
                    updateWrapper.eq(column, value);
                }else{
                    updateWrapper.set(column, value);
                }
            }catch (Exception ex){
                log.error("生成更新wrapper出错, fieldName:{}, value:{} ,type:", name, value);
                throw new BaseException("生成更新查询条件出错");
            }
        }
    }
}
