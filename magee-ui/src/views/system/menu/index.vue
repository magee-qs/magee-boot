<template>
    <div class="app-container">
        <el-form :model="queryParam" ref="queryRef" :inline="true" v-show="showSearch">
            <el-form-item label="菜单名称" prop="name">
                <el-input v-model="queryParam.name" placeholder="请输入菜单名称" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">查询</el-button>
                <el-button type="default" icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" icon="Plus" @click="handleAdd" v-hasPerm="['system.menyu.add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="default" icon="Sort" @click="toggleExpandAll">展开/折叠</el-button>
            </el-col>
        </el-row>
        <el-table v-if="showTable" v-loading="loading" :data="menuList" row-key="menuId" :default-expand-all="isExpand"
            :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
            <el-table-column prop="name" label="菜单名称" :show-overflow-tooltip="true" width="160"></el-table-column>
            <el-table-column prop="icon" label="图标" align="center" width="100">
                <template #default="scope">
                    <svg-icon :icon-class="scope.row.icon" />
                </template>
            </el-table-column>
            <el-table-column prop="orderNum" label="排序" width="60"></el-table-column>
            <el-table-column prop="perms" label="权限标识" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="component" label="组件路径" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="status_dictText" label="状态" width="80"> 
            </el-table-column>
            <el-table-column label="创建时间" align="center" width="160" prop="createTime">
                <template #default="scope">
                    <span>{{ parseTime(scope.row.createTime) }}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="210" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                        v-hasPerm="['system:menu:edit']">修改</el-button>
                    <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)"
                        v-hasPerm="['system:menu:add']">新增</el-button>
                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                        v-hasPerm="['system:menu:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <MenuForm ref="menuFormRef" @add="handleQuery" @update="getList"></MenuForm>
    </div>
</template>
<script lang="ts" setup>
import { getMenuList, removeMenu } from '@/api/system/menu';
import { handleTree, parseTime } from '@/utils';
import { nextTick, onMounted, reactive, ref } from 'vue';
import MenuForm from './MenuForm.vue';

const queryParam = reactive({
    name: ''
})

const queryRef = ref()
const menuFormRef = ref()

const showSearch = ref(true)
const loading = ref(false)
const menuList = ref([])
const isExpand = ref(false)
const showTable =ref(true)
const formTitle = ref('')

/** 查询菜单列表 */
function getList() {
    loading.value = true
    getMenuList().then((res) => {
        // 转换为树形结构
        menuList.value = handleTree(res.data, 'menuId', 'parentId', 'children')
        console.log(menuList.value)
        loading.value = false
    })
}

/** 查询数据 */
function handleQuery() {
    getList()
}

/** 重置查询 */
function resetQuery() {
    queryRef.value.resetFields()
}

/** 添加菜单 */
function handleAdd(row) { 
    let menuId = row?.menuId || null
    menuFormRef.value.openForm(menuId ,'add')
    formTitle.value = '新增菜单'
}

/** 菜单折叠/展开 */
function toggleExpandAll() {
    showTable.value = false
    isExpand.value = !isExpand.value
    nextTick(() => {
        showTable.value = true
    })
}
/** 修改菜单 */
function handleUpdate(row){
    menuFormRef.value.openForm(row.menuId, 'update')
    formTitle.value = '修改菜单'
}
/** 删除菜单 */
function handleDelete(row){
    removeMenu([row.menuId]).then(() => {
        handleQuery()
    })
}


onMounted(() => {
    getList()
})
</script>