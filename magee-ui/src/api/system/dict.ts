import request from "@/utils/request";  

// 查询字典数据详细
export function getDicts(dictType) {
    return request({
        url: '/sys/dict/getDict/' + dictType,
        method: 'get'
    })
}

/** 获取字典数据 */
export function getAllDictData(){
    return request({
        url : '/common/getAllDictData',
        method:'get'
    })
}

/** 查询字典分页数据 */
export function getDictLsit(param){
    return request({
        url: '/sys/dict/data/list',
        method: 'get',
        params: param
    })
}
/** 添加数据 */
export function addDict(form){
    return request({
        url: '/sys/dict/data/add',
        method:'post',
        data: form
    })
}
/** 修改字典 */
export function editDict(form){
    return request({
        url: '/sys/dict/data/edit',
        method: 'post',
        data: form
    })
}
/** 需改状态 */
export function changeDictStatus(dictCode){
    return request({
        url: '/sys/dict/data/status/' + dictCode,
        method: 'get'
    })
}

/** 删除字段数据 */
export function removeDict(dictCode){
    return request({
        url:  '/sys/dict/data/remove/' + dictCode,
        method: 'get'
    })
}

/** 获取字典 */
export function getDict(dictCode){
    return request({
        url: '/sys/dict/data/' + dictCode,
        method: 'get'
    })
}
