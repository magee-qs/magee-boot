<template>
    <div>
        <svg-icon icon-class="github" @click="to" ></svg-icon>
    </div>
</template>
<script lang="ts" setup>
import { ref } from 'vue';

const url = ref('https://gitee.com/y_project/RuoYi-Vue')

function to() {
  window.open(url.value)
}
</script>