const useMessage = {
	/**
	 * 显示消息提示框
	 * @param {any} content  提示的标题
	 * @return 
	  */
	toast(content) {
		uni.showToast({
			icon: 'none',
			title: content
		})
	}, 
	error(content){
		uni.showToast({
			title:content,
			icon:'error'
		})
	},
	success(content = '操作成功'){
		uni.showToast({
			title:content,
			icon:"success"
		})
	},
	alert(content, title = '系统提示'){
		return new Promise((resolve, reject) => {
			uni.showModal({
				title: title ,
				content: content,
				showCancel: false,
				success() {
					resolve('ok')
				},
				fail() {
					reject('cancel')
				}
			})
		}) 
	},
	/**
	 * 确认对话框
	 * @param {any} content 
	 * @return 
	  */
	confirm(content) {
		return new Promise((resolve, reject) => {
			uni.showModal({
				title: '提示',
				content: content,
				cancelText: '取消',
				confirmText: '确定',
				success: (res) => {
					resolve(res)
				},
				fail: (err) => {
					reject(err)
				}
			})
		})
	}
}

export default useMessage