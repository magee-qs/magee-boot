import cache from "./cache"

const useTab = {
	// 关闭所有页面，打开到应用内的某个页面
	reLunch(url){
		return uni.reLaunch({
			url: url
		})
	},
	// 跳转到tabBar页面，并关闭其他所有非tabBar页面
	switchTab(url){
		return uni.switchTab({
		  url: url
		})
	},
	// 关闭当前页面，跳转到应用内的某个页面
	redirectTo(url) {
	  return uni.redirectTo({
	    url: url
	  })
	},
	// 保留当前页面，跳转到应用内的某个页面
	navigateTo(url) {
	  return uni.navigateTo({
	    url: url
	  })
	},
	// 关闭当前页面，返回上一页面或多级页面
	navigateBack() {
	  return uni.navigateBack()
	},
	// 保存页面参数
	saveParam(param){
		cache.set('query', param)
	},
	// 获取页面参数
	getParam(){
		return cache.get('query')
	}
}

export default useTab