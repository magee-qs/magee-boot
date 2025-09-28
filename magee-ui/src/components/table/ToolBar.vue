<template>
  <div class="top-right-btn" :style="style">
    <el-row>
      <el-button-group>
        <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top" v-if="search">
          <el-button icon="Search" @click="toggleSearch()" />
        </el-tooltip>
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button icon="Refresh" @click="refresh()" />
        </el-tooltip>   
        <el-tooltip class="item" effect="dark" content="显隐列" placement="top" v-if="Object.keys(columns).length > 0"> 
          <el-dropdown class="fix-border" trigger="click" >
             <el-button icon="Menu"></el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <!-- 全选/反选 按钮 -->
                <el-dropdown-item>
                  <el-checkbox :indeterminate="isIndeterminate" v-model="isChecked" @change="toggleCheckAll"> 列展示
                  </el-checkbox>
                </el-dropdown-item>
                <div class="check-line"></div>
                <template v-for="(item, key) in columns" :key="item.key">
                  <el-dropdown-item>
                    <el-checkbox v-model="item.visible" @change="checkboxChange($event, key)" :label="item.label" />
                  </el-dropdown-item>
                </template>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-tooltip>
      </el-button-group>
    </el-row>
  </div>
</template>
<script lang="ts" setup>
import { computed } from 'vue'

const props = defineProps({
  /* 是否显示检索条件 */
  showSearch: {
    type: Boolean,
    default: true
  },
  /* 显隐列信息（数组格式、对象格式） */
  columns: {
    type: [Array, Object],
    default: () => ({})
  },
  /* 是否显示检索图标 */
  search: {
    type: Boolean,
    default: true
  },
  /* 右外边距 */
  gutter: {
    type: Number,
    default: 10
  },
})

const emits = defineEmits(['update:showSearch', 'queryTable'])
 
  
const style = computed(() => {
  const ret: any = {}
  if (props.gutter) {
    ret.marginRight = `${props.gutter / 2}px`
  }
  return ret
})

// 是否全选/半选 状态
const isChecked = computed({
  get: () => Array.isArray(props.columns) ? props.columns.every(col => col.visible) : Object.values(props.columns).every((col) => col.visible),
  set: () => { }
})

const isIndeterminate = computed(() => Array.isArray(props.columns) ? props.columns.some((col) => col.visible) && !isChecked.value : Object.values(props.columns).some((col) => col.visible) && !isChecked.value)

// 搜索
function toggleSearch() {
  emits("update:showSearch", !props.showSearch)
}

// 刷新
function refresh() {
  emits("queryTable")
}

  
// 单勾选
function checkboxChange(event, key) {
  if (Array.isArray(props.columns)) {
    props.columns.filter(item => item.key == key)[0].visible = event
  } else {
    props.columns[key].visible = event
  }
}

// 切换全选/反选
function toggleCheckAll() {
  const newValue = !isChecked.value
  if (Array.isArray(props.columns)) {
    props.columns.forEach((col: any) => (col.visible = newValue))
  } else {
    Object.values(props.columns).forEach((col) => (col.visible = newValue))
  }
}


</script>
<style lang='scss' scoped>
 
:deep(.el-dropdown-menu__item) {
  line-height: 30px;
  padding: 0 17px;
}

.check-line {
  width: 90%;
  height: 1px;
  background-color: #ccc;
  margin: 3px auto;
}

/* 把被 button-group 去掉的左边框补回来 */
:deep(.el-button-group .fix-border button.el-button) {
  border-left: 1px solid var(--el-border-color) !important;
}
</style>