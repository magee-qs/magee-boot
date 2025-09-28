<template>
    <BaseForm v-model="form" :title="title" :rules="rules" ref="formRef"
              :add="add${entityName}" :update="update${entityName}" :load="get${entityName}" :showFooter="showFooter"
              :init="init"   @resetForm="resetForm" @add="emit('add')"  @update="emit('update')" height="460px">
        <#list columns as column>
            <#if column.isKey == false>
        <el-col :span="12">
            <el-form-item label="${column.comment}" prop="${column.fieldName}">
                <el-input v-model="form.${column.fieldName}" placeholder="请输入${column.comment}" />
            </el-form-item>
        </el-col>
            </#if>
        </#list>
    </BaseForm>
</template>
<script lang="ts" setup>
    import { add${entityName}, get${entityName}, update${entityName} } from '@/api${url}';
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
          <#list columns as column>
           ${column.fieldName}: '',
          </#list>
        }
    }
    const formRef = ref()
    const title = ref('${comment}')
    const rules = {
        <#list columns as column>
            <#if column.nullable == 0>
        ${column.fieldName}: [{ required: true, message: "${column.comment}不能为空", trigger: "blur" }],
            </#if>
        </#list>
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