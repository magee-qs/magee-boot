import useUserStore from "@/store/modules/user"

function authPermission(permission) {
    const all_permission = "*:*:*"
    const permissions = useUserStore().permissions
    if (permission && permission.length > 0) {
        return permissions.some(v => {
            return all_permission === v || v === permission
        })
    } else {
        return false
    }
}
function authRole(role) {
    const super_admin = 'admin'
    const roles = useUserStore().roles
    if (role && role.length > 0) {
        return roles.some(v => {
            return super_admin === v || v === role
        })
    } else {
        return false
    }
}
const useAuth = {
    /**
     * 
     * @param perm 验证是否具备权限
     */
    hasPerm(perm) {
        return authPermission(perm)
    },
    /**
     *  验证用户是否含有指定权限，只需包含其中一个
     * @param perms 
     * @returns 
     */
    hasPermAny(perms) {
        return perms.some(item => {
            return authPermission(item)
        })
    },
    /**
     * 验证用户是否含有指定权限，必须全部拥有
     * @param perms 
     * @returns 
     */
    hasPermAll(perms) {
        return perms.every(item => {
            return authPermission(item)
        })
    },
    /**
     * 验证用户是否具备某角
     * @param role 
     * @returns 
     */
    hasRole(role) {
        return authRole(role)
    },
    /**
     * 验证用户是否含有指定角色，只需包含其中一个
     * @param roles 
     * @returns 
     */
    hasRoleAny(roles) {
        return roles.some(item => {
            return authRole(item)
        })
    },
    /**
     * 验证用户是否含有指定角色，必须全部拥有
     * @param roles 
     * @returns 
     */
    hasRoleAll(roles) {
        return roles.every(item => {
            return authRole(item)
        })
    }
}
export default useAuth