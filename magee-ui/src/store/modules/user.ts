import { getInfo, login, logout } from "@/api/login";
import { sys } from "@/utils/sys";
import { isEmpty, isHttp } from "@/utils/validate";
import { defineStore } from "pinia";
import defAva from '@/assets/images/profile.jpg'
import useMessage from "@/hooks/message";

const useUserStore = defineStore('user', {
    state: () => ({
        token: sys.getAccessToken(),
        userInfo: {} as any,
        permissions: [],
        roles: [] 
    }),
    actions: {
        logout() {
            return new Promise((resolve) => {
                console.log('清空用户缓存')
                this.token = ''
                this.userInfo = {}
                this.permissions = []
                this.roles = []
                sys.removeAccessToken()
                resolve('')

                logout() 
            })
        },
        login(loginForm) {
            return new Promise((resolve, reject) => {
                login(loginForm).then((res: any) => {
                    let token = res.data
                    sys.setAccessToken(token)
                    this.token = token
                    resolve(res)
                }).catch(error => {
                    reject(error)
                })
            })
        },
        // 获取用户信息
        getInfo() {
            return getInfo().then((res: any) => {
                // userInfo: {}, roles: [], permissions:[]
                this.userInfo = res.userInfo
                let avatar = this.userInfo.avatar || ""
                if (!isHttp(avatar)) {
                    avatar = (isEmpty(avatar)) ? defAva : import.meta.env.VITE_APP_BASE_API + avatar
                }
                this.userInfo.avatar = avatar
                this.roles = res.roles || ['ROLE_DEFAULT']
                this.permissions = res.permissions || [] 
                return res
            })
        }
    }
})

export default useUserStore