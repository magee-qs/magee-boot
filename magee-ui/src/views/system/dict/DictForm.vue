<template>
    <BaseForm ref="formRef" :title="title" v-model="form" :rules="rules" 
        :add="addDict" :update="editDict" :init="init" :load="getDict"
        @add="emit('add')" @update="emit('update')"  @reset-form="resetForm" >
         <el-col :span="12">
            <el-form-item label="字典编码" prop="dictCode">
                <el-input v-model="form.dictCode" placeholder="请输入字典编码"  :disabled="action == 'update'"/>
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="字典类型" prop="dictType">
                <el-input v-model="form.dictType" placeholder="请输入字典类型" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="数据标签" prop="dictLabel">
                <el-input v-model="form.dictLabel" placeholder="请输入数据标签" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="数据键值" prop="dictValue">
                <el-input v-model="form.dictValue" placeholder="请输入数据键值" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="样式属性" prop="cssClass">
                <el-input v-model="form.cssClass" placeholder="请输入样式属性" />
            </el-form-item>
        </el-col>
        <el-col :span="12">

            <el-form-item label="显示排序" prop="dictSort">
                <el-input-number v-model="form.dictSort" :controls="false" :min="0" align="left" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="回显样式" prop="listClass">
                <el-select v-model="form.listClass">
                    <el-option v-for="item in listClassOptions" :key="item.value"
                        :label="item.label + '(' + item.value + ')'" :value="item.value"></el-option>
                </el-select>
            </el-form-item>
        </el-col>

        <el-col :span="12">

            <el-form-item label="状态" prop="status">
                <StatusRadio v-model="form.status"></StatusRadio>
            </el-form-item>
        </el-col>

        <el-col :span="23">
            <el-form-item label="备注" prop="remark">
                <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
            </el-form-item>
        </el-col>


    </BaseForm>
</template>
<script lang="ts" setup>
import { addDict, editDict, getDict } from '@/api/system/dict';
import StatusRadio from '@/components/base/StatusRadio.vue';
import BaseForm from '@/components/form/BaseForm.vue'; 
import { ref } from 'vue';

const formRef = ref()
const form = ref(init())
const rules = ref({
    dictLabel: [{ required: true, message: "数据标签不能为空", trigger: "blur" }],
    dictValue: [{ required: true, message: "数据键值不能为空", trigger: "blur" }],
    dictSort: [{ required: true, message: "数据顺序不能为空", trigger: "blur" }]
})
const title = ref('字典')
// 数据标签回显样式
const listClassOptions = ref([
    { value: "default", label: "默认" },
    { value: "primary", label: "主要" },
    { value: "success", label: "成功" },
    { value: "info", label: "信息" },
    { value: "warning", label: "警告" },
    { value: "danger", label: "危险" }
])
const action = ref('add')
const emit = defineEmits(['add','update'])
 

defineExpose({ openForm })

function openForm(dictCode) {
    if(dictCode){
        title.value = '修改字典'
    }
    formRef.value.openForm(dictCode) 
}
function init() {
    return {
        dictType: '',
        dictCode: '',
        dictLabel: '',
        dictValue: '',
        cssClass: '',
        listClass: '',
        dictSort: 1,
        isDefault: 0,
        status: 1,
        remark: ''
    }
}

function resetForm(){
    form.value = init()
}
 
</script>