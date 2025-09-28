<template>
    <div class="app-container">
        <el-form ref="queryRef" :model="queryParam" :inline="true" v-show="showSearch">
            <#list columns as column>
                <#if column.isKey == false>
            <el-form-item label="${column.comment}" prop="${column.fieldName}">
                <el-input v-model="queryParam.${column.fieldName}" placeholder="请输入${column.comment}"  style="width: 200px"/>
            </el-form-item>
                </#if>
            </#list>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">查询</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="Plus" @click="handleAdd"
                           v-hasPerm="['${perm}:add']">新增</el-button>
            </el-col>
            <ToolBar v-model:showSearch="showSearch" @queryTable="getList"></ToolBar>
        </el-row>

        <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <#list columns as column>
                <el-table-column label="${column.comment}" align="center" prop="${column.fieldName}" />
            </#list>
            <el-table-column label="操作" width="260" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                               v-hasPerm="['${perm}:edit']">修改</el-button>
                    <el-button link type="primary" icon="Lock" @click="handleStatus(scope.row)"
                               v-hasPerm="['${perm}:edit']">{{scope.row.status == 1 ? '禁用': '启用'}}</el-button>
                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                               v-hasPerm="['${perm}:remove']">删除</el-button>

                    <!-- 以下操作按钮根据需要保留 -->
                    <el-dropdown type="default" trigger="click" class="ml10">
                        <el-button type="primary" icon="Notification" link>更多操作</el-button>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item>
                                    <el-button type="primary" link icon="Key" @click="handleUpdate(scope.row)"
                                               v-hasPerm="['${perm}:add']">添加</el-button>

                                </el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </template>
            </el-table-column>
        </el-table>
        <Pagination v-show="total > 0" :total="total" v-model:page="queryParam.pageNum"
                    v-model:limit="queryParam.pageSize" @pagination="getList()"></Pagination>

        <${entityName}Form ref="formRef" @add="handleQuery" @update="getList" :showFooter="showFooter"></${entityName}Form>
    </div>

</template>
<script lang="ts" setup>
    import { change${entityName}Status, get${entityName}List, remove${entityName}} from '@/api${url}';
    import Pagination from '@/components/table/Pagination.vue';
    import { onMounted, reactive, ref } from 'vue';
    import ${entityName}Form from './${entityName}Form.vue';
    import useMessage from '@/hooks/message';
    import ToolBar from '@/components/table/ToolBar.vue';


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
    function init(){
        return {
            pageNum: 1,
            pageSize: 10,
            <#list columns as column>
               <#if column.dataType == 'varchar'>
            ${column.fieldName}: '',
               <#else>
            ${column.fieldName}: null,
                </#if>
            </#list>
        }
    }

    /** 查询数据 */
    function getList() {
        get${entityName}List(queryParam.value).then((res: any) => {
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
        formRef.value.openForm(row.${keyParam})
    }
    /** 删除数据 */
    function handleDelete(row) {
        useMessage.confirm('确定删除数据?').then(() => {
            remove${entityName}(row.${keyParam}).then(() => {
                useMessage.success()
                handleQuery()
            })
        })
    }
    /** 修改状态 */
    function handleStatus(row) {
        let text = row.status == 1 ? '禁用' : '启用'
        useMessage.confirm('确定要' + text + '记录').then(() => {
            change${entityName}Status(row.${keyParam}).then(() => {
                useMessage.success()
                getList()
            })
        })
    }
    /** 选中记录 */
    function handleSelectionChange(selection) {
        ids.value = selection.map(item => item.${keyParam})
    }

    /** 加载初始化数据 */
    onMounted(() => {
        getList()
    })
</script>