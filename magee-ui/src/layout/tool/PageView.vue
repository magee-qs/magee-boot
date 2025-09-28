<template>
  <section class="app-main">
    <router-view v-slot="{ Component, route }">
      <transition name="fade-transform" mode="out-in">
        <keep-alive :include="viewStore.cachedViews">
          <component v-if="!route.meta.link" :is="Component" :key="route.path" />
        </keep-alive>
      </transition>
    </router-view>
    <GlobalFrame></GlobalFrame>
    <GlobalFooter></GlobalFooter>
  </section>
</template>
<script lang="ts" setup>
import { useRoute } from 'vue-router';
import GlobalFooter from '../page/GlobalFooter.vue';
import GlobalFrame from '../page/GlobalFrame.vue';
import useViewStore from '@/store/modules/view';
import { onMounted, watchEffect } from 'vue';


const route = useRoute()
const viewStore = useViewStore()

onMounted(() => {
  addIframe()
})

watchEffect(() => {
  if (route.meta.link) {
    viewStore.addIframeView(route)
  }
})

function addIframe() {
  if (route.meta.link) {
    viewStore.addIframeView(route)
  }
}

</script>


<style lang="scss" scoped>
.app-main {
  /* 50= navbar  50  */
  min-height: calc(100vh - 50px);
  width: 100%;
  position: relative;
  overflow: hidden;
}

.fixed-header+.app-main {
  overflow-y: auto;
  scrollbar-gutter: auto;
  height: calc(100vh - 50px);
  min-height: 0px;
}

.app-main:has(.copyright) {
  padding-bottom: 36px;
}

.fixed-header+.app-main {
  margin-top: 50px;
}

.hasTagsView {
  .app-main {
    /* 84 = navbar + tags-view = 50 + 34 */
    min-height: calc(100vh - 84px);
  }

  .fixed-header+.app-main {
    margin-top: 84px;
    height: calc(100vh - 84px);
    min-height: 0px;
  }
}
</style>

<style lang="scss">
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background-color: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background-color: #c0c0c0;
  border-radius: 3px;
}
</style>
