import router, { constantRouteMap, dynamicRouteMap } from "@/router";
import { defineStore } from "pinia";
import Layout from '@/layout/TabLayout.vue'
import InnerLink from '@/layout/page/GlobalLink.vue'
import ParentView from "@/layout/tool/ParentView.vue";
import useAuth from "@/hooks/auth";
import { getRouters } from "@/api/login";

// 匹配views里面所有的.vue文件
const modules = import.meta.glob('@/views/**/*.vue')

const usePermissionStore = defineStore('permission', {
    state: () => ({
        routes: [],
        addRoutes: [],
        defaultRoutes: [],
        topbarRouters: [],
        sidebarRouters: []
    }),
    actions: {
        setRoutes(routes) {
            this.addRoutes = routes
            this.routes = constantRouteMap.concat(routes)
        },
        setDefaultRoutes(routes) {
            this.defaultRoutes = constantRouteMap.concat(routes)
        },
        setTopbarRoutes(routes) {
            this.topbarRouters = routes
        },
        setSidebarRouters(routes) {
            this.sidebarRouters = routes
        },
        generateRoutes() { 
            // 向后端请求路由数据
            return getRouters().then(res => { 
                const sdata = JSON.parse(JSON.stringify(res.data))
                const rdata = JSON.parse(JSON.stringify(res.data))
                const defaultData = JSON.parse(JSON.stringify(res.data))
                const sidebarRoutes = filterAsyncRouter(sdata)
                const rewriteRoutes = filterAsyncRouter(rdata, false, true)
                const defaultRoutes = filterAsyncRouter(defaultData)
                const asyncRoutes = filterDynamicRoutes(dynamicRouteMap)
                asyncRoutes.forEach(route => { router.addRoute(route) })
                this.setRoutes(rewriteRoutes)
                this.setSidebarRouters(constantRouteMap.concat(sidebarRoutes))
                this.setDefaultRoutes(sidebarRoutes)
                this.setTopbarRoutes(defaultRoutes)
                return rewriteRoutes
            })
        }
    }
})

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap, lastRouter = false, type = false) {
    return asyncRouterMap.filter(route => {
        if (type && route.children) {
            route.children = filterChildren(route.children)
        }
        if (route.component) {
            // Layout ParentView 组件特殊处理
            if (route.component === 'Layout') {
                route.component = Layout
            } else if (route.component === 'ParentView') {
                route.component = ParentView
            } else if (route.component === 'InnerLink') {
                route.component = InnerLink
            } else {
                route.component = loadView(route.component)
            }
        }
        if (route.children != null && route.children && route.children.length) {
            route.children = filterAsyncRouter(route.children, route, type)
        } else {
            delete route['children']
            delete route['redirect']
        }
        return true
    })
}

function filterChildren(childrenMap, lastRouter: any = false) {
    var children = []
    childrenMap.forEach(el => {
        el.path = lastRouter ? lastRouter.path + '/' + el.path : el.path
        if (el.children && el.children.length && el.component === 'ParentView') {
            children = children.concat(filterChildren(el.children, el))
        } else {
            children.push(el)
        }
    })
    return children
}

// 动态路由遍历，验证是否具备权限
export function filterDynamicRoutes(routes) {
    const res = []
    routes.forEach(route => {
        if (route.permissions) {
            if (useAuth.hasPermAny(route.permissions)) {
                res.push(route)
            }
        } else if (route.roles) {
            if (useAuth.hasRoleAny(route.roles)) {
                res.push(route)
            }
        }
    })
    return res
}

export const loadView = (view) => {
    let res
    for (const path in modules) {
        const dir = path.split('views/')[1].split('.vue')[0]
        if (dir === view) {
            res = () => modules[path]()
        }
    }
    return res
}


export default usePermissionStore