<template>
    <el-dialog v-model="visible" title="选择用户" width="800px" top="5vh" append-to-body>
        <el-form :model="queryParam" ref="queryRef" :inline="true">
            <el-form-item label="角色名" prop="name">
                <el-input v-model="queryParam.name" placeholder="请输入角色名" />
            </el-form-item>
            <el-form-item label="权限字符串" prop="roleKey">
                <el-input v-model="queryParam.roleKey" placeholder="权限字符串" />
            </el-form-item> 
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">查询</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>
        <el-row>
            <el-table :data="dataList" ref="tableRef" @selection-change="handleSelectionChange" height="260px">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column label="用户名称" prop="name" :show-overflow-tooltip="true" />
                <el-table-column label="用户昵称" prop="roleKey" :show-overflow-tooltip="true" />
                <el-table-column label="排序" prop="sort" :show-overflow-tooltip="true" />
                <el-table-column label="数据权限" prop="dataScope_dictText" />
                <el-table-column label="状态" align="center" prop="status_dcitText"> 
                </el-table-column>
                <el-table-column label="创建时间" align="center" prop="createTime" width="180">
                    <template #default="scope">
                        <span>{{ parseTime(scope.row.createTime) }}</span>
                    </template>
                </el-table-column>
            </el-table>
        </el-row>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="visible = false">取消</el-button>
                <el-button type="primary" @click="handleSubmit">确定</el-button>
            </div>
        </template>
    </el-dialog>
</template>
<script lang="ts" setup>
import { getRoleList} from '@/api/comm'; 
import { parseTime } from '@/utils';
import { reactive, ref } from 'vue'; 

 


const visible = ref(false)
const queryRef = ref()

const dataList = ref([])
const total = ref(0)
const ids = ref([])
const selectedRows = ref([]) 

const emit = defineEmits(['submit'])
const queryParam = reactive({
    name: '',
    roleKey: '' ,
    pageNum: 1,
    pageSize: 10
})

function getList() {
    getRoleList(queryParam).then((res: any) => {
        dataList.value = res.records
        total.value = res.total
    })
}



/** 查询事件 */
function handleQuery() {
    queryParam.pageNum = 1
    getList()
}

/** 重置查询 */
function resetQuery() {
    queryRef.value.resetFields()
}

/** 选中记录 */
function handleSelectionChange(selection) {
    ids.value = selection.map(item => item.roleId)
    selectedRows.value = selection
}

/** 提交数据 */
function handleSubmit() {
    // 提交返回的数据
    emit('submit', ids.value, selectedRows.value)
    visible.value = false
}

/** 打开对话框 */
function open() {
    getList() 
    visible.value = true
}

defineExpose({ open })
</script>