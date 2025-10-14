import { getInfo, login, logout } from "@/api/login";
import useAuth from "@/hooks/auth";
import cache from "@/hooks/cache";
import { isEmpty, isHttp } from "@/utils/validate";
import { defineStore } from "pinia";

const baseUrl = import.meta.env.VITE_APP_BASEAPI

const useUserStore = defineStore('user', {
    state: () => ({
        token: '',
        userInfo: cache.get('userInfo'),
        roles: [],
        permissions: [],
        isLoad: false
    }),
    actions: {
        logout(){
            this.token = ''
            this.userInfo = {}
            this.isLoad  =  false
            useAuth.removeToken()
            logout()
        },
        login(loginForm){
            return new Promise((resolve, reject) => {
				login(loginForm).then((res : any) => {
					let token = res.data
					useAuth.setToken(token)
					resolve(token)
				}).catch(err => {
					reject(err)
				})
			})
        },
        getInfo(){
            return getInfo().then((res: any) => {
                this.userInfo = res.userInfo
				let avatar = this.userInfo.avatar || ""
				if (!isHttp(avatar)) {
					avatar = (isEmpty(avatar)) ?  '@/static/images/profile.jpg' :   baseUrl + avatar
				}
				this.userInfo.avatar = avatar
				this.roles = res.roles || ['ROLE_DEFAULT']
				this.permissions = res.permissions || [] 
                this.isLoad  = true
            })  
        }
    }
})


export default useUserStore