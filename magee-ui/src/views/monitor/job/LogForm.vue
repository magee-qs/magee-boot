<template>
    <BaseForm v-model="form" :title="title" :rules="rules" ref="formRef"
              :add="addSysJobLog" :update="updateSysJobLog" :load="getSysJobLog" :showFooter="showFooter"
              :init="init"   @resetForm="resetForm" @add="emit('add')"  @update="emit('update')" height="460px">
        <el-col :span="12">
            <el-form-item label="任务名" prop="jobName">
                <el-input v-model="form.jobName" placeholder="请输入任务名" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="任务分组" prop="jobGroup">
                <el-input v-model="form.jobGroup" placeholder="请输入任务分组" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="目标字符串" prop="invokeTarget">
                <el-input v-model="form.invokeTarget" placeholder="请输入目标字符串" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="日志信息" prop="jobMessage">
                <el-input v-model="form.jobMessage" placeholder="请输入日志信息" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="执行状态" prop="status">
                <el-input v-model="form.status" placeholder="请输入执行状态" />
            </el-form-item>
        </el-col>
        <el-col :span="12">
            <el-form-item label="异常信息" prop="errorInfo">
                <el-input v-model="form.errorInfo" placeholder="请输入异常信息" />
            </el-form-item>
        </el-col>
    </BaseForm>
</template>
<script lang="ts" setup>
    import { addSysJobLog, getSysJobLog, updateSysJobLog } from '@/api/monitor/jobLog';
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
           logId: '',
           jobName: '',
           jobGroup: '',
           invokeTarget: '',
           jobMessage: '',
           status: '',
           errorInfo: '',
        }
    }
    const formRef = ref()
    const title = ref('定时任务日志')
    const rules = {
        logId: [{ required: true, message: "日志id不能为空", trigger: "blur" }],
    }

    defineExpose({ openForm })

    /** 显示表单 */
    function openForm(postId) {
        formRef.value.openForm(postId)
    }

    /** 重置表单 */
    function resetForm(){
        form.value = init()
    }

    const emit = defineEmits(['add','update'])
</script>