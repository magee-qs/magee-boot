import request from "@/utils/request";
/** 定时任务 接口api */


/** 获取定时任务分页数据
* @param queryParam 查询参数
*/
export function getSysJobList(queryParam){
    return request({
        url: '/monitor/job/list',
        method: 'get',
        params: queryParam
    })
}

/** 获取定时任务实体对象
* @param jobId 主键
*/
export function getSysJob(jobId){
    return request({
        url: '/monitor/job/' + jobId,
        method: 'get'
    })
}

/** 添加定时任务
* @param form 表单参数
*/
export function addSysJob(form){
    return request({
        url: '/monitor/job/add',
        method: 'post',
        data : form
    })
}

/** 修改定时任务
* @param form 表单参数
*/
export function updateSysJob(form){
    return request({
        url: '/monitor/job/update',
        method: 'post',
        data: form
    })
}
/** 修改状态
* @param jobId 主键id
*/
export function changeSysJobStatus(jobId){
    return request({
        url: '/monitor/job/status/' + jobId,
        method: 'get'
    })
}

/** 删除对象
* @param jobId 主键id
*/
export function removeSysJob(jobId){
    return request({
        url: '/monitor/job/remove/' + jobId,
        method: 'get'
    })
}

/** 启动任务 */
export function startJob(jobId){
    return request({
        url: '/monitor/job/startJob/' + jobId,
        method: 'get'
    })
}

/** 立刻执行任务一次 */
export function runJob(jobId){
    return request({
        url: '/monitor/job/runJob/' + jobId,
        method: 'get'
    })
}

/** 立刻执行任务一次 */
export function resumeJob(jobId){
    return request({
        url: '/monitor/job/resumeJob/' + jobId,
        method: 'get'
    })
}

/** 立刻执行任务一次 */
export function pauseJob(jobId){
    return request({
        url: '/monitor/job/pauseJob/' + jobId,
        method: 'get'
    })
}

/** 立刻执行任务一次 */
export function removeJob(jobId){
    return request({
        url: '/monitor/job/removeJob/' + jobId,
        method: 'get'
    })
}