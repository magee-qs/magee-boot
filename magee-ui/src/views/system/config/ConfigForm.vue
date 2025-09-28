<template>
    <BaseForm ref="formRef" v-model="form" :title="title" :rules="rules" label-width="80px" height="280px"
        :add="addConfig" :update="editConfig" :init="init" :load="getConfig" @add="emit('add')"
        @update="emit('update')" @reset-form="resetForm">
        <el-row>
            <el-col :span="12">
                <el-form-item label="参数名称" prop="configName">
                    <el-input v-model="form.configName" placeholder="请输入参数名称" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="参数键名" prop="configKey">
                    <el-input v-model="form.configKey" placeholder="请输入参数键名" />
                </el-form-item>
            </el-col>
            <el-col :span="24">
                <el-form-item label="参数键值" prop="configValue">
                    <el-input v-model="form.configValue" type="textarea" placeholder="请输入参数键值" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="系统内置" prop="configType">
                    <el-radio-group v-model="form.configType">
                        <el-radio :value="1">是</el-radio>
                        <el-radio :value="0">否</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-col>
           
            <el-col :span="24">
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
                </el-form-item>
            </el-col>
        </el-row>
    </BaseForm>
</template>
<script lang="ts" setup>
import { addConfig, editConfig, getConfig } from '@/api/system/config'; 
import BaseForm from '@/components/form/BaseForm.vue';
import { ref } from 'vue';

const formRef = ref()
const form = ref(init())
const rules = ref({
    configName: [{ required: true, message: "参数名称不能为空", trigger: "blur" }],
    configKey: [{ required: true, message: "参数键名不能为空", trigger: "blur" }],
    configValue: [{ required: true, message: "参数键值不能为空", trigger: "blur" }]
})

const title = ref('系统参数')

defineExpose({ openForm })
const emit = defineEmits(['add', 'update'])

function openForm(configId) {
    formRef.value.openForm(configId)
}

function resetForm(){
    form.value = init()
}

function init() {
    return {
        configId: '',
        configName: '',
        configKey: '',
        configValue: '',
        configType: 0,
        remark: '',
        status: 1
    }
}
</script>