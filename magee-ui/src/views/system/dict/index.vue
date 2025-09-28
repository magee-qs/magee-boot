<template>
    <div class="app-container">
        <el-form ref="queryRef" :model="queryParam" :inline="true" v-show="showSearch">
            <el-form-item label="字典名称" prop="dictLabel">
                <el-input v-model="queryParam.dictLabel" />
            </el-form-item>
            <el-form-item label="字典类型" prop="dictType">
                <el-input v-model="queryParam.dictType" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="Plus" @click="handleAdd"
                    v-hasPerm="['system:dict:add']">新增</el-button> 
            </el-col>
             <ToolBar v-model="showSearch" @query-table="getList"></ToolBar>
        </el-row>
        <el-table :data="dataList" v-loading="loading" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="字典编码" align="center" prop="dictCode" />
            <el-table-column label="字典标签" align="center" prop="dictLabel" width="160" />
            <el-table-column label="字典键值" align="center" prop="dictValue" />
            <el-table-column label="字典类型" align="center" prop="dictType" width="160"/>
            <el-table-column label="字典排序" align="center" prop="dictSort" />
            <el-table-column label="状态" align="center" prop="status">
                <template #default="scope">{{ scope.row.status == 1 ? '正常': '停用' }}</template>
            </el-table-column>
            <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
            <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
            <el-table-column label="操作" align="center" width="220" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                        v-hasPerm="['system:dict:edit']">修改</el-button>
                    <el-button link type="primary" icon="Lock" @click="handleStatus(scope.row)"
                        v-hasPerm="['system:dict:edit']">{{ parseStatus(scope.row.status) }}</el-button>
                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                        v-hasPerm="['system:dict:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <Pagination v-model:limit="queryParam.pageSize" v-model:page="queryParam.pageNum" :total="total" @pagination="getList" ></Pagination>
   
        <DictForm ref="formRef" @add="handleQuery" @update="getList"></DictForm>
   
   </div>
</template>
<script lang="ts" setup>
import { changeDictStatus, getDictLsit, removeDict } from '@/api/system/dict';
import Pagination from '@/components/table/Pagination.vue';
import ToolBar from '@/components/table/ToolBar.vue';
import useMessage from '@/hooks/message';
import { parseStatus } from '@/utils';
import { onMounted, ref } from 'vue'; 
import DictForm from './DictForm.vue';

const queryRef = ref()
const queryParam = ref({
    pageNum: 1,
    pageSize: 10,
    dictType: '',
    dictLabel: '',
    orders: [
        { column: 'dictType,dictSort', asc: true } 
    ]
})
const showSearch = ref(true)
const dataList = ref([])
const total = ref(0)
const loading = ref(false)
const ids = ref([])

const formRef = ref()

function getList() {
    getDictLsit(queryParam.value).then((res: any) => {
        dataList.value = res.records
        total.value = res.total
    })
}

function handleQuery() {
    queryParam.value.pageNum = 1
    getList()
}

function resetQuery() {
    queryRef.value.resetFields()
}

function handleAdd() {
    formRef.value.openForm()
}

function handleUpdate(row) {
    formRef.value.openForm(row.dictCode)
}

function handleStatus(row) {
    changeDictStatus(row.dictCode).then(() => {
        useMessage.success()
        getList()
    })
}

function handleDelete(row) {
    removeDict(row.dictCode).then(() => {
        useMessage.success()
        getList()
    })
}

function handleSelectionChange(selection) {
    ids.value = selection.map(item => item.dictCode)
}

onMounted(() => {
    getList()
})
</script>