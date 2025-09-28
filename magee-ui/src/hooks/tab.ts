import router from "@/router"
import useViewStore from "@/store/modules/view"

const useTab = {
    /**
     * 刷新当前tab页签
     * @param obj 
     */
    refreshPage(obj) {
        const { path, query, matched } = router.currentRoute.value
        if (obj === undefined) {
            matched.forEach(m => {
                if (m.components && m.components.default && m.components.default.name) {
                    if (!['Layout', 'ParentView'].includes(m.components.default.name)) {
                        obj = { name: m.components.default.name, path: path, query: query }
                    }
                }
            })
        }
        return useViewStore().delCachedView(obj).then(() => {
            const { path, query } = obj
            router.replace({
                path: '/redirect' + path,
                query: query
            })
        })
    },
    /**
     * 关闭当前tab页签，打开新页签
     * @param obj 
     */
    closeOpenPaeg(obj) {
        useViewStore().delView(router.currentRoute.value)
        if (obj !== undefined) {
            return router.push(obj)
        }
    },
    /**
     * 关闭指定tab页签
     * @param obj 
     */
    closePage(obj) {
        if (obj === undefined) {
            return useViewStore().delView(router.currentRoute.value).then((data: any) => {
                const latestView = data.visitedViews.slice(-1)[0]
                if (latestView) {
                    return router.push(latestView.fullPath)
                }
                return router.push('/')
            })
        }
        return useViewStore().delView(obj)
    },
    // 关闭所有tab页签
    closeAllPage() {
        return useViewStore().delAllViews()
    },
    // 关闭左侧tab页签
    closeLeftPage(obj) {
        return useViewStore().delLeftTags(obj || router.currentRoute.value)
    },
    // 关闭右侧tab页签
    closeRightPage(obj) {
        return useViewStore().delRightTags(obj || router.currentRoute.value)
    },
    // 关闭其他tab页签
    closeOtherPage(obj) {
        return useViewStore().delOthersViews(obj || router.currentRoute.value)
    },
    // 打开tab页签
    openPage(title, url, params) {
        const obj = { path: url, meta: { title: title } }
        useViewStore().addView(obj)
        return router.push({ path: url, query: params })
    },
    // 修改tab页签
    updatePage(obj) {
        return useViewStore().updateVisitedView(obj)
    }
}

export default useTab