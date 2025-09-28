import { localCache } from "@/hooks/cache"

// 系统工具
const accessTokenKey = 'sys-access-token'
const refreshTokenkey = 'sys-refresh-token'

export const sys =  {
    getAccessToken() {
        return localCache.get(accessTokenKey)
    },
    setAccessToken(accessToken) {
        localCache.set(accessTokenKey, accessToken)
    },
    removeAccessToken() {
        localCache.remove(accessTokenKey)
    },
    getRefreshToken() {
        return localCache.get(refreshTokenkey)
    },
    setRefreshToken(refreshToken) {
        localCache.set(refreshTokenkey, refreshToken)
    },
    removeRefreshToken() {
        localCache.remove(refreshTokenkey)
    }
}