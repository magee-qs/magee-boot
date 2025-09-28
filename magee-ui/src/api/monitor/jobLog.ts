import request from "@/utils/request";
/** 定时任务日志 接口api */


/** 获取定时任务日志分页数据
* @param queryParam 查询参数
*/
export function getSysJobLogList(queryParam){
    return request({
        url: '/monitor/jobLog/list',
        method: 'get',
        params: queryParam
    })
}

/** 获取定时任务日志实体对象
* @param logId 主键
*/
export function getSysJobLog(logId){
    return request({
        url: '/monitor/jobLog/' + logId,
        method: 'get'
    })
}

/** 添加定时任务日志
* @param form 表单参数
*/
export function addSysJobLog(form){
    return request({
        url: '/monitor/jobLog/add',
        method: 'post',
        data : form
    })
}

/** 修改定时任务日志
* @param form 表单参数
*/
export function updateSysJobLog(form){
    return request({
        url: '/monitor/jobLog/update',
        method: 'post',
        data: form
    })
}
/** 修改状态
* @param logId 主键id
*/
export function changeSysJobLogStatus(logId){
    return request({
        url: '/monitor/jobLog/status/' + logId,
        method: 'get'
    })
}

/** 删除对象
* @param logId 主键id
*/
export function removeSysJobLog(logId){
    return request({
        url: '/monitor/jobLog/remove/' + logId,
        method: 'get'
    })
}