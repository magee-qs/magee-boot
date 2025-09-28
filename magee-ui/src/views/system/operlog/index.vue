<template>
    <div class="app-container">
        <el-form :model="queryParam" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="操作地址" prop="operateIp">
                <el-input v-model="queryParam.operateIp" placeholder="请输入操作地址" clearable style="width: 240px;" />
            </el-form-item>
            <el-form-item label="系统模块" prop="title">
                <el-input v-model="queryParam.title" placeholder="请输入系统模块" clearable style="width: 240px;" />
            </el-form-item>
            <el-form-item label="操作人" prop="operateName">
                <el-input v-model="queryParam.operateName" placeholder="请输入操作人" clearable style="width: 240px;" />
            </el-form-item>
            <el-form-item label="操作时间" style="width: 308px">
                <el-date-picker v-model="dateRange" value-format="YYYY-MM-DD HH:mm:ss" type="daterange"
                    range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"
                    :default-time="[new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 59)]"></el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">查询</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5"><el-button type="danger" plain icon="Delete" @click="handleDelete"
                    v-hasPerm="['monitor:operlog:remove']">删除</el-button></el-col>
        </el-row>

        <el-table :data="logList" v-loading="loading" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column label="日志编号" align="center" prop="logId" width="190" />
            <el-table-column label="系统模块" align="center" prop="title" width="160" :show-overflow-tooltip="true" />
            <el-table-column label="请求地址" prop="url" :show-overflow-tooltip="true" min-width="240" />
            <el-table-column label="控制器" prop="controller" :show-overflow-tooltip="true"  min-width="360"/>
            <el-table-column label="操作类型" align="center" prop="businessType_dictText"/> 
            <el-table-column label="操作人员" align="center" width="110" prop="operateName"  />
            <el-table-column label="操作时间" align="center" width="180" prop="operateTime" />
            <el-table-column label="操作地址" align="center" prop="operateIp" width="130"  />
            <el-table-column label="操作状态" align="center" prop="status_dictText" />
            <el-table-column label="耗时(ms)" align="center" prop="costTime" />
            <el-table-column label="错误消息" prop="errorMsg" />
        </el-table>
        <Pagination :total="total" v-model:limit="queryParam.pageSize" v-model:page="queryParam.pageNum" @pagination="getList" ></Pagination>
    </div>
</template>
<script lang="ts" setup>
import { getLogList, removeLog } from '@/api/system/log';
import Pagination from '@/components/table/Pagination.vue';
import useMessage from '@/hooks/message';
import { onMounted, ref } from 'vue';
 

const queryRef = ref()

const queryParam = ref({
    operateIp: '',
    title: '',
    operateName: '',
    pageNum: 1,
    pageSize: 10,
    orders: [{ column: 'operateTime', asc: false }]
})
const dateRange = ref([])
const showSearch = ref(true)
const logList = ref([])
const total = ref(0)
const ids = ref([])
const loading = ref(false)


onMounted(() => {
    getList()
})

function getList() {
    getLogList(queryParam.value).then((res: any) => {
        logList.value = res.records
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

function handleDelete() {
    if (!ids.value || ids.value.length == 0) {
        useMessage.warning('没有选中记录')
        return
    }
    removeLog(ids.value).then(() => {
        useMessage.success()
        getList()
    })
}

function handleSelectionChange(selection) {
    ids.value = selection.map(item => item.logId)
}
</script>