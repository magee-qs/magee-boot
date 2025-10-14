 
import request from "@/utils/request"

/**
 * 查询用户个人信息
 * @returns 
 */
export function getProfile() {
    return request({
        url: '/sys/profile/getProfile',
        method: 'get'
    })
}

/**
 * 
 * @param data 更新个人信息
 * @returns 
 */
export function updateProfile(data) {
    return request({
        url: '/sys/profile/updateProfile',
        method: 'post',
        data: data
    })
}

/**
 * 修改密码
 * @param data  oldPassword, newPassword
 * @returns 
 */
export function updatePwd(data) {
    return request({
        url: '/sys/profile/updatePwd',
        method: 'post',
        data: data
    })
}
/**
 * 上传头像
 * @param data 
 */
export function uploadAvatar(data) {
    return request({
        url: '/sys/profile/avatar',
        method: 'post',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        data: data
    })
}


export function getUserList(params): any{
    return request({
        url: '/sys/user/list',
        method: 'get',
        params: params
    })
}


/** 获取角色分配用户 */
export function getUserRoleList(queryParam) {
    return request({
        url: '/sys/role/roleList',
        params: queryParam,
        method: 'get'
    })
}

/** 保存用户角色 */
export function saveUserRole(userId, roleIds) {
    return request({
        url: '/sys/role/saveRole',
        method: 'post',
        data: { userId, roleIds }
    })
}

/** 删除角色用户 */
export function removeUserRole(userId, roleIds){
       return request({
        url: '/sys/role/removeRole',
        method: 'post',
        data: { userId, roleIds }
    })
}

/** 修改用户状态 */
export function changeUserStatus(userId){
    return request({
        url: '/sys/user/changeStatus/' + userId,
        method:'get'
    })
}

/** 重置用户密码 */
export function resetUserPwd(userId){
    return request({
        url: '/sys/user/resetPwd/' + userId,
        method:'get'
    })
}

/** 删除用户 */
export function removeUser(userId){
    return request({
        url: '/sys/user/remove/' + userId,
        method:'get'
    })
}

/** 新增用户 */
export function addUser(form){
    return request({
        url: '/sys/user/add',
        method: 'post',
        data: form
    })
}

/** 修改用户 */
export function updateUser(form){
    return request({
        url: '/sys/user/update',
        method: 'post',
        data: form
    })
}

/** 获取用户 */
export function getUserById(userId){
    return request({
        url: '/sys/user/' + userId,
        method:'get'
    })
}
