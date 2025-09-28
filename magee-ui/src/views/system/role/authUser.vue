<template>
    <div class="app-container">
        <el-form :model="queryParam" ref="queryRef" v-show="showSearch" :inline="true">
            <el-form-item label="用户名称" prop="userName">
                <el-input v-model="queryParam.userName" placeholder="请输入用户名称" />
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
                <el-input v-model="queryParam.phone" placeholder="请输入手机号" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">查询</el-button>
                <el-button icon="Refresh" @click="handelReset">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="Plus" @click="openDialog"
                    v-hasPerm="['system:role:add']">添加用户</el-button>
            </el-col>
             <el-col :span="1.5">
            <el-button
               type="danger"
               plain
               icon="CircleClose"
               :disabled="multiple"
               @click="cancelAuthUserAll"
               v-hasPerm="['system:role:remove']"
            >批量取消授权</el-button>
         </el-col> 
        </el-row>
        
      <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column label="用户名称" prop="userName" :show-overflow-tooltip="true" />
         <el-table-column label="用户昵称" prop="nickName" :show-overflow-tooltip="true" />
         <el-table-column label="邮箱" prop="email" :show-overflow-tooltip="true" />
         <el-table-column label="手机" prop="phone" :show-overflow-tooltip="true" />
         <el-table-column label="状态" align="center" prop="status_dictText" /> 
         <el-table-column label="创建时间" align="center" prop="createTime" width="180">
            <template #default="scope">
               <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
               <el-button link type="primary" icon="CircleClose" @click="cancelAuthUser(scope.row)" v-hasPerm="['system:role:remove']">取消授权</el-button>
            </template>
         </el-table-column>
      </el-table>
      <UserModal ref="userModal" @submit="handleSubmit"></UserModal>
    </div>
</template>
<script lang="ts" setup>
import { getUserRoleList, removeUserRole, saveUserRole } from '@/api/system/role'; 
import UserModal from '@/components/system/UserModal.vue';
import useMessage from '@/hooks/message';  
import { parseTime } from '@/utils'; 
import { onMounted, reactive, ref } from 'vue';
import { useRoute } from 'vue-router';


const route = useRoute() 
const queryParam = reactive({
    userName: '',
    phone: '',
    pageNum: 1,
    pageSize: 10 ,
    roleId:  route.params.roleId
})

const showSearch = ref(true)
const loading = ref(false)
const userIds = ref([])
const userList = ref([])
const total = ref(0)
const multiple = ref(false)
const userModal = ref()


/** 查询数据 */
function getList(){
    getUserRoleList(queryParam).then((res: any)=>{
        userList.value = res.records
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
    userModal.value.open()
}
/** 取消角色 */
function cancelAuthUser(row){
    removeUserRole(queryParam.roleId, [row.userId]).then(() => {
        handleQuery()
    })
}
 

/** 选中记录 */
function handleSelectionChange(selection){
    userIds.value = selection.map(item => item.userId)
    multiple.value = !selection.length
}

/** 取消选中的记录 */
function cancelAuthUserAll(){
    if(userIds.value && userIds.value.length > 0){
        removeUserRole(queryParam.roleId, userIds.value).then(() => {
            handleQuery()
        })
    }else{
        useMessage.warning('没有选中任何记录')
    }
}

/** 选中记录 */
function handleSubmit(ids){
    saveUserRole(queryParam.roleId, ids).then(() => {
        handleQuery()
    })
}
 
onMounted(() => {
    getList()
})
</script>

<style lang="scss" scoped></style>