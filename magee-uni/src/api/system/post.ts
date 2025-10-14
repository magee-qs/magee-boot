import request from "@/utils/request";

/** 岗位管理 */
export function getPostList(queryParam){
    return request({
        url: '/sys/post/list',
        method: 'get',
        params: queryParam
    })
}

/** 获取岗位 */
export function getPost(postId){
    return request({
        url: '/sys/post/' + postId,
        method: 'get'
    })
}

/** 添加岗位 */
export function addPost(form){
    return request({
        url: '/sys/post/add',
        method: 'post',
        data : form
    })
}

/** 修改岗位 */
export function updatePost(form){
    return request({
        url: '/sys/post/update',
        method: 'post',
        data: form
    })
}
/** 修改状态 */
export function changePostStatus(postId){
    return request({
        url: '/sys/post/status/' + postId,
        method: 'get'
    })
}

/** 删除 */
export function removePost(postId){
    return request({
        url: '/sys/post/remove/' + postId,
        method: 'get'
    })
}