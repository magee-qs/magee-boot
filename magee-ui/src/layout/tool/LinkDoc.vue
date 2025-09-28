<template>
    <div>
        <svg-icon icon-class="question" @click="to" />
    </div>
</template>
<script lang="ts" setup>
import { ref } from 'vue';

const url = ref('http://doc.ruoyi.vip/ruoyi-vue')
function  to(){
    window.open(url.value)
}
</script>