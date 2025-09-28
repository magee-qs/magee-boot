<template>
    <div class="app-container">
        <el-form ref="queryRef" :model="queryParam" :inline="true" v-show="showSearch">
                        <el-form-item label="任务名" prop="jobName">
                <el-input v-model="queryParam.jobName" placeholder="请输入任务名"  style="width: 200px"/>
            </el-form-item>
            <el-form-item label="任务分组" prop="jobGroup">
                <el-input v-model="queryParam.jobGroup" placeholder="请输入任务分组"  style="width: 200px"/>
            </el-form-item>
             <el-form-item label="任务日期" prop="name">
                  <el-date-picker
                        v-model="dateRange"  type="daterange"
                        range-separator="-"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期" 
                    />
             </el-form-item>
            <el-form-item label="执行状态" prop="status">
                <StatusRadio v-model="queryParam.status" true-label="成功" false-label="失败"></StatusRadio>
            </el-form-item>
            
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">查询</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="Plus" @click="handleAdd"
                           v-hasPerm="['monitor:jobLog:add']">新增</el-button>
            </el-col>
             <el-col :span="1.5">
                <el-button type="primary" plain icon="Plus" @click="handleBatchRemove"
                           v-hasPerm="['monitor:jobLog:remove']">批量删除</el-button>
            </el-col>
            <ToolBar v-model:showSearch="showSearch" @queryTable="getList"></ToolBar>
        </el-row>

        <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" /> 
                <el-table-column label="任务名" align="center" prop="jobName" />
                <el-table-column label="任务分组" align="center" prop="jobGroup" />
                <el-table-column label="目标字符串" align="center" prop="invokeTarget" />
                <el-table-column label="日志信息" align="center" prop="jobMessage" />
                <el-table-column label="执行状态" align="center" prop="status" />
                <el-table-column label="异常信息" align="center" prop="errorInfo" />
            <el-table-column label="操作" width="260" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                               v-hasPerm="['monitor:jobLog:edit']">修改</el-button>
                    <el-button link type="primary" icon="Lock" @click="handleStatus(scope.row)"
                               v-hasPerm="['monitor:jobLog:edit']">{{scope.row.status == 1 ? '禁用': '启用'}}</el-button>
                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                               v-hasPerm="['monitor:jobLog:remove']">删除</el-button> 
                </template>
            </el-table-column>
        </el-table>
        <Pagination v-show="total > 0" :total="total" v-model:page="queryParam.pageNum"
                    v-model:limit="queryParam.pageSize" @pagination="getList()"></Pagination>

        <SysJobLogForm ref="formRef" @add="handleQuery" @update="getList" :showFooter="showFooter"></SysJobLogForm>
    </div>

</template>
<script lang="ts" setup>
    import { changeSysJobLogStatus, getSysJobLogList, removeSysJobLog} from '@/api/monitor/jobLog';
    import Pagination from '@/components/table/Pagination.vue';
    import { onMounted, reactive, ref } from 'vue';
    import SysJobLogForm from './LogForm.vue';
    import useMessage from '@/hooks/message';
    import ToolBar from '@/components/table/ToolBar.vue';
import StatusRadio from '@/components/base/StatusRadio.vue';
import { addDateRange } from '@/utils';


    const queryRef = ref()
    const queryParam = ref(init())
    const showSearch = ref(true)
    const dataList = ref([])
    const total = ref(0)
    const loading = ref(false)
    const ids = ref([])
    const showFooter = ref(false)
    const formRef = ref()
    const dateRange =ref([])

    /** 表单初始化 */
    function init(){
        return {
            pageNum: 1,
            pageSize: 10,
            logId: null,
            jobName: '',
            jobGroup: '',
            invokeTarget: '',
            jobMessage: '',
            status: null,
            errorInfo: '',
        }
    }

    /** 查询数据 */
    function getList() {
        let query = addDateRange(queryParam.value, dateRange, 'createTime')
        getSysJobLogList(query).then((res: any) => {
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
        formRef.value.openForm(row.logId)
    }
    /** 删除数据 */
    function handleDelete(row) {
        useMessage.confirm('确定删除数据?').then(() => {
            removeSysJobLog(row.logId).then(() => {
                useMessage.success()
                handleQuery()
            })
        })
    }
    /** 修改状态 */
    function handleStatus(row) {
        let text = row.status == 1 ? '禁用' : '启用'
        useMessage.confirm('确定要' + text + '记录').then(() => {
            changeSysJobLogStatus(row.logId).then(() => {
                useMessage.success()
                getList()
            })
        })
    }
    /** 选中记录 */
    function handleSelectionChange(selection) {
        ids.value = selection.map(item => item.logId)
    }

    function handleBatchRemove(){

    }

    /** 加载初始化数据 */
    onMounted(() => {
        getList()
    })
</script>