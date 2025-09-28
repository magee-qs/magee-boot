
<template>
  <router-view></router-view>
</template>
<script setup lang="ts">
import { nextTick, onMounted } from 'vue';
import { handleThemeStyle } from './utils/theme';
import useSettingStore from './store/modules/setting';

 onMounted(() => {
  nextTick(() => {
    handleThemeStyle(useSettingStore().theme)
  })
 })
</script>


 