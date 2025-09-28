import useUserStore from "@/store/modules/user"

/** 判断是否具备角色操作权限 */
export default {
    mounted(el, binding, vnode) {
        const { value } = binding
        const super_admin = 'admin'
        const roles = useUserStore().roles
        let hasPerm = false
        if (value && value instanceof Array && value.length > 0) {
            // 角色数组 ['role1','role2']
            hasPerm = roles.some(role => {
                return super_admin == role || value.includes(role)
            })
        } else {
            // 权限 'role1'
            hasPerm = roles.some(perm => {
                return super_admin == perm || value == perm
            })
        }
        // 无权限
        if (!hasPerm) {
            el.parentNode && el.parentNode.removeChild(el)
        }
    },
}