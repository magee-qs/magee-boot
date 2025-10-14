/** 日志接口 */

import request from "@/utils/request";

export function getLogList(queryParam){ 
    return request({
        url: '/sys/log/list',
        method: 'get',
        params: queryParam
    })
}


export function removeLog(ids){
    return request({
        url: '/sys/log/remove/' + ids,
        params: ids,
        method: 'get'
    })
}