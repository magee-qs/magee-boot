<template>
    <BaseFrame :src="url"></BaseFrame>

</template>
<script lang="ts" setup>
import BaseFrame from '@/components/frame/BaseFrame.vue';
import { ref } from 'vue';


const url = ref(import.meta.env.VITE_APP_BASE_API + '/druid/login.html')
</script>