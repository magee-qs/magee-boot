<template>
    <el-select v-model="innerValue" placeholder="请选择" @change="onChange" :multiple="multiple" :disabled="disabled"  style="width: 100%">
        <el-option :label="item.label" :value="item.value" :key="index"
            v-for="(item, index) in dictData"></el-option>
    </el-select>
</template>
<script lang="ts" setup> 
import useDictStore from '@/store/modules/dict' 
import { onMounted, ref , watch } from 'vue'

const props = defineProps({
    dictType: {
        type: String,
        default: ''
    },
    multiple: {
        type: Boolean,
        default: false
    },
    modelValue: {
        type: [String, Number],
        required: true
    },
    disabled: {
        type: Boolean,
        default: false
    }
})

const dictData:any = ref([])
const innerValue = ref(props.modelValue || '')
const dictStore = useDictStore()

const emit = defineEmits(['change','update:modelValue'])
 
 
 /* -------------- 双向绑定 -------------- */
watch(() => props.modelValue, val => (innerValue.value = String(val)))
watch(innerValue, val => emit('update:modelValue', val))
 
onMounted(() => {
    console.log('监听dictType--加载dictData', props.dictType)
    dictStore.getDictData(props.dictType).then(data => {
        dictData.value = data
        console.log('加载dict完成', dictData.value)
    })
   
})

 

</script>