<template>
    <el-scrollbar ref="scrollContainer" :vertical="false" class="scroll-container" @scroll="onScroll">
        <slot></slot>
    </el-scrollbar>
</template>
<script lang="ts" setup>
import useViewStore from '@/store/modules/view';
import type { ElScrollbar } from 'element-plus';
import { ref, computed } from 'vue';


const tagAndTagSpacing = ref(4)
const scrollContainer = ref<InstanceType<typeof ElScrollbar>>()

const viewStore = useViewStore()
const visitedViews = computed(() => viewStore.visitedViews)

function onScroll(e) {
    console.log(e.scrollLeft, e.scrollTop)
}

function moveToTarget(currentTag) {
    
}

defineExpose({
    moveToTarget,
})
</script>

<style lang='scss' scoped>
.scroll-container {
    white-space: nowrap;
    position: relative;
    overflow: hidden;
    width: 100%;

    :deep(.el-scrollbar__bar) {
        bottom: 0px;
    }

    :deep(.el-scrollbar__wrap) {
        height: 39px;
    }
}
</style>