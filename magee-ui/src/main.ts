import { createApp } from 'vue' 
 
import App from './App.vue'

import Cookies from 'js-cookie'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/dark/css-vars.css'
import locale from 'element-plus/es/locale/lang/zh-cn'

import '@/assets/styles/index.scss' // global css

// svg图标
import 'virtual:svg-icons-register'
import SvgIcon from '@/components/SvgIcon/index.vue'
import elementIcons from '@/components/SvgIcon/iconPlus'


import directive from './directive'


import store from './store'
import router from './router'


import './permission' // permission control
import ToolBar from './components/table/ToolBar.vue'
import Pagination from './components/table/Pagination.vue'
import BaseForm from './components/form/BaseForm.vue'

const app = createApp(App)

app.use(router)
app.use(store)
app.use(elementIcons)

// 全局组件
app.component('svg-icon', SvgIcon)
app.component('ToolBar' ,ToolBar)
app.component('Pagination', Pagination)
app.component('BaseForm', BaseForm)

directive(app)

app.use(ElementPlus, {
    locale: locale,
    // 支持 large、default、small
    size: Cookies.get('size') || 'default'
})
app.mount('#app')
