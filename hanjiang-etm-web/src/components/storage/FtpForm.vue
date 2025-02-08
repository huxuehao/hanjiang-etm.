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
  host: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  port: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  userName: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  password: [
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
        <el-form-item label="Host" prop="host">
          <el-input
            placeholder="请填写Host"
            :maxLength="15"
            v-model="protocolConfig.host"
            clearable
          ></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="0">
      <el-col :span="24">
        <el-form-item label="port" prop="port">
          <el-input
            placeholder="请填写Port"
            :maxLength="6"
            v-model="protocolConfig.port"
            clearable
          ></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="0">
      <el-col :span="24">
        <el-form-item label="UserName" prop="userName">
          <el-input
            placeholder="请填写UserName"
            :maxLength="100"
            v-model="protocolConfig.userName"
            clearable
          ></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="0">
      <el-col :span="24">
        <el-form-item label="Password" prop="password">
          <el-input
            placeholder="请填写Password"
            :maxLength="100"
            v-model="protocolConfig.password"
            clearable
          ></el-input>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<style lang="scss" scoped></style>
