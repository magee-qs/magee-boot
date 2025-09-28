<template>
    <BaseForm v-model="form" :title="title" :rules="rules" ref="formRef" :add="addSysJob" :update="updateSysJob"
        :load="getSysJob" :showFooter="showFooter" :init="init" @resetForm="resetForm" @add="emit('add')"
        @update="emit('update')" height="460px">
        <el-col :span="12">
            <el-form-item label="任务名称" prop="jobName">
                <el-input v-model="form.jobName" placeholder="请输入任务名称" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="任务组" prop="jobGroup">
                <el-input v-model="form.jobGroup" placeholder="请输入任务组" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="任务类" prop="clazz">
                <el-input v-model="form.clazz" placeholder="请输入任务类" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="执行方法" prop="method">
                <el-input v-model="form.method" placeholder="请输入执行方法" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="参数" prop="param">
                <el-input v-model="form.param" placeholder="请输入参数" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="表达式" prop="cron">
                <el-input v-model="form.cron" placeholder="请输入表达式" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-tooltip class="box-item" effect="dark" content="策略 1 立刻执行 2 执行一次 3 放弃" placement="top-start">
                <el-form-item label="策略" prop="policy">
                    <el-input v-model="form.policy" placeholder="请输入策略 1 立刻执行 2 执行一次 3 放弃" />
                </el-form-item>
            </el-tooltip>

        </el-col> 
    </BaseForm>
</template>
<script lang="ts" setup>
import { addSysJob, getSysJob, updateSysJob } from '@/api/monitor/job';
import BaseForm from '@/components/form/BaseForm.vue';
import { ref } from 'vue';

defineProps({
    showFooter: {
        type: Boolean,
        default: true
    }
})

const form = ref(init())

/** 表单数据初始化 */
function init() {
    return {
        jobId: '',
        jobName: '',
        jobGroup: '',
        clazz: '',
        method: '',
        param: '',
        cron: '',
        policy: 3,
        status: 0,
    }
}
const formRef = ref()
const title = ref('定时任务')
const rules = {
    jobId: [{ required: true, message: "任务id不能为空", trigger: "blur" }],
}

defineExpose({ openForm })

/** 显示表单 */
function openForm(postId) {
    formRef.value.openForm(postId)
}

/** 重置表单 */
function resetForm() {
    form.value = init()
}

const emit = defineEmits(['add', 'update'])
</script>