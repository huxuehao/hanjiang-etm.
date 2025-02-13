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
  endpoint: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  accessKey: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  secretKey: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  bucketName: [
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
  <el-form ref="form" :model="protocolConfig" :rules="rules" label-width="90px">
    <el-row :gutter="0">
      <el-col :span="24">
        <el-form-item label="服务地址" prop="endpoint">
          <el-input
            placeholder="请填写服务地址"
            maxlength="35"
            v-model="protocolConfig.endpoint"
            clearable
          ></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="0">
      <el-col :span="24">
        <el-form-item label="AccessKey" prop="accessKey">
          <el-input
            placeholder="请填写AccessKey"
            maxlength="100"
            v-model="protocolConfig.accessKey"
            clearable
          ></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="0">
      <el-col :span="24">
        <el-form-item label="SecretKey" prop="secretKey">
          <el-input
            placeholder="请填写SecretKey"
            maxlength="100"
            v-model="protocolConfig.secretKey"
            clearable
          ></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="0">
      <el-col :span="24">
        <el-form-item label="BucketName" prop="bucketName">
          <el-input
            placeholder="请填写BucketName"
            maxlength="100"
            v-model="protocolConfig.bucketName"
            clearable
          ></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="0">
      <el-col :span="24">
        <el-form-item label="AppId" prop="appId">
          <el-input
            placeholder="请填写AppId"
            maxlength="100"
            v-model="protocolConfig.appId"
            clearable
          ></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="0">
      <el-col :span="24">
        <el-form-item label="Region" prop="region">
          <el-input
            placeholder="请填写Region"
            maxlength="100"
            v-model="protocolConfig.region"
            clearable
          ></el-input>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<style lang="scss" scoped></style>
