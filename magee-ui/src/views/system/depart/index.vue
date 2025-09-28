<template>
    <div class="app-container">
        <el-form ref="queryFormRef" :model="queryParam" :inline="true" v-show="showSearch" label-width="86px">
            <el-form-item label="部门名称" prop="name">
                <el-input v-model="queryParam.name" placeholder="请输入部门名称" clearable style="width: 200px" />
            </el-form-item>
            <el-form-item label="部门编码" prop="code">
                <el-input v-model="queryParam.code" placeholder="请输入部门名称" clearable style="width: 200px" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
                <el-select v-model="queryParam.status" placeholder="部门状态" clearable style="width: 200px">
                    <el-option v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.label"
                        :value="Number(dict.value)" />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">查询</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="Plus" @click="handleAdd"
                    v-hasPerm="['system:depart:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="primary" plain icon="Sort" @click="toggleExpandAll">展开/折叠</el-button>
            </el-col>
            <ToolBar v-model:showSearch="showSearch" @queryTable="getList"></ToolBar>
        </el-row>

        <el-table v-if="refreshTable" :data="departList" v-loading="loading" row-key="departId"
            :default-expand-all="expandAll" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
            <el-table-column prop="code" label="部门编码" width="160"></el-table-column>
            <el-table-column prop="name" label="部门名称" width="260"></el-table-column> 
            <el-table-column prop="leader" label="负责人" width="120"></el-table-column> 
            <el-table-column prop="orderNum" label="排序" width="200"></el-table-column>
            <el-table-column prop="status_dictText" label="状态" width="100"> 
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" width="200">
                <template #default="scope">
                    <span>{{  scope.row.createTime }}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" min-width="320">
                <template #default="scope">
                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                        v-hasPerm="['system:dept:edit']">修改</el-button>
                    <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)"
                        v-hasPerm="['system:dept:add']">新增</el-button>
                    <el-button v-if="scope.row.parentId != 0" link type="primary" icon="Delete"
                        @click="handleDelete(scope.row)" v-hasPerm="['system:dept:remove']">删除</el-button>
                </template>
            </el-table-column>
            <DepartForm ref="formRef" @add="handleQuery" @update="getList"></DepartForm>
        </el-table>
    </div>
</template>
<script lang="ts" setup>

import { getDepartList } from '@/api/system/depart';
import ToolBar from '@/components/table/ToolBar.vue';
import useDictStore, { dictTypeConfig } from '@/store/modules/dict';
import { handleTree } from '@/utils';
import { nextTick, onMounted, reactive, ref } from 'vue';
import DepartForm from './DepartForm.vue';

const dictStore = useDictStore()
const sys_normal_disable = ref([])
const loading = ref(false)
const expandAll = ref(false)

const departList = ref([])

const refreshTable = ref(true)
const showSearch = ref(true)

const queryFormRef = ref()
const formRef = ref()

const queryParam  = reactive({
    
    code: '',
    name: '',
    status: null
})

/** 查询部门数据 */
function getList() {
    loading.value = true
    getDepartList(queryParam).then(res => { 
        departList.value = handleTree(res.data || [], 'departId')
        loading.value = false
    })
}

/** 查询数据 */
function handleQuery() {
    getList()
}

/** 重置查询 */
function resetQuery() {
    queryFormRef.value.resetFields()
}

function toggleExpandAll() {
    refreshTable.value = false 
    expandAll.value = !expandAll.value
    nextTick(() => {
        refreshTable.value = true
    })
}

/** 添加部门 */
function handleAdd(row) { 
    let departId =  row?.departId
    formRef.value.openForm(departId, 'add')
}

/** 修改部门 */
function handleUpdate(row){
    let departId = row.departId
    formRef.value.openForm(departId,'update')
}
/** 删除部门  */
function handleDelete(row){

}

onMounted(() => {
    dictStore.getDictData(dictTypeConfig.sys_normal_disable).then(data => {
        sys_normal_disable.value = data
    })


    getList()
})

</script>