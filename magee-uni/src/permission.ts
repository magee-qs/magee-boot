import useAuth from "./hooks/auth"
import useMessage from "./hooks/message"
import useUserStore from "./store/user"

const loginPage = '/pages/login'

const whiteList = ['/pages/login']

// 页面跳转验证拦截器
let list = ["navigateTo", "redirectTo", "reLaunch", "switchTab"]

function checkWhite(url) {
	const path = url.split('?')[0]
	return whiteList.indexOf(path) != -1
}

export const setupPermission = () => {
    list.forEach(item => {
    let token = useAuth.getToken() 
    uni.addInterceptor( item , {
        invoke(to) {
            console.log('进入页面跳转')
            if(token){
                if(to.url == loginPage){
                    uni.reLaunch({
                        url: '/'
                    })
                    return true
                }else{ 
                    return true
                } 
            }else{
                if(checkWhite(to.url)){
                    return true
                }

                useMessage.alert('登录状态已经过期,请重新登录').then(() => {
                    uni.reLaunch({
                        url: loginPage
                    })
                }).catch(() => {
                     uni.reLaunch({
                        url: loginPage
                    })
                })
            }
        },
        fail(err){
            console.error('路由跳转失败', err)
        }
    })
})
}

