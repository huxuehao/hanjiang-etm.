<script setup lang="ts">
import { RouterView } from 'vue-router'
import { useUserStore } from "@/stores/user";
import { ref, watch } from 'vue';

let loading = ref(false)
const userStore = useUserStore();
watch(() => userStore.globalLoading, (val) => {
  loading.value = val
})

</script>

<template>
  <RouterView v-loading="loading" element-loading-background="rgba(122, 122, 122, 0.3)"/>
</template>

<style lang="scss">

body {
  margin: 0;
}

#nprogress .bar {
  background: #2f86de !important;
  height: 0.5px !important;
}

.hover {
  transition: 200ms;
}
.hover:hover {
  color: #409EFF;
}

.pointer {
  cursor: pointer;
}

.resize-left-box {
  height: 100%; 
  border-right: 1px solid #EBEEF5;
}

.resize-right-box {
  height: 100%; 
}
</style>
<style scoped></style>
