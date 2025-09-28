<template>
    <BaseForm v-model="form" :rules="rules" :title="title" width="600px" height="320px" label-width="80px" :add="addDepart"
        :update="updateDepart" :init="init" :load="getDepart" @add="emit('add')" @update="emit('update')"  @reset-form="resetForm" ref="formRef">
        <el-row>
            <el-col :span="24" v-if="form.parentId !== 0">
                <el-form-item label="上级部门" prop="parentId">
                    <DepartSelect v-model="form.parentId"></DepartSelect>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="部门名称" prop="name">
                    <el-input v-model="form.name" placeholder="请输入部门名称" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="部门编码" prop="code">
                    <el-input v-model="form.code" placeholder="请输入部门编码" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="显示排序" prop="orderNum">
                    <el-input-number v-model="form.orderNum" :controls="false" :min="0" align="left" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="负责人" prop="leader">
                    <el-input v-model="form.leader" placeholder="请输入负责人" maxlength="20" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="联系电话" prop="phone">
                    <el-input v-model="form.phone" placeholder="请输入联系电话" maxlength="11" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="部门状态">
                    <el-radio-group v-model="form.status">
                        <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :value="Number(dict.value)">{{
                            dict.label }}</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-col>
        </el-row>
    </BaseForm>
</template>
<script lang="ts" setup>
import { addDepart, getDepart, updateDepart } from '@/api/system/depart';
import BaseForm from '@/components/form/BaseForm.vue';
import DepartSelect from '@/components/system/DepartSelect.vue';
import useDictStore, { dictTypeConfig } from '@/store/modules/dict';
import { onMounted, reactive, ref } from 'vue';

const title = ref('部门')
const formRef = ref() 
const dictStore = useDictStore()
const sys_normal_disable = ref([])


onMounted(() => {
    // 加载字典
    dictStore.getDictData(dictTypeConfig.sys_normal_disable).then(data => {
        sys_normal_disable.value = data
    })
})

const form: any = ref(init())
const rules = reactive({
    parentId: [{ required: true, message: "上级部门不能为空", trigger: "blur" }],
    name: [{ required: true, message: "部门名称不能为空", trigger: "blur" }],
    code: [{ required: true, message: "部门编码不能为空", trigger: "blur" }],
    orderNum: [{ required: true, message: "显示排序不能为空", trigger: "blur" }],
    email: [{ type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] }],
    phone: [{ pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur" }]
})

function init() {
    return {
        parentId: '0',
        name: '',
        code: '',
        orderNum: 1,
        email: '',
        phone: '',
        status: 1,
        leader: ''
    }
}

const emit = defineEmits(['add', 'update'])


function openForm(departId, opp = 'add') { 
    if (opp === 'add') {
        // 新增
        if (departId) {
            form.value.parentId = departId 
            
        }
        formRef.value.openForm()
    } else if (opp === 'update') {
        // 修改
        formRef.value.openForm(departId)
    }
}

function resetForm(){
    form.value = init()
}

defineExpose({ openForm })
</script>