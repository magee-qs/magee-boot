const prefixKey = 'megee-'

/** 缓存操作 */
const cache = {
	/* *
	 * 写入缓存
	 */
	set(key, value) {
		try {
			uni.setStorageSync(prefixKey + key, value)
			return true
		} catch (err) {
			console.error('写入缓存失败', err)
			return false
		}

	},
	/**
	 * 读取缓存
	*/
	get(key) {
		try {
			return uni.getStorageSync(prefixKey + key)
		} catch (err) {
			console.error('读取缓存失败', err)
			return null
		}
	},
	/**
	 * 删除缓存
	*/
	remove(key) {
		try {
			uni.removeStorageSync(prefixKey + key)
			return true
		} catch (err) {
			console.error('删除缓存失败', err)
			return false
		}

	},
	/**
	 * 清空缓存
	*/
	clear() {
		try {
			uni.clearStorageSync()
			return true
		} catch (err) {
			console.error('清空缓存失败', err)
			return false
		}
	}
}

export default cache