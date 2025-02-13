<script lang="ts" setup>
import { isEmpty } from '@/utils/tools';
import { onMounted, ref } from 'vue';

const props = defineProps({
  config: {
    type: Object,
    default: () => {},
  },
});

const rules = {
  localDir: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ]
};

let protocolConfig = ref<any>({})

onMounted(() => {
  if(!isEmpty(props.config)) {
    protocolConfig.value = props.config
  }
})

const getConfig = () => {
  return protocolConfig.value 
};

let form = ref()

defineExpose({form, getConfig})

</script>

<template>
  <el-form ref="form" :model="protocolConfig" :rules="rules" label-width="80px">
    <el-row :gutter="0">
      <el-col :span="24">
        <el-form-item label="本地目录" prop="localDir">
          <el-input
            placeholder="请填写本地目录"
            maxlength="100"
            v-model="protocolConfig.localDir"
            clearable
          ></el-input>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<style lang="scss" scoped></style>
