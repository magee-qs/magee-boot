<template>
  <div :class="classObj" class="app-wrapper" :style="{ '--current-color': theme }">
    <div v-if="device === 'mobile' && sidebar.opened" class="drawer-bg" @click="handleClickOutside"></div>
    <GlobalLeft v-if="!sidebar.hide" class="sidebar-container"></GlobalLeft>
    <div :class="{ hasTagsView: needTagsView, sidebarHide: sidebar.hide }" class="main-container">
      <div :class="{ 'fixed-header': fixedHeader }">
        <GlobalNav @setLayout="setLayout"></GlobalNav>
        <GlobalView v-if="needTagsView"></GlobalView>
      </div>
      <PageView></PageView>
      <GlobalSetting ref="settingRef"></GlobalSetting>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { computed, ref, watch, watchEffect } from 'vue';
import GlobalView from './page/GlobalView.vue';
import useSettingStore from '@/store/modules/setting';
import useAppStore from '@/store/modules/app';
import { useWindowSize } from '@vueuse/core';
import GlobalSetting from './page/GlobalSetting.vue';
import PageView from './tool/PageView.vue';
import GlobalLeft from './page/GlobalLeft.vue';
import GlobalNav from './page/GlobalNav.vue';

const settingStore = useSettingStore()
const appStore = useAppStore()
const theme = computed(() => settingStore.theme)
const needTagsView = computed(() => settingStore.tagsView) 
const fixedHeader = computed(() => settingStore.fixedHeader)
const sidebar = computed(() => appStore.sidebar)
const device = computed(() => appStore.device)

const classObj = computed(() => ({
  hideSidebar: !sidebar.value.opened,
  openSidebar: sidebar.value.opened,
  withoutAnimation: sidebar.value.withoutAnimation,
  mobile: device.value === 'mobile'
}))

const { width, height } = useWindowSize()
const WIDTH = 992 // refer to Bootstrap's responsive design

watch(() => device.value, () => {
  if (device.value === 'mobile' && sidebar.value.opened) {
    appStore.closeSideBar({ withoutAnimation: false })
  }
})

watchEffect(() => {
  if (width.value - 1 < WIDTH) {
    appStore.toggleDevice('mobile')
    appStore.closeSideBar({ withoutAnimation: true })
  } else {
    appStore.toggleDevice('desktop')
  }
})

function handleClickOutside() {
  appStore.closeSideBar({ withoutAnimation: false })
}
const settingRef = ref(null)
function setLayout() {
  settingRef.value.openSetting()
}

</script>

<style lang="scss" scoped>
@use "@/assets/styles/mixin.scss" as mix;
@use "@/assets/styles/variables.module.scss" as vars;

.app-wrapper {
  @include mix.clearfix;
  position: relative;
  height: 100%;
  width: 100%;

  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}

.main-container:has(.fixed-header) {
  height: 100vh;
  overflow: hidden;
}

.drawer-bg {
  background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}

.fixed-header {
  position: fixed;
  top: 0;
  right: 0;
  z-index: 9;
  width: calc(100% - #{vars.$base-sidebar-width});
  transition: width 0.28s;
}

.hideSidebar .fixed-header {
  width: calc(100% - 54px);
}

.sidebarHide .fixed-header {
  width: 100%;
}

.mobile .fixed-header {
  width: 100%;
}
</style>