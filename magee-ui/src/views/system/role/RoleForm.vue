<template>
    <BaseForm v-model="form" :rules="rules" ref="formRef" :title="title" :add="addRole" :update="updateRole"
        :init="init" :load="getRole" @add="emit('add')" @update="emit('update')" @reset-form="resetForm" height="280px">
        <el-row>
            <el-col :span="12">
                <el-form-item label="角色名" prop="name">
                    <el-input v-model="form.name" placeholder="请输入角色名称" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="权限类型" prop="roleKey">
                    <el-input v-model="form.roleKey" placeholder="请输入权限字符" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="排序" prop="sort">
                    <el-input-number v-model="form.sort" :min="1" :controls="false" align="left" style="width: 100%;" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="状态" prop="status">
                    <el-radio-group v-model="form.status">
                        <el-radio :value="1">启用</el-radio>
                        <el-radio :value="0">停用</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="数据权限" prop="dataScope">
                    <DictSelect v-model="form.dataScope" dict-type="sys_data_scope"></DictSelect>
                </el-form-item>
            </el-col>
        </el-row>





    </BaseForm>
</template>
<script lang="ts" setup>
import { addRole, getRole, updateRole } from '@/api/system/role';
import BaseForm from '@/components/form/BaseForm.vue';
import DictSelect from '@/components/system/DictSelect.vue';
import { reactive, ref } from 'vue';

defineProps({
    title: {
        type: String,
        default: '表单'
    }
})
const title = ref('角色')
const formRef = ref()
const form = ref({
    name: '',
    roleKey: '',
    sort: undefined,
    status: 1,
    dataScope: ''
})

function init() {
    return {
        name: '',
        roleKey: '',
        sort: 1,
        status: 1,
        dataScope: '1'
    }
}
const rules = reactive({
    name: [{ required: true, message: "角色名称不能为空", trigger: "blur" }],
    roleKey: [{ required: true, message: "权限字符不能为空", trigger: "blur" }],
    sort: [{ required: true, message: "角色顺序不能为空", trigger: "blur" }],
    dataScope: [{ required: true, message: "数据权限不能为空", trigger: "blur" }]
})
const emit = defineEmits(['add', 'update'])

function openForm(roleId) {
    formRef.value.openForm(roleId)
}

function resetForm(){
    form.value = init()
}

defineExpose({ openForm })

</script>