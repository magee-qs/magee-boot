import useUserStore from "../store/user"
import cache from "./cache"

function authPermission(permission){
	const allPermission = '*:*:*'
	const permissions = useUserStore().permissions
	if(permission && permissions.length > 0){
		return permissions.some(v => {
			return allPermission == v || v == permission
		})
	}
	return false
}

function authRole(role){
	const super_admin = 'admin'
	const roles = useUserStore().roles
	if(role && roles.length > 0){
		return roles.some( v => {
			return v === super_admin || v == role
		})
	}
	return false
}

const tokenKey = 'X-Access-Token'

const useAuth = {
	hasPerm(permission){
		return authPermission(permission)
	},
	hasRole(role){
		return authRole(role)
	},
	getToken(){
		return cache.get(tokenKey)
	},
	setToken(token){
		cache.set(tokenKey, token)
	},
	removeToken( ){
		cache.remove(tokenKey)
	}
}

export default useAuth