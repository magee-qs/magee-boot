/** 
 * 部门数据操作
 */

import request from "@/utils/request";

 

/** 查询部门数据 */
export function getDepartList(queryParam){
    return request({
        url: '/sys/depart/list',
        params: queryParam,
        method: 'get'
    })
}

/** 添加部门 */
export function addDepart(form){
    return request({
        url: '/sys/depart/add',
        method: 'post',
        data: form
    })
}

/** 修改部门 */
export function updateDepart(form){
     return request({
        url: '/sys/depart/update',
        method: 'post',
        data: form
    })
}

/** 获取部门 */
export function getDepart(departId){
    return request({
        url: '/sys/depart/' + departId,
        method: 'get'
    })
}