<template>
    <div class="app-container">
        <el-form :model="queryParam" ref="queryRef" v-show="showSearch" :inline="true">
            <el-form-item label="角色名" prop="name">
                <el-input v-model="queryParam.name" placeholder="请输入角色名" />
            </el-form-item>
            <el-form-item label="权限类别" prop="roleKey">
                <el-input v-model="queryParam.roleKey" placeholder="请输入权限类别" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">查询</el-button>
                <el-button icon="Refresh" @click="handelReset">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="Plus" @click="openDialog"
                    v-hasPerm="['system:user-role:add']">添加角色</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="CircleClose" :disabled="multiple" @click="cancelAuthRoleAll"
                    v-hasPerm="['system:user-role:remove']">批量取消授权</el-button>
            </el-col>
        </el-row>

        <el-table v-loading="loading" :data="roleList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="角色名" prop="name" :show-overflow-tooltip="true" />
            <el-table-column label="权限类型" prop="roleKey"   /> 
            <el-table-column label="数据权限" prop="dataScope_dictText"  />
            <el-table-column label="状态" align="center" prop="status_dictText" />
            <el-table-column label="创建时间" align="center" prop="createTime" width="180">
                <template #default="scope">
                    <span>{{ parseTime(scope.row.createTime) }}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button link type="primary" icon="CircleClose" @click="cancelAuthRole(scope.row)"
                        v-hasPerm="['system:user-role:remove']">取消授权</el-button>
                </template>
            </el-table-column>
        </el-table>
        <RoleModal ref="roleModal" @submit="handleSubmit"></RoleModal>
    </div>
</template>
<script lang="ts" setup>
import { getUserRoleList, removeUserRole, saveUserRole } from '@/api/system/user'
import RoleModal from '@/components/system/RoleModal.vue'
import useMessage from '@/hooks/message'
import { parseTime } from '@/utils'
import { onMounted, reactive, ref } from 'vue'
import { useRoute } from 'vue-router'

 


const route = useRoute() 
const queryParam = reactive({
    name: '',
    roleKey: '',
    pageNum: 1,
    pageSize: 10 ,
    userId:  route.params.userId
})

const showSearch = ref(true)
const loading = ref(false)
const roleIds = ref([])
const roleList = ref([])
const total = ref(0)
const multiple = ref(false)
const roleModal = ref()


/** 查询数据 */
function getList(){
    getUserRoleList(queryParam).then((res: any)=>{
        roleList.value = res.records
        total.value = res.total
    })
}

/** 查询数据 */
function handleQuery() {
    queryParam.pageNum = 1
    getList()
}
/** 重置查询条件 */
function handelReset() {

}
/** 添加用户 */
function openDialog(){
    roleModal.value.open()
}
/** 取消角色 */
function cancelAuthRole(row){
    removeUserRole(queryParam.userId, [row.roleId]).then(() => {
        handleQuery()
    })
}
 

/** 选中记录 */
function handleSelectionChange(selection){
    roleIds.value = selection.map(item => item.roleId)
    multiple.value = !selection.length
}

/** 取消选中的记录 */
function cancelAuthRoleAll(){
    if(roleIds.value && roleIds.value.length > 0){
        removeUserRole(queryParam.userId, roleIds.value).then(() => {
            handleQuery()
        })
    }else{
        useMessage.warning('没有选中任何记录')
    }
}

/** 选中记录 */
function handleSubmit(ids){
    saveUserRole(queryParam.userId, ids).then(() => {
        handleQuery()
    })
}
 
onMounted(() => {
    getList()
})
</script>