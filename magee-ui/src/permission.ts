import { ElMessage } from "element-plus"
import router from "./router"
import usePermissionStore from "./store/modules/permissoin"
import useSettingStore from "./store/modules/setting"
import useUserStore from "./store/modules/user"
import { isRelogin } from "./utils/request"
import { sys } from "./utils/sys"
import { isHttp, isPathMatch } from "./utils/validate"
import NProgress from 'nprogress'


const whiteList = ['/login']

const isWhiteList = (path) => {
    return whiteList.some(pattern => isPathMatch(pattern, path))
}

NProgress.configure({ showSpinner: false })

router.beforeEach((to, form, next) => {
    const settingStore = useSettingStore()
    const userStore = useUserStore()
    const permissionStore = usePermissionStore()
    NProgress.start()
    let token = sys.getAccessToken()
    if (token) {
        to.meta.title && settingStore.setTitle(to.meta.title)
        /** has token */
        if (to.path === '/login') {
            next({ path: '/' })
            NProgress.done()
        } else if (isWhiteList(to.path)) {
            next()
        } else {
            if (permissionStore.loaded == false) {
                // 加载用户信息
                userStore.getInfo().then(() => {
                    isRelogin.show = false
                    permissionStore.generateRoutes().then((accessRoutes: []) => {
                        // 根据roles权限生成可访问的路由表
                        accessRoutes.forEach((route: any) => {
                            if (!isHttp(route.path)) {
                                router.addRoute(route) // 动态添加可访问路由表
                            }
                        })
                        next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
                    })
                }).catch(err => {
                    userStore.logout().then(() => {
                        ElMessage.error(err)
                        next({ path: '/login'})
                    })
                })
            } else {
                next()
            }
        }
    } else {
        // 没有token
        if (isWhiteList(to.path)) {
            // 在免登录白名单，直接进入
            next()
        } else {
            next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
            NProgress.done()
        }
    }
})


router.afterEach(() => {
    NProgress.done()
})