package com.magee.framework.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.magee.common.utils.CollUtils;
import com.magee.common.utils.ConvertUtils;
import com.magee.common.utils.StringUtils;
import lombok.SneakyThrows;
import net.sf.jsqlparser.expression.Expression;
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
        PermissionDataContext context = PermissionDataContextHolder.getContext();

        if(context == null){
            return null;
        }

        PermissionDataRule rule = context.getRule();

        // 放行数据过滤
        if(rule == null || rule == PermissionDataRule.All){
            return null;
        }
        String sql = StringUtils.EMPTY;
        switch (rule){
            case Custom:
                sql = context.getCustomSql();
                break;
            case Depart:
                sql = " depart_id   =" + ConvertUtils.toLong(context.getDepartId(), 0L)   + "   ";
                break;
            case AllDepart:
                sql = " depart_id in (" + CollUtils.join(context.getDepartIds(), ",") + " ) ";
                break;
            case User:
                sql = " create_by = " + context.getUserId() + " ";
                break;
        }
        return CCJSqlParserUtil.parseCondExpression(sql);
    }

}
