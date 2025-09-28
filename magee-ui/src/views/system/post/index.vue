<template>
    <div class="app-container">
        <el-form ref="queryRef" :model="queryParam" :inline="true" v-show="showSearch">
            <el-form-item label="岗位编码" prop="code">
                <el-input v-model="queryParam.code" placeholder="请输入岗位编码" />
            </el-form-item>
            <el-form-item label="岗位名称" prop="name">
                <el-input v-model="queryParam.name" placeholder="请输入岗位名称" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">查询</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="Plus" @click="handleAdd"
                    v-hasPerm="['system:post:add']">新增</el-button>
            </el-col>
            <ToolBar v-model:showSearch="showSearch" @queryTable="getList"></ToolBar>
        </el-row>

        <el-table v-loading="loading" :data="postList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="岗位编码" align="center" prop="code" />
            <el-table-column label="岗位名称" align="center" prop="name" />
            <el-table-column label="岗位排序" align="center" prop="sort" />
            <el-table-column label="状态" align="center" prop="status_dictText">
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" width="180">
                <template #default="scope">
                    <span>{{ scope.row.createTime }}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="260" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                        v-hasPerm="['system:post:edit']">修改</el-button>

                    <el-button type="primary" link icon="CircleCheck" v-hasPerm="['system:role:edit']"
                        @click="handleStatus(scope.row)">启用/禁用</el-button>

                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                        v-hasPerm="['system:post:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <Pagination v-show="total > 0" :total="total" v-model:page="queryParam.pageNum"
            v-model:limit="queryParam.pageSize" @pagination="getList()"></Pagination>

        <PostForm ref="formRef" @submit="handleQuery"></PostForm>
    </div>

</template>
<script lang="ts" setup>
import { changePostStatus, getPostList, removePost } from '@/api/system/post';
import Pagination from '@/components/table/Pagination.vue';
import { onMounted, reactive, ref } from 'vue';
import PostForm from './PostForm.vue';
import useMessage from '@/hooks/message';
import ToolBar from '@/components/table/ToolBar.vue';


const queryRef = ref()
const queryParam = reactive({
    pageNum: 1,
    pageSize: 10,
    name: '',
    code: ''
})
const showSearch = ref(true)
const postList = ref([])
const total = ref(0)
const loading = ref(false)
const ids = ref([])

const formRef = ref()


function getList() {
    getPostList(queryParam).then((res: any) => {
        postList.value = res.records
        total.value = res.total
    })
}
function handleQuery() {
    queryParam.pageNum = 1
    getList()
}
function resetQuery() {
    queryRef.value.resetFields()
}

function handleAdd() {
    formRef.value.openForm()
}

function handleUpdate(row) {
    formRef.value.openForm(row.postId)
}

function handleDelete(row) {
    removePost(row.postId).then(() => {
        useMessage.success()
        handleQuery()
    })
}

function handleStatus(row) {
    changePostStatus(row.postId).then(() => {
        useMessage.success()
        getList()
    })
}

function handleSelectionChange(selection) {
    ids.value = selection.map(item => item.postId)
}

onMounted(() => {
    getList()
})
</script>