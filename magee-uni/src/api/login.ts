import request from '@/utils/request'
 
/**
 * 获取验证码
 */
export function getCodeImg(){
    return request({
        url: '/sys/captcha',
        method:'get'
    })
}

/**
 * 
 * @param loginForm 用户登录
 * @returns 
 */
export function login(loginForm){
    return request({
        url: '/sys/login',
        method: 'post',
        data: loginForm
    })
}

/** 获取路由信息 */
export function getRouters() {
    return request({
        url: '/sys/getRouters',
        method: 'get'
    })
}

/** 获取用户信息 */
export function getInfo(){
    return request({
        url: '/sys/getInfo',
        method: 'get'
    })
}


export function logout(){
    return request({
        url: '/sys/logout',
        method: 'get'
    })
}