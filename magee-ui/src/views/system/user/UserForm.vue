<template>
    <BaseForm :title="title" v-model="form" :rules="rules" ref="formRef" height="320px"
        :add="addUser" :update="updateUser" :init="init" :load="getUserById" 
        @add="emit('add')" @update="emit('update')" @reset-form="resetForm">
        <el-col :span="12">
            <el-form-item label="账号" prop="userName">
                <el-input v-model="form.userName" placeholder="请输入用户账号" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="用户名" prop="nickName">
                <el-input v-model="form.nickName" placeholder="请输入用户名" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="部门" prop="departId">
                <DepartSelect v-model="form.departId"></DepartSelect>
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
                <el-input v-model="form.phone" placeholder="请输入手机号" maxlength="11" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
                <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="性别" prop="sex">
                <el-select v-model="form.sex" placeholder="请选择">
                    <el-option v-for="dict in sys_user_sex" :key="dict.value" :label="dict.label"
                        :value="Number(dict.value)"></el-option>
                </el-select>
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="状态" prop="status">
                <el-radio-group v-model="form.status">
                    <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :value="Number(dict.value)">{{
                        dict.label }}</el-radio>
                </el-radio-group>
            </el-form-item>
        </el-col>
    </BaseForm>
</template>
<script lang="ts" setup>
import { addUser, getUserById, updateUser } from '@/api/system/user';
import BaseForm from '@/components/form/BaseForm.vue';
import DepartSelect from '@/components/system/DepartSelect.vue'; 
import useDictStore, { dictTypeConfig } from '@/store/modules/dict';
import { onMounted, reactive, ref } from 'vue';

 
const formRef = ref()
const title = ref('用户')
const form = ref(init())
function init() {
    return {
        userName: '',
        nickName: '',
        departId: '',
        phone: '',
        email: '',
        sex: 0,
        status: 1
    }
}

const sys_user_sex = ref()
const sys_normal_disable = ref()

const rules = reactive({
    userName: [{ required: true, message: "用户名称不能为空", trigger: "blur" }, { min: 2, max: 20, message: "用户名称长度必须介于 2 和 20 之间", trigger: "blur" }],
    nickName: [{ required: true, message: "用户昵称不能为空", trigger: "blur" }],
    password: [{ required: true, message: "用户密码不能为空", trigger: "blur" }, { min: 5, max: 20, message: "用户密码长度必须介于 5 和 20 之间", trigger: "blur" }, { pattern: /^[^<>"'|\\]+$/, message: "不能包含非法字符：< > \" ' \\\ |", trigger: "blur" }],
    email: [{ type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] }],
    phonenumber: [{ pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur" }]
}
)
 
const emit = defineEmits(['add','update'])


onMounted(() => {
    useDictStore().getDictData(dictTypeConfig.sys_user_sex).then(data => {
        sys_user_sex.value = data
    })
    useDictStore().getDictData(dictTypeConfig.sys_normal_disable).then(data => {
        sys_normal_disable.value = data
    })
})
 
function openForm(userId) { 
    formRef.value.openForm(userId)
}

function resetForm(){
    form.value = init()
}

defineExpose({ openForm })
</script>