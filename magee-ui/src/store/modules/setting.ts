import { defineStore } from "pinia";
import setting from "@/setting";
import { useDark, useToggle } from '@vueuse/core'
import { useDynamicTitle } from "@/hooks/dynamicTitle";

const isDark = useDark()
const toggleDark = useToggle(isDark)


const { sideTheme, showSettings, topNav, tagsView, tagsIcon, fixedHeader, sidebarLogo, dynamicTitle, footerVisible, footerContent } = setting

const storageSetting: any = localStorage.getItem('layout-setting') ? {} : JSON.stringify(localStorage.getItem('layout-setting'))

const useSettingStore = defineStore('setting', {
    state: () => ({
        title: '',
        theme: storageSetting.theme || '#409EFF',
        sideTheme: storageSetting.sideTheme || sideTheme,
        showSettings: showSettings,
        topNav: storageSetting.topNav === undefined ? topNav : storageSetting.topNav,
        tagsView: storageSetting.tagsView === undefined ? tagsView : storageSetting.tagsView,
        tagsIcon: storageSetting.tagsIcon === undefined ? tagsIcon : storageSetting.tagsIcon,
        fixedHeader: storageSetting.fixedHeader === undefined ? fixedHeader : storageSetting.fixedHeader,
        sidebarLogo: storageSetting.sidebarLogo === undefined ? sidebarLogo : storageSetting.sidebarLogo,
        dynamicTitle: storageSetting.dynamicTitle === undefined ? dynamicTitle : storageSetting.dynamicTitle,
        footerVisible: storageSetting.footerVisible === undefined ? footerVisible : storageSetting.footerVisible,
        footerContent: footerContent,
        isDark: isDark.value
    }),
    actions: {
        // 修改布局设置
        changeSetting(data) {
            const { key, value } = data
            if (this.hasOwnProperty(key)) {
                this[key] = value
            }
        },
        // 设置网页标题
        setTitle(title) {
            this.title = title
            useDynamicTitle()
        },
        // 切换暗黑模式
        toggleTheme() {
            this.isDark = !this.isDark
            toggleDark()
        }
    }
})

export default useSettingStore