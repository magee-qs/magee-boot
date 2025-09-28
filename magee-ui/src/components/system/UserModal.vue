<template>
    <el-dialog v-model="visible" title="选择用户" width="800px" top="5vh" append-to-body>
        <el-form :model="queryParam" ref="queryRef" :inline="true">
            <el-form-item label="账号" prop="usreName">
                <el-input v-model="queryParam.userName" placeholder="请输入账号" />
            </el-form-item>
            <el-form-item label="用户名" prop="nickName">
                <el-input v-model="queryParam.nickName" placeholder="请输入用户名" />
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
                <el-input v-model="queryParam.phone" placeholder="请输入手机号" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">查询</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>
        <el-row>
            <el-table :data="userList" ref="tableRef" @selection-change="handleSelectionChange" height="260px">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column label="用户名称" prop="userName" :show-overflow-tooltip="true" />
                <el-table-column label="用户昵称" prop="nickName" :show-overflow-tooltip="true" />
                <el-table-column label="邮箱" prop="email" :show-overflow-tooltip="true" />
                <el-table-column label="手机" prop="phone" :show-overflow-tooltip="true" />
                <el-table-column label="状态" align="center" prop="status_dictText">
                </el-table-column>
                <el-table-column label="创建时间" align="center" prop="createTime" width="180">
                    <template #default="scope">
                        <span>{{ scope.row.createTime }}</span>
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
import { getUserList } from '@/api/comm'; 
import { reactive, ref } from 'vue';


const props = defineProps({
    multiple: {
        type: Boolean,
        default: true
    }
})

const visible = ref(false)
const queryRef = ref()

const userList = ref([])
const total = ref(0)
const userIds = ref([])
const selectedRows = ref([])

const emit = defineEmits(['submit'])
const queryParam = reactive({
    userName: '',
    nickName: '',
    phone: '',
    pageNum: 1,
    pageSize: 10
})

function getList() {
    getUserList(queryParam).then((res: any) => {
        userList.value = res.records
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
    userIds.value = selection.map(item => item.userId)
    selectedRows.value = selection
}

/** 提交数据 */
function handleSubmit() {
    // 提交返回的数据
    if (props.multiple) {
        emit('submit', userIds.value, selectedRows.value)
    } else {
        emit('submit', (userIds.value && userIds.value.length > 0) ? userIds.value[0] : null,
            selectedRows.value && selectedRows.value.length > 0 ? selectedRows.value[0] : null)
    }

    visible.value = false
}

/** 打开对话框 */
function open() {
    getList()
    visible.value = true
}

defineExpose({ open })
</script>