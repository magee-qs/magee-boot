<template>
    <div class="app-container">
        <el-form :model="queryParam" ref="queryRef" :inline="true" v-show="showSearch">
            <el-form-item label="标题" prop="title">
                <el-input v-model="queryParam.title" placeholder="请输入标题" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">查询</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>
        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="Plus" @click="handleAdd"
                    v-hasPerm="['system:notice:add']">新增</el-button>
            </el-col>
            <ToolBar v-model:show-search="showSearch" @queryTable="getList"></ToolBar>
        </el-row>
        <el-table :data="noticeList" v-loading="loading" @selection-change="handleSelectionChange">
            <el-table-column label="公告标题" align="center" prop="title" :show-overflow-tooltip="true" />
            <el-table-column label="公告类型" align="center" prop="noticeType_dictText" width="100" />
            <el-table-column label="状态" align="center" prop="status_dictText" width="100" />
            <el-table-column label="创建者" align="center" prop="createBy" width="100" />
            <el-table-column label="创建时间" align="center" prop="createTime" width="160" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
                <template #default="scope">
                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                        v-hasPerm="['system:notice:edit']">修改</el-button>
                    <el-button link type="primary" icon="Lock" @click="handleStatus(scope.row)"
                        v-hasPerm="['system:notice:edit']">{{ scope.row.status == 1 ? '关闭' : '启用' }}</el-button>
                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                        v-hasPerm="['system:notice:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <Pagination :total="total" v-model:page="queryParam.pageNum" v-model:limit="queryParam.pageSize"
            @pagination="getList">
        </Pagination>

        <NoticeForm ref="formRef" @add="handleQuery" @update="getList"></NoticeForm>
    </div>
</template>
<script lang="ts" setup>
import { changeNoticeStatus, getNoticeList, removeNotice } from '@/api/system/notice';
import Pagination from '@/components/table/Pagination.vue';
import ToolBar from '@/components/table/ToolBar.vue';
import { onMounted, ref } from 'vue';
import NoticeForm from './NoticeForm.vue';
import useMessage from '@/hooks/message';



const queryRef = ref()
const formRef = ref() 
const queryParam = ref({
    title: '',
    pageNum: 1,
    pageSize: 10
})
const showSearch = ref(true)
const total = ref(0)
const noticeList = ref([])
const loading = ref(false)
const ids = ref([])

onMounted(() => {
    getList()
})

function getList() {
    loading.value = true
    getNoticeList(queryParam.value).then((res: any) => {
        noticeList.value = res.records
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
}

function handleAdd() {
    formRef.value.openForm()
}

function handleUpdate(row) {
    formRef.value.openForm(row.noticeId)
}

function handleDelete(row) {
    removeNotice(row.noticeId).then(() => {
        useMessage.success()
        handleQuery()
    })
}

function handleStatus(row) {
    changeNoticeStatus(row.noticeId).then(() => {
        useMessage.success()
        getList()
    })
}

function handleSelectionChange(selection) {
    ids.value = selection.map(item => item.noticeId)
}

</script>