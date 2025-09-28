<template>
    <div v-if="!item.hidden">
        <template
            v-if="hasOneShowingChild(item.children, item) && (!onlyOneChild.children || onlyOneChild.noShowingChildren) && !item.alwaysShow">
            <MenuLink v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path, onlyOneChild.query)">
                <el-menu-item :index="resolvePath(onlyOneChild.path, null)" :class="{ 'submenu-title-noDropdown': !isNest }">
                    <svg-icon :icon-class="onlyOneChild.meta.icon || (item.meta && item.meta.icon)" />
                    <template #title><span class="menu-title" :title="hasTitle(onlyOneChild.meta.title)">{{
                        onlyOneChild.meta.title }}</span></template>
                </el-menu-item>
            </MenuLink>
        </template>

        <el-sub-menu v-else ref="subMenu" :index="resolvePath(item.path, null)" teleported>
            <template v-if="item.meta" #title>
                <svg-icon :icon-class="item.meta && item.meta.icon" />
                <span class="menu-title" :title="hasTitle(item.meta.title)">{{ item.meta.title }}</span>
            </template>

            <SideMenu v-for="(child, index) in item.children" :key="child.path + index" :is-nest="true"
                :item="child" :base-path="resolvePath(child.path, null)" class="nest-menu">
            </SideMenu>
        </el-sub-menu>
    </div>
</template>
<script lang="ts" setup>
import { getNormalPath } from '@/utils/index'
import { isExternal } from '@/utils/validate';
import { ref } from 'vue';
import MenuLink from './MenuLink.vue';
const props = defineProps({
    item: {
        type: Object,
        required: true
    },
    isNest: {
        type: Boolean,
        default: false
    },
    basePath: {
        type: String,
        default: ''
    }
})
const onlyOneChild: any = ref({})

function hasOneShowingChild(children = [], parent) {
    if (!children) {
        children = []
    }
    const showingChildren = children.filter(item => {
        if (item.hidden) {
            return false
        }
        onlyOneChild.value = item
        return true
    })

    // When there is only one child router, the child router is displayed by default
    if (showingChildren.length === 1) {
        return true
    }

    // Show parent if there are no child router to display
    if (showingChildren.length === 0) {
        onlyOneChild.value = { ...parent, path: '', noShowingChildren: true }
        return true
    }

    return false

}

function resolvePath(routePath, routeQuery) {
    if (isExternal(routePath)) {
        return routePath
    }
    if (isExternal(props.basePath)) {
        return props.basePath
    }
    if (routeQuery) {
        let query = JSON.parse(routeQuery)
        return { path: getNormalPath(props.basePath + '/' + routePath), query: query }
    }
    return getNormalPath(props.basePath + '/' + routePath)
}


function hasTitle(title) {
    if (title.length > 5) {
        return title
    } else {
        return ""
    }
}
</script>
