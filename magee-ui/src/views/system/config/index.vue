<template>
    <div class="app-container">
        <el-form :model="queryParam" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="参数名称" prop="configName">
                <el-input v-model="queryParam.configName" placeholder="请输入参数名称" clearable style="width: 240px" />
            </el-form-item>
            <el-form-item label="参数键名" prop="configKey">
                <el-input v-model="queryParam.configKey" placeholder="请输入参数键名" clearable style="width: 240px" />
            </el-form-item>
            <el-form-item label="系统内置" prop="configType">
                <el-radio-group v-model="queryParam.configType" placeholder="系统内置">
                    <el-radio :value="1">是</el-radio>
                    <el-radio :value="0">否</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="创建时间" style="width: 308px;">
                <el-date-picker v-model="dateRange" value-format="YYYY-MM-DD" type="daterange" range-separator="-"
                    start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>


        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5"><el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPerm="['system:config:add']">新增</el-button></el-col>
            <ToolBar v-model:showSearch="showSearch" @queryTable="getList"></ToolBar>
        </el-row>

         <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" /> 
         <el-table-column label="参数名称" align="center" prop="configName" :show-overflow-tooltip="true" />
         <el-table-column label="参数键名" align="center" prop="configKey" :show-overflow-tooltip="true" />
         <el-table-column label="参数键值" align="center" prop="configValue" :show-overflow-tooltip="true" />
         <el-table-column label="系统内置" align="center" prop="configType">
            <template #default="scope">
               <span>{{ scope.row.configType == 1 ? '是': '否' }}</span>
            </template>
         </el-table-column>
         <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
         <el-table-column label="创建时间" align="center" prop="createTime" width="180">
           
         </el-table-column>
         <el-table-column label="操作" align="center" width="220" class-name="small-padding fixed-width">
            <template #default="scope">
               <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPerm="['system:config:edit']" >修改</el-button> 
               <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPerm="['system:config:remove']">删除</el-button>
            </template>
         </el-table-column>
      </el-table>

        <Pagination v-model:page="queryParam.pageNum" v-model:list="queryParam.pageSize" :total="total" @pagination="getList"></Pagination>
        <ConfigForm ref="formRef" @add="handleQuery" @update="getList()"></ConfigForm>
    </div>
</template>
<script lang="ts" setup>
import {  getConfigList, removeConfig } from '@/api/system/config';
import Pagination from '@/components/table/Pagination.vue';
import ToolBar from '@/components/table/ToolBar.vue';
import { addDateRange } from '@/utils';
import { onMounted, ref } from 'vue';
import ConfigForm from './ConfigForm.vue';
import useMessage from '@/hooks/message';

const queryRef = ref()
const formRef = ref()
const queryParam = ref({
    pageNum: 1,
    pageSize: 10,
    configName: '',
    configKey: '',
    configType: undefined, 
})
const dateRange = ref([])
const showSearch = ref(true)
const loading = ref(false)
const total = ref(0)
const dataList =ref([])
const ids = ref([])

onMounted(() => {
    getList()
})

function getList() {
    loading.value = true
    addDateRange(queryParam.value, dateRange.value,'createTime')
    getConfigList(queryParam.value).then((res : any) => {
        dataList.value = res.records
        total.value = res.total
        loading.value = false
    })
}
function handleQuery() {
    queryParam.value.pageNum = 1
    getList()
}
function resetQuery() {
    queryRef.value.resetFields()
    dateRange.value = []
}
function handleAdd(){
    formRef.value.openForm()
}

function handleUpdate(row){
    formRef.value.openForm(row.configId)
}

function handleDelete(row){
    removeConfig(row.configId).then(() => {
        useMessage.success()
        getList()
    })
}

function handleStatus(row){
    changeConfigStatus(row.configId).then(() => {
        useMessage.success()
        getList()
    })
}

function handleSelectionChange(selection){
    ids.value = selection.map(item => item.configId)
}
</script>