import request from "@/utils/request";

/** 代码生成器 */


/** 查询表名 */
export function getTableList(){
    return request({
        url: '/tool/gen/list',
        method: 'get'
    })
}

/** 生成代码 */
export function genCode(form){
    return request({
        url: '/tool/gen/generate',
        method:'post',
        data: form
    })
}