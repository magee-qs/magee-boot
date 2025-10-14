import request from "@/utils/request";

/** 查询角色列表 */
export function getRoleList(query) {
    return request({
        url: '/sys/role/list',
        method: 'get',
        params: query
    })
}
/** 查询角色 */
export function getRole(roleId) {
    return request({
        url: '/sys/role/' + roleId,
        method: 'get'
    })
}

/** 添加角色 */
export function addRole(form) {
    return request({
        url: '/sys/role/add',
        data: form,
        method: 'post'
    })
}

/** 修改角色 */
export function updateRole(form) {
    return request({
        url: '/sys/role/update',
        data: form,
        method: 'post'
    })
}

/** 修改角色 */
export function removeRole(roleId) {
    return request({
        url: '/sys/role/remove/' + roleId,
        method: 'delete'
    })
}

/** 修改角色状态 */
export function changeRoleState(roleId) {
    return request({
        url: '/sys/role/changeStatus/' + roleId,
        method: 'get'
    })
}

/** 获取角色分配用户 */
export function getUserRoleList(queryParam) {
    return request({
        url: '/sys/role/userList',
        params: queryParam,
        method: 'get'
    })
}

/** 保存用户角色 */
export function saveUserRole(roleId, userIds) {
    return request({
        url: '/sys/role/saveUser',
        method: 'post',
        data: { roleId, userIds }
    })
}

/** 删除角色用户 */
export function removeUserRole(roleId, userIds){
       return request({
        url: '/sys/role/removeUser',
        method: 'post',
        data: { roleId, userIds }
    })
}

