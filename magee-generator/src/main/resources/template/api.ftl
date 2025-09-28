import request from "@/utils/request";
/** ${comment} 接口api */


/** 获取${comment}分页数据
* @param queryParam 查询参数
*/
export function get${entityName}List(queryParam){
    return request({
        url: '${url}/list',
        method: 'get',
        params: queryParam
    })
}

/** 获取${comment}实体对象
* @param ${keyParam} 主键
*/
export function get${entityName}(${keyParam}){
    return request({
        url: '${url}/' + ${keyParam},
        method: 'get'
    })
}

/** 添加${comment}
* @param form 表单参数
*/
export function add${entityName}(form){
    return request({
        url: '${url}/add',
        method: 'post',
        data : form
    })
}

/** 修改${comment}
* @param form 表单参数
*/
export function update${entityName}(form){
    return request({
        url: '${url}/update',
        method: 'post',
        data: form
    })
}
/** 修改状态
* @param ${keyParam} 主键id
*/
export function change${entityName}Status(${keyParam}){
    return request({
        url: '${url}/status/' + ${keyParam},
        method: 'get'
    })
}

/** 删除对象
* @param ${keyParam} 主键id
*/
export function remove${entityName}(${keyParam}){
    return request({
        url: '${url}/remove/' + ${keyParam},
        method: 'get'
    })
}