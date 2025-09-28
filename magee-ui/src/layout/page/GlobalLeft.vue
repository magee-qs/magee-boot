<template>
    <div :class="{ 'has-logo': showLogo }" class="sidebar-container"> 
        <Logo v-if="showLogo" :collapse="isCollapse"></Logo>
        <el-scrollbar wrap-class="scrollbar-wrapper">
            <el-menu :default-active="activeMenu" :collapse="isCollapse" :background-color="getMenuBackground"
                :text-color="getMenuTextColor" :unique-opened="true" :active-text-color="theme"
                :collapse-transition="false" mode="vertical" :class="sideTheme">
                <SideMenu v-for="(route, index) in sidebarRouters" :key="route.path + index" :item="route"
                    :base-path="route.path"></SideMenu>
            </el-menu>
        </el-scrollbar>
    </div>
</template>
<script lang="ts" setup>
import useAppStore from '@/store/modules/app';
import usePermissionStore from '@/store/modules/permissoin';
import useSettingStore from '@/store/modules/setting';
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import variables from '@/assets/styles/variables.module.scss'
import Logo from '../menu/Logo.vue';
import SideMenu from '../menu/SideMenu.vue';
 
const route = useRoute()
const appStore = useAppStore()
const settingStore = useSettingStore()
const permissionStore = usePermissionStore()

const sidebarRouters = computed(() => permissionStore.sidebarRouters)
const showLogo = computed(() => settingStore.sidebarLogo)
const sideTheme = computed(() => settingStore.theme)
const theme = computed(() => settingStore.theme)
const isCollapse = computed(() => !appStore.sidebar.opened)

// 获取菜单背景色
const getMenuBackground = computed(() => {
  if (settingStore.isDark) {
    return 'var(--sidebar-bg)'
  }
  return sideTheme.value === 'theme-dark' ? variables.menuBg : variables.menuLightBg
})

// 获取菜单文字颜色
const getMenuTextColor = computed(() => {
  if (settingStore.isDark) {
    return 'var(--sidebar-text)'
  }
  return sideTheme.value === 'theme-dark' ? variables.menuText : variables.menuLightText
})

const activeMenu = computed(() => {
  const { meta, path } = route
  if (meta.activeMenu) {
    return meta.activeMenu
  }
  return path
})

</script>

<style lang="scss" scoped>
.sidebar-container {
    background-color: v-bind(getMenuBackground);

    .scrollbar-wrapper {
        background-color: v-bind(getMenuBackground);
    }

    .el-menu {
        border: none;
        height: 100%;
        width: 100% !important;

        .el-menu-item,
        .el-sub-menu__title {
            &:hover {
                background-color: var(--menu-hover, rgba(0, 0, 0, 0.06)) !important;
            }
        }

        .el-menu-item {
            color: v-bind(getMenuTextColor);

            &.is-active {
                color: var(--menu-active-text, #409eff);
                background-color: var(--menu-hover, rgba(0, 0, 0, 0.06)) !important;
            }
        }

        .el-sub-menu__title {
            color: v-bind(getMenuTextColor);
        }
    }
}
</style>