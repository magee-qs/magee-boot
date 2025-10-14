 
import useAuth from "@/hooks/auth"
import useMessage from "../hooks/message"
 
import {  transParams } from "./common"
import errorCode from "./errorCode"

let timeout = 10000
const baseURL = import.meta.env.VITE_APP_BASEAPI

const request = (config) => { 
	const token = useAuth.getToken()
	config.header = config.header || {}
	if (token) {
		config.header['X-Access-Token'] = token
	}
	if (config.method == 'get' && config.params) {
		const params = transParams(config.params)
		let url = config.url + '?' + params
		config.params = {}
		config.url = url
	}
	return new Promise((resolve, reject) => {
		uni.request({
			method: config.method || 'get',
			timeout: config.timeout || timeout,
			url: config.baseUrl || baseURL + config.url,
			data: config.data,
			header: config.header,
			dataType: 'json'
		}).then((res : any) => { 
			const code = res.data.code
			const message = errorCode[code] || res.data.message || errorCode['default']
			if (code == 401) {
				useMessage.alert('登录状态已过期,请重新登录!').then(() => {
					// uni.reLaunch({
					// 	url: '/pages/login'
					// })
				})
			} else if (code == 200) {
				resolve(res.data)
			} else {
				useMessage.toast(message)
				reject(message)
			}
		}).catch(error => {
			let { message } = error
			if (message == 'Network Error') {
				message = '接口连接异常'
			} else if (message.includes('timeout')) {
				message = '接口请求超时'
			} else if (message.includes('Request failed with status code')) {
				message = '系统接口' + message.substr(message.length - 3) + '异常'
			}
			useMessage.toast(message)
			reject(error)
		})
	})
}

export default request