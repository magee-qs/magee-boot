<template>
    <div class="app-container">
        <el-form :model="queryParam" ref="queryRef" v-show="showSearch" :inline="true" label-width="68px">
            <el-form-item label="角色名称" prop="name">
                <el-input v-model="queryParam.name" placeholder="请输入角色名称" clearable style="width: 240px"></el-input>
            </el-form-item>
            <el-form-item label="权限字符" prop="roleKey">
                <el-input v-model="queryParam.roleKey" placeholder="请输入权限字符" clearable style="width: 240px"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">查询</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPerm="'system:rol:add'">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="Plus" @click="handleDelete"
                    v-hasPerm="'system:rol:remove'">删除</el-button>
            </el-col>
            <ToolBar v-model:show-search="showSearch" @query-table="getList" :columns="columns"></ToolBar>
        </el-row>
        <!-- 表格数据 -->
        <el-table :data="roleList" v-loading="loading" stripe border @sort-change="handelSort">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="角色名称" prop="name" sortable :show-overflow-tooltip="true" width="150" />
            <el-table-column label="权限字符" prop="roleKey" sortable :show-overflow-tooltip="true" width="150" />
            <el-table-column label="显示顺序" prop="sort" sortable width="120" />
            <el-table-column label="状态" align="center" sortable width="100">
                <template #default="scope">
                    <el-switch v-model="scope.row.status" :active-value="1" :inactive-value="0"
                        @change="handleStatusChange(scope.row)"></el-switch>
                </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" sortable min-width="160">
                <template #default="scope">
                    <span>{{  scope.row.createTime }}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="360">
                <template #default="scope">

                    <el-button type="primary"  link icon="Edit" @click="handleUpdate(scope.row)"
                        v-hasPerm="['system:role:edit']">编辑</el-button>


                    <el-button type="danger" link icon="Delete" @click="handleDelete(scope.row)"
                        v-hasPerm="['system:role:remove']">删除</el-button>

                    <el-dropdown type="default" trigger="click" class="ml10">
                        <el-button type="primary" link icon="Notification">更多操作</el-button>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item>
                                    <el-button type="primary" link icon="CircleCheck" v-hasPerm="['system:role:edit']"
                                        @click="handlePermission(scope.row)">权限设置</el-button>
                                </el-dropdown-item>
                                <el-dropdown-item>
                                    <el-button type="primary" link icon="User" v-hasPerm="['system:role:edit']"
                                        @click="handleAuthUser(scope.row)" >分配用户</el-button>
                                </el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown> 
                </template>
            </el-table-column>
        </el-table>

        <Pagination :total="total" v-model:page="queryParam.pageNum" v-model:limit="queryParam.pageSize" @pagination="getList"></Pagination>

        <!-- 添加或修改角色 -->
        <RoleForm ref="roleForm"   @add="handleQuery" @update="getList"></RoleForm>

        <PermissionForm ref="permForm" :title="permTitle"></PermissionForm>
    </div>
</template>
<script lang="ts" setup>
import { changeRoleState, getRoleList, removeRole } from '@/api/system/role';
import ToolBar from '@/components/table/ToolBar.vue'; 
import { onMounted, reactive, ref } from 'vue';
import RoleForm from './RoleForm.vue';
import Pagination from '@/components/table/Pagination.vue';
import useTable from '@/hooks/useTable';
import PermissionForm from '@/components/perm/PermissionForm.vue';
import { useRouter } from 'vue-router';


const showSearch = ref(true)
const loading = ref(false)
const roleList = ref([])
const total = ref(0)
const title = ref('')
const permTitle = ref('角色权限配置')

const roleForm = ref()
const permForm = ref()
const router = useRouter()

const queryParam = reactive({
    pageNum: 1,
    pageSize: 10,
    name: undefined,
    roleKey: undefined,
    status: undefined,
    orders: [{
        column: 'roleId',
        asc: true
    }]
})

const columns = ref([])

function getList() {
    loading.value = true
    getRoleList(queryParam).then((res: any) => {
        roleList.value = res.records
        total.value = res.total
        loading.value = false
    }).catch(() => {
        loading.value = false
    })
}
/** 查询数据 */
function handleQuery() {
    queryParam.pageNum = 1
    getList()
}
/** 重置查询 */
function resetQuery() {

}
/** 添加角色 */
function handleAdd() {
    title.value = '添加角色'
    roleForm.value.openForm()
}
/** 删除角色 */
function handleDelete(row) {
    removeRole(row.roleId).then(() => {
        getList()
    })
}

/** 修改状态 */
function handleStatusChange(row) {
    changeRoleState(row.roleId).then(() => {
        getList()
    })
}
/** 修改角色 */
function handleUpdate(row) {
    title.value = '修改角色'
    roleForm.value.openForm(row.roleId)
}

/** 设置菜单权限 */
function handlePermission(row) {
    permTitle.value = '角色权限配置 - ' + row.name
    permForm.value.open(row.roleId, 'Role')
}

/** 设置角色用户 */
function handleAuthUser(row) {
    router.push('/system/role-auth/user/' + row.roleId)
}

function handelSort(rows) {
    queryParam.orders = useTable.getOrderColumn(rows)
    handleQuery()
}

onMounted(() => {
    getList()
})
</script>
<style lang="sass" scoped></style>