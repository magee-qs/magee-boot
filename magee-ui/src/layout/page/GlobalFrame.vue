<template>
     <GlobalFrame v-for="(item, index) in viewStore.iframeViews" :key="item.path" :iframeId="'iframe' + index"
          v-show="route.path === item.path" :src="iframeUrl(item.meta.link, item.query)"></GlobalFrame>
</template>
<script lang="ts" setup>
import useViewStore from '@/store/modules/view';
import { useRoute } from 'vue-router';

const route = useRoute()
const viewStore = useViewStore()

function iframeUrl(url, query) {
     if (Object.keys(query).length > 0) {
          let params = Object.keys(query).map((key) => key + "=" + query[key]).join("&")
          return url + "?" + params
     }
     return url
}
</script>