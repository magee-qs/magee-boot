import request from "@/utils/request";
/** 
 * 通用功能访问
 * 1. 主要通过缓存读取数据
 * 2. 大部门功能组件数去获取
 * 
 */

/** 获取字典数据 */
export function getDictData(dictType){
    return request({
        url: '/common/getDictData/'+ dictType,
        method: 'get'
    })
}

/** 获取用户分页数据 */
export function getUserList(queryParam){
    return request({
        url: '/common/getUserList',
        method: 'get',
        params: queryParam
    })
}

/** 获取角色分页数据 */
export function  getRoleList(queryParam){
    return request({
        url : '/common/getRoleList',
        method: 'get',
        params: queryParam
    })
}


/** 加载部门数据 */
export function getDepartList(){
    return request({
        url: '/common/getDepartList',
        method: 'get'
    })
}