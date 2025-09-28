<template>
    <div class="app-container">
        <el-form ref="queryRef" :model="queryParam" :inline="true" v-show="showSearch">
            <el-form-item label="任务名称" prop="jobName">
                <el-input v-model="queryParam.jobName" placeholder="请输入任务名称" style="width: 200px" />
            </el-form-item>

            <el-form-item label="任务组" prop="clazz">
                <el-input v-model="queryParam.clazz" placeholder="请输入任务类" style="width: 200px" />
            </el-form-item>

            <el-form-item label="状态" prop="status">
                <el-select v-model="queryParam.status" placeholder="请选择" style="width: 200px">
                    <el-option label="默认" :value="0" />
                    <el-option label="立刻执行" :value="1" />
                    <el-option label="执行一次" :value="2" />
                    <el-option label="停止执行" :value="3" />
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
                    v-hasPerm="['monitor:job:add']">新增</el-button>
            </el-col>
            <ToolBar v-model:showSearch="showSearch" @queryTable="getList"></ToolBar>
        </el-row>

        <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="任务名称" align="center" prop="jobName" />
            <el-table-column label="任务组" align="center" prop="jobGroup" />
            <el-table-column label="任务类" align="center" prop="clazz" min-width="160" :show-overflow-tooltip="true" />
            <el-table-column label="执行方法" align="center" prop="method" min-width="120" :show-overflow-tooltip="true" />
            <el-table-column label="参数" align="center" prop="param" min-width="120" :show-overflow-tooltip="true"/>
            <el-table-column label="表达式" align="center" prop="cron" width="120"/>
            <el-table-column label="策略" align="center" prop="policy">
                <template #default="scope">
                    {{ policyState(scope.row) }}
                </template>
            </el-table-column>
            <el-table-column label="状态" align="center" prop="status_dictText" /> 
            <el-table-column label="运行状态" align="center" prop="running">
                <template #default="scope">
                    {{ runingState(scope.row) }}
                </template>
            </el-table-column> 
            <el-table-column label="操作" width="360" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                        v-hasPerm="['monitor:job:edit']">修改</el-button>

                    <el-button link type="primary" icon="Lock" @click="handleStatus(scope.row)"
                        v-hasPerm="['monitor:job:edit']">{{ scope.row.status == 1 ? '禁用' : '启用' }}</el-button>
                    <el-dropdown type="default" trigger="click" class="ml10">
                        <el-button type="primary" icon="Notification" link>定时任务操作</el-button>
                        <template #dropdown>
                            <el-dropdown-menu>
                                 <el-dropdown-item>
                                    <el-button link type="primary" @click="handleStartJob(scope.row)"
                                        v-hasPerm="['monitor:job:edit']">
                                        <el-icon> <VideoPlay />  </el-icon>启动任务</el-button>
                                </el-dropdown-item>
                                <el-dropdown-item>
                                    <el-button link type="primary" @click="handleRunJob(scope.row)"
                                        v-hasPerm="['monitor:job:edit']">
                                        <el-icon> <VideoPlay /></el-icon>启动一次</el-button>
                                </el-dropdown-item>
                                <el-dropdown-item>
                                    <el-button link type="primary" @click="handlePauseJob(scope.row)"  v-hasPerm="['monitor:job:edit']"  >
                                        <el-icon> <VideoPause /> </el-icon>暂停任务</el-button>
                                </el-dropdown-item>
                                <el-dropdown-item>
                                    <el-button link type="primary" @click="handleResumeJob(scope.row)"
                                        v-hasPerm="['monitor:job:edit']">
                                        <el-icon>
                                            <RefreshLeft />
                                        </el-icon>恢复任务</el-button>
                                </el-dropdown-item>
                                <el-dropdown-item>
                                    <el-button link type="primary" @click="handleRemoveJob(scope.row)"
                                        v-hasPerm="['monitor:job:edit']">
                                        <el-icon>
                                            <Remove />
                                        </el-icon>删除任务</el-button>
                                </el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>


                </template>
            </el-table-column>
        </el-table>
        <Pagination v-show="total > 0" :total="total" v-model:page="queryParam.pageNum"
            v-model:limit="queryParam.pageSize" @pagination="getList()"></Pagination>

        <SysJobForm ref="formRef" @add="handleQuery" @update="getList" :showFooter="showFooter"></SysJobForm>
    </div>

</template>
<script lang="ts" setup>
import { changeSysJobStatus, getSysJobList, removeJob, removeSysJob, resumeJob,pauseJob ,startJob, runJob } from '@/api/monitor/job';
import Pagination from '@/components/table/Pagination.vue';
import { computed, onMounted, reactive, ref } from 'vue';
import SysJobForm from './JobForm.vue';
import useMessage from '@/hooks/message';
import ToolBar from '@/components/table/ToolBar.vue';
import StatusRadio from '@/components/base/StatusRadio.vue';
 


const queryRef = ref()
const queryParam = ref(init())
const showSearch = ref(true)
const dataList = ref([])
const total = ref(0)
const loading = ref(false)
const ids = ref([])
const showFooter = ref(true)
const formRef = ref()

/** 表单初始化 */
function init() {
    return {
        pageNum: 1,
        pageSize: 10,
        jobId: null,
        jobName: '',
        jobGroup: '',
        clazz: '',
        method: '',
        param: '',
        cron: '',
        policy: '',
        status: null,
    }
}

/** 查询数据 */
function getList() {
    getSysJobList(queryParam.value).then((res: any) => {
        dataList.value = res.records
        total.value = res.total
    })
}
/** 查询 */
function handleQuery() {
    queryParam.value.pageNum = 1
    getList()
}
/** 重置表单 */
function resetQuery() {
    queryParam.value = init()
}
/** 添加表单 */
function handleAdd() {
    formRef.value.openForm()
}
/** 更新表单 */
function handleUpdate(row) {
    formRef.value.openForm(row.jobId)
}
/** 删除数据 */
function handleDelete(row) {
    useMessage.confirm('确定删除数据?').then(() => {
        removeSysJob(row.jobId).then(() => {
            useMessage.success()
            handleQuery()
        })
    })
}
/** 修改状态 */
function handleStatus(row) {
    let text = row.status == 1 ? '禁用' : '启用'
    useMessage.confirm('确定要' + text + '记录').then(() => {
        changeSysJobStatus(row.jobId).then(() => {
            useMessage.success()
            getList()
        })
    })
}
/** 选中记录 */
function handleSelectionChange(selection) {
    ids.value = selection.map(item => item.jobId)
}

function policyState(row: any) {
    let policy = row.policy
    if (policy == 1) {
        return '立刻执行'
    } else if (policy == 2) {
        return '执行一次'
    } else if (policy == 3) {
        return ' 放弃'
    } else {
        return '默认'
    }
}


function jobState(row: any) {
    let status = row.status
    if (status == 0) {
        return '待启动'
    } else if (status == 1) {
        return '运行中'
    } else if (status == 2) {
        return '暂停'
    } else {
        return ''
    }
}

function runingState(row: any){
    let status = row.running
    if(status == 0){
        return '未启动'
    }else if(status == 1){
        return '运行中'
    }else if(status == 2){
        return '暂停'
    }else{
        return ''
    }
}


function handleRunJob(row) {
    runJob(row.jobId).then(() => {
        useMessage.success('启动成功')
        getList()
    })
}

function handleResumeJob(row) {
    resumeJob(row.jobId).then(() => {
        useMessage.success('恢复任务成功')
        getList()
    })
}


function handlePauseJob(row) {
    pauseJob(row.jobId).then(() => {
        useMessage.success('暂停任务成功')
        getList()
    })
}

function handleRemoveJob(row) {
    removeJob(row.jobId).then(() => {
        useMessage.success('暂停任务成功')
        getList()
    })
}

function handleStartJob(row){
    startJob(row.jobId).then(() => {
        useMessage.success("启动任务成功")
        getList()
    })
}

/** 加载初始化数据 */
onMounted(() => {
    getList()
})
</script>