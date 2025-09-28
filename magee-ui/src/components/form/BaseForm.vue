<template>
    <el-dialog v-model="dialogVisible" :title="formTitle" :width="width" :height="height"  append-to-body @close="closeForm">
        <div class="form-body-height" :style="{ height: height }">
            <el-form ref="formRef" :model="form" :rules="rules" :label-width="labelWidth">
                <el-row>
                    <slot></slot>
                </el-row>
            </el-form>
        </div>
        <template #footer>
            <div class="dialog-footer" v-if="showFooter">
                <el-button @click="handleCancel">取消</el-button>
                <el-button type="primary" @click="handleSubmit"> 确定 </el-button>
            </div>
        </template>
    </el-dialog>
</template>
<script lang="ts" setup>
import useMessage from '@/hooks/message';
import { computed, ref, watch } from 'vue';

const props = defineProps({
    title: {
        default: '表单填报',
        type: String,
    },
    width: {
        type: [Number, String],
        default: '600px',
    },
    height: {
        type: [Number, String],
        default: '450px'
    },
    labelWidth: {
        default: '80px',
        type: [Number, String]
    },
    modelValue: {
        required: true
    },
    rules: {
        type: Object,
        required: true
    },
    add: {
        required: true,
        type: Function
    },
    update: {
        required: true,
        type: Function
    },
    load: {
        require: true,
        type: Function
    },
    init: {
        require: true,
        type: Function
    },
    showFooter: {
        type: Boolean,
        default: true
    }

})

const dialogVisible = ref(false)
const form = ref(props.modelValue)
const formRef = ref()
const emit = defineEmits(['add', 'update', 'update:modelValue', 'resetForm'])

 
// 数据双向绑定
watch(() => props.modelValue, (val) => form.value = val)
watch(form, val => emit('update:modelValue', val))

const action = ref('add')

const formTitle  = computed(() => {
    if(action.value == 'add'){
        return '新增' + props.title
    }else if(action.value == 'update'){
        return '修改' + props.title
    }else {
        return props.title
    }
})
/** 打开窗口 */
function openForm(param) {
    dialogVisible.value = true
    if (param) {
        action.value = 'update'
        // 加载数据
        if (props.load) {
            props.load(param).then(res => {
                form.value = res.data
            })
        }
    } else {
        action.value = 'add'
    }
}

/** 提交数据 */
function handleSubmit() {
    formRef.value.validate((valid) => {
        if (valid) {
            if (action.value == 'add') {
                // 新增数据
                if (props.add) {
                    props.add(form.value).then(() => {
                        useMessage.success()
                        // 重置表单
                        closeForm()
                        // 添加成功 
                        emit('add')
                    })
                }
            } else if (action.value == 'update') {
                // 更新数据
                if (props.update) {
                    props.update(form.value).then(() => {
                        useMessage.success()
                        // 重置表单
                        closeForm()
                        // 修改
                        emit('update')
                    })
                }
            }
        }
    })
}

/** 取消操作 */
function handleCancel() {
    closeForm()
}

function closeForm() {
    if(props.init){
        form.value = props.init()
    }
    emit('resetForm')
    dialogVisible.value = false
}

defineExpose({ openForm, closeForm })
</script>

<style lang="scss" scoped>
.form-body-height {
    overflow: auto;
}
</style>