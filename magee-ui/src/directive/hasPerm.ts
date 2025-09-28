import useUserStore from "@/store/modules/user"

/** 判断是否具备操作权限 */
export default {
    mounted(el, binding, vnode) {
        const { value } = binding
        const all_permission = '*:*:*'
        const permissions = useUserStore().permissions
        let hasPerm = false
        if (value && value instanceof Array && value.length > 0) {
            // 权限数组 ['sys:user:add','sys:user:edit']
            hasPerm = permissions.some(perm => {
                return all_permission == perm || value.includes(perm)
            })
        } else {
            // 权限 'sys:user:add'
            hasPerm = permissions.some(perm => {
                return all_permission == perm || value == perm
            })
        }
        // 无权限
        if (!hasPerm) {
            el.parentNode && el.parentNode.removeChild(el)
        }
    },
}