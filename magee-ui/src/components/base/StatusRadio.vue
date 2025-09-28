<template>
    <el-radio-group v-model="innerValue" :disabled="disabled" style="width: 100%;">
        <el-radio value="1">{{trueLabel}}</el-radio>
        <el-radio value="0">{{falseLabel}}</el-radio>
    </el-radio-group>
</template>
<script lang="ts" setup>
import { ref, watch } from 'vue';

const props = defineProps({
    modelValue: {
        type: [Number, String],
        required: true
    },
    disabled: {
        type: Boolean,
        default: false
    },
    trueLabel: {
        default: '启用'
    },
    falseLabel: {
        default: '禁用'
    }
})

const innerValue = ref(String(props.modelValue))

const emit = defineEmits(['update:modalValue'])
watch(() => props.modelValue, val => innerValue.value = String(val))
watch(innerValue, val => emit('update:modalValue', val))
</script>