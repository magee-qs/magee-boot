<template>
    <BaseForm v-model="form" :rules="rules" ref="formRef" :title="title" :add="addNotice" :update="editNotice"
        :init="init" :load="getNotice" @add="emit('add')" @update="emit('update')" @reset-form="resetForm">
        <el-row>
            <el-col :span="12">
                <el-form-item label="公告标题" prop="title">
                    <el-input v-model="form.title" placeholder="请输入公告标题" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="公告类型" prop="noticeType">
                    <el-select v-model="form.noticeType" placeholder="请选择">
                        <el-option v-for="dict in sys_notice_type" :key="dict.value" :label="dict.label"
                            :value="Number(dict.value)"></el-option>
                    </el-select>
                </el-form-item>
            </el-col>
            <el-col :span="24">
                <el-form-item label="状态">
                    <el-radio-group v-model="form.status">
                        <el-radio v-for="dict in sys_notice_status" :key="dict.value" :value="Number(dict.value)">{{ dict.label
                        }}</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-col>
            <el-col :span="24">
                <el-form-item label="内容"> 
                    <Editor v-model="form.content" :min-height="192"></Editor> 
                </el-form-item>
            </el-col>
        </el-row>
    </BaseForm>
</template>
<script lang="ts" setup>
import { addNotice, editNotice, getNotice, getNoticeContentById } from '@/api/system/notice';
import Editor from '@/components/editor/Editor.vue';
import BaseForm from '@/components/form/BaseForm.vue';
import useDictStore, { dictTypeConfig } from '@/store/modules/dict';
import { onMounted, ref, watch } from 'vue';
const formRef = ref()
const form = ref(init())
const rules = ref({
    title: [{ required: true, message: "公告标题不能为空", trigger: "blur" }],
    noticeType: [{ required: true, message: "公告类型不能为空", trigger: "change" }]
})
const title = ref('通告')
const sys_notice_type = ref([])
const sys_notice_status = ref([])

onMounted(() => {
    useDictStore().getDictData(dictTypeConfig.sys_notice_type).then((data) => {
        sys_notice_type.value = data
    })
    useDictStore().getDictData(dictTypeConfig.sys_notice_status).then(data => {
        sys_notice_status.value = data
    }) 
})

function init() {
    return {
        noticeId: '',
        title: '',
        noticeType: 1,
        status: 1,
        content: ''
    }
}
function openForm(noticeId) {
    formRef.value.openForm(noticeId)
}

function resetForm(){
    form.value = init()
}

const emit = defineEmits(['add', 'update'])


defineExpose({ openForm })

watch(() => form.value.noticeId, (val) => {
    if(val){
       getNoticeContentById(val).then(res=>{
        form.value.content = res.data
       })
    }
})
</script>