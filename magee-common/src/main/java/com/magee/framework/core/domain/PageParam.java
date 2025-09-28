package com.magee.framework.core.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.magee.common.utils.ConvertUtils;
import com.magee.common.utils.ObjectUtils;
import com.magee.common.utils.StringUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 功能描述: 查询基础参数
 *
 * @author magee
 * @version 版本号:1.0.0
 */
@Data
public class PageParam {
     /** 页面 */
     @TableField(exist = false)
     private Integer pageNum = 1;


     /** 分页数 */
     @TableField(exist = false)
     private Integer pageSize = 10;


     private List<OrderItem> orders ;

     /** 分页参数转换 */
     public <T> IPage<T> toPage(){
          Page<T> page =new Page<>(pageNum , pageSize);
          if(orders != null && orders.size() > 0){
               orders.forEach(item -> {
                    item.setColumn(StringUtils.toUnderScoreCase(item.getColumn()));
               });
               page.addOrder(orders);
          }else{
               page.addOrder(OrderItem.asc("id"));
          }
          return page;
     }
}
