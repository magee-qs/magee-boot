/** 系统参数配置 */

import request from "@/utils/request";

/** 请求参数分页列表 */
export function getConfigList(queryParam){ 
    return request({
        url: '/sys/config/list',
        method: 'get',
        params: queryParam
    })
}

/** 获取参数 */
export function getConfig(configId){
    return request({
        url: '/sys/config/' + configId,
        method: 'get'
    })
}

/** 新增参数 */
export function addConfig(form){
    return request({
        url: '/sys/config/add',
        method: 'post',
        data: form
    })
}

/** 修改参数 */
export function editConfig(form){
    return request({
        url: '/sys/config/edit',
        method: 'post',
        data: form
    })
}

/** 删除参数 */
export function removeConfig(configId){
    return request({
        url: '/sys/config/remove/' + configId,
        method: 'get'
    })
}
 

