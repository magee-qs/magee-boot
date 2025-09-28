package com.magee.framework.core.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.magee.common.constant.CommonConstant;
import com.magee.common.utils.ConvertUtils;

import java.util.HashMap;

/**
 * 操作消息提醒
 * @author magee
 * @version 1.0
 */
public class AjaxResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public AjaxResult(){}

    public AjaxResult(int code , Object data, String message){
        super.put("code", code);
        super.put("data", data);
        super.put("message", message);
    }

    public static AjaxResult ok(Object data, String message){
        return new AjaxResult(CommonConstant.SC_OK, data, message);
    }

    public static AjaxResult ok(Object data){
        return ok(data, "");
    }


    public static AjaxResult ok(){
        return ok("操作成功");
    }

    public static AjaxResult ok(String message){
        return  ok(null, message);
    }

    public static AjaxResult error(int code ,Object data, String message){
        return new AjaxResult(code, data, message);
    }

    public static AjaxResult error(Object data, String message){
        return new AjaxResult(CommonConstant.SC_ERROR, data, message);
    }

    public static AjaxResult error(Object data){
        return error(data, "");
    }

    public static AjaxResult error(String message){
        return  error(null, message);
    }
    /** 校验失败 */
    public static AjaxResult auth(String message){
        return error(CommonConstant.SC_NOAUTH,null,message);
    }
    /** 无操作权限 */
    public static AjaxResult perm(String message){
        return error(CommonConstant.SC_NOPERM,null,message);
    }

    /** 返回分页数据 */
    public static AjaxResult list(Object data, Long total){
        AjaxResult result = AjaxResult.ok("");
        result.put("total", total);
        result.put("records", data);
        return result;
    }

    /** 返回分页数据 */
    public static AjaxResult list(IPage page){
        AjaxResult result = AjaxResult.ok("");
        result.put("total", ConvertUtils.toInt(page.getTotal()));
        result.put("records", page.getRecords());
        return result;
    }
}
