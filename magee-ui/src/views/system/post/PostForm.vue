<template>
    <BaseForm v-model="form" :title="title" :rules="rules" ref="formRef" 
       :add="addPost" :update="updatePost" :load="getPost" :init="init" @add="emit('add')"  @update="emit('update')" @reset-form="resetForm" height="160px">
        <el-col :span="12">
            <el-form-item label="岗位编码" prop="code">
                <el-input v-model="form.code" placeholder="请输入岗位编码" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="岗位名称" prop="name">
                <el-input v-model="form.name" placeholder="请输入岗位名称" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="岗位状态" prop="status">
                <StatusRadio v-model="form.status"></StatusRadio>
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="排序" prop="sort">
                <el-input-number v-model="form.sort" :min="1" :controls="false" align="left" style="width: 100%;" />
            </el-form-item>
        </el-col>
    </BaseForm>
</template>
<script lang="ts" setup>
import { addPost, getPost, updatePost } from '@/api/system/post';
import StatusRadio from '@/components/base/StatusRadio.vue';
import BaseForm from '@/components/form/BaseForm.vue'; 
import { ref } from 'vue';

const form = ref(init())

function init() {
    return {
        code: '',
        name: '',
        sort: 1,
        status: 1
    }
}
const formRef = ref()
const title = ref('岗位')
const rules = {
    name: [{ required: true, message: "岗位名称不能为空", trigger: "blur" }],
    code: [{ required: true, message: "岗位编码不能为空", trigger: "blur" }],
    sort: [{ required: true, message: "岗位顺序不能为空", trigger: "blur" }],
}



defineExpose({ openForm })

function openForm(postId) {
    formRef.value.openForm(postId) 
}

function resetForm(){
    form.value = init()
}

const emit = defineEmits(['add','update']) 
</script>