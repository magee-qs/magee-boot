<template>
   <div class="app-container">
      <el-row :gutter="20">
         <splitpanes :horizontal="appStore.device === 'mobile'" class="default-theme">
            <pane size="84">
               <el-col>
                  <el-form ::model="queryParam" ref="queryRef" :inline="true" label-width="68px">
                     <el-form-item label="用户名" prop="userName">
                        <el-input v-model="queryParam.userName" placeholder="请输入用户名称" clearable style="width: 240px" />
                     </el-form-item>
                     <el-form-item label="手机号" prop="phone">
                        <el-input v-model="queryParam.phone" placeholder="请输入手机号码" clearable style="width: 240px" />
                     </el-form-item>
                     <el-form-item>
                        <el-button type="primary" icon="Search" @click="handleQuery">查询</el-button>
                     </el-form-item>
                  </el-form>

                  <el-row :gutter="10" class="mb8">
                     <el-col :span="1.5">
                        <el-button type="primary" plain icon="Plus" @click="handleAdd"
                           v-hasPerm="'system:user:add'">新增</el-button>
                     </el-col>
                     <el-col :span="1.5">
                        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete"
                           v-hasPerm="'system:user:remove'">删除</el-button>
                     </el-col>
                     <ToolBar v-model:show-search="showSearch" @query-table="handleQuery" :columns="columns"></ToolBar>
                  </el-row>
                  <el-table :data="userList" v-loading="loading" @selection-change="handleSelectionChange">
                     <el-table-column type="selection" width="50" align="center" /> 
                     <el-table-column label="用户名称" align="center" key="userName" prop="userName"
                        v-if="columns.userName.visible" :show-overflow-tooltip="true" />
                     <el-table-column label="用户昵称" align="center" key="nickName" prop="nickName"
                        v-if="columns.nickName.visible" :show-overflow-tooltip="true" />
                     <el-table-column label="部门" align="center" key="departName" prop="departName"
                        v-if="columns.departName.visible" :show-overflow-tooltip="true" />
                     <el-table-column label="手机号码" align="center" key="phone" prop="phone" v-if="columns.phone.visible"
                        width="120" />
                     <el-table-column label="状态" align="center" key="status">
                        <template #default="scope">
                           <el-switch v-model="scope.row.status" :active-value="1" :inactive-value="0"
                              @change="handleStatusChange(scope.row)"></el-switch>
                        </template>
                     </el-table-column>
                     <el-table-column label="创建时间" align="center" prop="createTime" v-if="columns.createTime.visible"
                        width="160">
                        <template #default="scope">
                           <span>{{ parseTime(scope.row.createTime) }}</span>
                        </template>
                     </el-table-column>
                     <el-table-column label="操作" align="center" width="360" class-name="small-padding fixed-width">
                        <template #default="scope">

                           <el-button type="primary" link icon="Edit" @click="handleUpdate(scope.row)"
                              v-hasPerm="['system:user:edit']">编辑</el-button>


                           <el-button type="danger" link icon="Delete" @click="handleDelete(scope.row)"
                              v-hasPerm="['system:user:remove']">删除</el-button>

                              
                           <el-dropdown type="default" trigger="click" class="ml10">
                              <el-button type="primary" icon="Notification" link>更多操作</el-button>
                              <template #dropdown>
                                 <el-dropdown-menu>
                                    <el-dropdown-item>
                                       <el-button type="primary" link icon="Key" @click="handleResetPwd(scope.row)"
                                          v-hasPerm="['system:user:resetPwd']">重置密码</el-button>

                                    </el-dropdown-item>
                                    <el-dropdown-item>
                                       <el-button type="primary" link icon="CircleCheck"
                                          @click="handleAuthRole(scope.row)"
                                          v-hasPerm="['system:user:edit']">分配角色</el-button>
                                    </el-dropdown-item>
                                 </el-dropdown-menu>
                              </template>
                           </el-dropdown>
                        </template>
                     </el-table-column>
                  </el-table>
                  <Pagination v-show="true" :total="total" v-model:page="queryParam.pageNum"
                     v-model:limit="queryParam.pageSize" @pagination="getList()"></Pagination>

               </el-col>
               <UserForm ref="userForm" @add="handleQuery" @update="getList"></UserForm>
            </pane>
         </splitpanes>
      </el-row>

   </div>
</template>
<script lang="ts" setup>
import { changeUserStatus, getUserList, removeUser, resetUserPwd } from "@/api/system/user";
import ToolBar from "@/components/table/ToolBar.vue";
import useAppStore from "@/store/modules/app";
import { addDateRange, parseTime } from "@/utils";
import { Splitpanes, Pane } from "splitpanes"
import { computed, onMounted, reactive, ref } from "vue";
import Pagination from "@/components/table/Pagination.vue";
import { useRouter } from "vue-router";
import useMessage from "@/hooks/message";
import UserForm from "./UserForm.vue";



const appStore = useAppStore()
const router = useRouter()

const queryParam = reactive({
   pageNum: 1,
   pageSize: 10,
   userName: undefined,
   phone: undefined,
   staus: undefined,
   departId: undefined
})
const loading = ref(false)
const dateRange = ref([])
const userList = ref([])
const total = ref(0)
const showSearch = ref(true)


const selectedKeys = ref([])
const selectedRows = ref([])

const userForm = ref()

const multiple = computed(() => selectedKeys.value.length > 1)

// 列显隐信息
const columns = ref({
   id: { label: '用户编号', visible: true },
   userName: { label: '用户名称', visible: true },
   nickName: { label: '用户昵称', visible: true },
   departName: { label: '部门', visible: true },
   phone: { label: '手机号码', visible: true },
   status: { label: '状态', visible: true },
   createTime: { label: '创建时间', visible: true }
})

/** 分页查询 */
function getList() {
   loading.value = true
   addDateRange(queryParam, dateRange, 'createTime')
   getUserList(queryParam).then(res => {
      loading.value = false
      userList.value = res.records
      total.value = res.total
   }).catch(() => {
      loading.value = false
   })
}

/** 查询数据 */
function handleQuery() {
   queryParam.pageNum = 1
   getList()
}
/** 选择记录 */
function handleSelectionChange(selection) {
   selectedKeys.value = selection.map(item => item.id)
   selectedRows.value = selection
}

/** 添加用户 */
function handleAdd() {
   userForm.value.openForm()
}
/** 编辑用户 */
function handleUpdate(row) {
    userForm.value.openForm(row.userId)
}
/** 删除用户 */
function handleDelete(row) {
   useMessage.confirm(`确定要删除当前用户?`).then(() => {
      removeUser(row.userId).then(() => {
         useMessage.success()
         getList()
      })
   })
}
/** 修改用户状态 */
function handleStatusChange(row) {
   useMessage.confirm('确定要修改当前用户状态码?').then(() => {
      changeUserStatus(row.userId).then(() => {
         useMessage.success()
         getList()
      })

   })
}
/** 重置密码 */
function handleResetPwd(row) {
   useMessage.confirm(`确定要重置用户密码?`).then(() => {
      resetUserPwd(row.userId).then(() => {
         useMessage.success()
         getList()
      })

   })
}
/** 设置角色 */
function handleAuthRole(row) {
   router.push('/system/user-auth/role/' + row.userId)
}


onMounted(() => {
   handleQuery()
})
</script>
