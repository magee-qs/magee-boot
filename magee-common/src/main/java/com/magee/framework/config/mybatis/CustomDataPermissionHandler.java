package com.magee.framework.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.magee.common.utils.CollUtils;
import com.magee.common.utils.ConvertUtils;
import com.magee.common.utils.StringUtils;
import lombok.SneakyThrows;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;


/**
 * 功能描述: 实现数据权限逻辑
 *
 * @author magee
 * @version 版本号:1.0.0
 */
public class CustomDataPermissionHandler implements DataPermissionHandler {
    /**
     * 拦截数据
     * */
    @SneakyThrows
    public Expression getSqlSegment(Expression where, String mappedStatementId) {
        // MyBatis-Plus会自动处理逻辑删除
        Expression dataPermissionExpression = buildDataPermissionExpression();

        if (dataPermissionExpression == null) {
            return where; // 返回原始条件（包含逻辑删除）
        }

        if (where == null) {
            return dataPermissionExpression;
        }
        // 合并：原始条件 AND 数据权限条件
        return new AndExpression(where, dataPermissionExpression);

    }


    /**
     * 生成查询参数
     * */
    private Expression buildDataPermissionExpression(){
        PermissionDataContext context = PermissionDataContextHolder.getContext();

        if(context == null){
            return null;
        }
        // 获取注解
        PermissionDataRule rule = context.getRule();

        // 放行数据过滤
        if(rule == null || rule == PermissionDataRule.All){
            return null;
        }

        String userColumn  = context.getUserColumn();
        String departColumn = context.getDepartColumn();

        String sql = StringUtils.EMPTY;
        switch (rule){
            case Custom:
                sql = context.getCustomSql();
                break;
            case Depart:
                sql = " " +  departColumn + " = " + ConvertUtils.toLong(context.getDepartId(), 0L)   + "   ";
                break;
            case AllDepart:
                sql = " " + departColumn + " in (" + CollUtils.join(context.getDepartIds(), ",") + " ) ";
                break;
            case User:
                sql = "  " + userColumn +"  = " + context.getUserId() + " ";
                break;
        }
        try{
            return CCJSqlParserUtil.parseCondExpression(sql);
        }catch (Exception ex){
            return null;
        }
    }


}
