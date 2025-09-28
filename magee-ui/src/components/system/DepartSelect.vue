<template>
    <el-tree-select v-model="innerValue" :data="departList"
        :props="{ value: 'departId', label: 'name', children: 'children' }" value-key="departId" placeholder="选择上级部门"
        :check-strictly="true" show-checkbox :disabled="disabled" :multiple="multiple" style="width: 100%" />
</template>
<script lang="ts" setup>
import { getDepartList } from '@/api/comm';
import { handleTree } from '@/utils';
import { onMounted, ref, watch } from 'vue';


const props = defineProps({
    modelValue: {
        type:  [String, Number] ,
        required: true
    },
    disabled: {
        type: Boolean,
        default: false
    },
    multiple: {
        type: Boolean,
        default: false
    },
})

const emit = defineEmits(['update:modelValue'])

const departList = ref([])
const innerValue = ref( props.modelValue || '')

 
/* -------------- 双向绑定 -------------- */ 
watch(innerValue, val => emit('update:modelValue', val))
watch(() => props.modelValue, val => (innerValue.value = String(val)))


onMounted(() => {
    getDepartList().then((res: any) => { 
        departList.value = handleTree(res.data || [] , 'departId') 
    })
})


</script>